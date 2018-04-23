package com.blossomproject.ui.theme;

import com.blossomproject.core.common.utils.misc.InMemoryURLFactory;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.plugin.core.PluginRegistry;

public class ThemeCompilerImpl implements ThemeCompiler, CommandLineRunner, Ordered {

  private final static String SCSS_PATH = "classpath:/scss/%s.scss";
  private final static String[] FILENAMES = new String[]{"style","style_mail"};

  private final PluginRegistry<Theme, String> registry;
  private final ResourceLoader resourceLoader;
  private final ConcurrentHashMap<String, String> cache;

  public ThemeCompilerImpl(
    PluginRegistry<Theme, String> registry,
    ResourceLoader resourceLoader) {
    this.registry = registry;
    this.resourceLoader = resourceLoader;
    this.cache = new ConcurrentHashMap<>();
  }

  @Override
  public void run(String... args) throws Exception {
    doCompileAll();
  }

  @Override
  public void doCompileAll() throws Exception {
    this.cache.clear();

    for (Theme theme : registry.getPlugins()) {
      for (String filename : FILENAMES) {

        final String scssPath = String.format("classpath:/scss/%s.scss", filename);
        final URL scssResource = resourceLoader.getResource(scssPath).getURL();

        if (null != scssResource) {
          final String scssCode = IOUtils.toString(scssResource, StandardCharsets.UTF_8);

          Options options = new Options();
          options.setImporters(Collections.singleton(new CustomImporter(theme)));

          final Compiler compiler = new Compiler();
          final Output output = compiler.compileString(
            scssCode,
            new URI(scssPath),
            null,
            options
          );

          cache.put(theme.getName() + "_" + filename, output.getCss());
        }
      }
    }
  }

  @Override
  public void getCss(String theme, String filename, OutputStream os) throws IOException {
    if (this.cache.containsKey(theme + "_" + filename)) {
      os.write(this.cache.get(theme + "_" + filename).getBytes());
    }
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  private class CustomImporter implements Importer {

    private final Theme theme;

    public CustomImporter(Theme theme) {
      this.theme = theme;
    }

    /**
     * Resolve the target file for an {@code @import} directive.
     *
     * @param url The {@code import} url.
     * @param previous The file that contains the {@code import} directive.
     * @return The resolve import objects or {@code null} if the import file was not found.
     */
    private Collection<Import> doImport(String url, Import previous) {
      try {
        if (url.startsWith("/")) {
          // absolute imports are searched in /WEB-INF/scss
          return resolveImport(Paths.get("/scss").resolve(url));
        }

        // find in import paths
        final List<Path> importPaths = new LinkedList<>();

        // (a) relative to the previous import file
        final String previousPath = previous.getAbsoluteUri().getPath();
        final Path previousParentPath = Paths.get(previousPath).getParent();
        importPaths.add(previousParentPath);

        for (Path importPath : importPaths) {
          Path target = importPath.resolve(url);

          Collection<Import> imports = resolveImport(target);
          if (null != imports) {
            return imports;
          }
        }

        // file not found
        throw new FileNotFoundException(url);
      } catch (URISyntaxException | IOException e) {
        throw new RuntimeException(e);
      }
    }

    /**
     * Try to determine the import object for a given path.
     *
     * @param path The path to resolve.
     * @return The import object or {@code null} if the file was not found.
     */
    private Collection<Import> resolveImport(Path path) throws IOException, URISyntaxException {
      URL resource = resolveResource(path);

      if (null == resource) {
        return null;
      }
      final URI uri = resource.toURI();
      final String source = IOUtils.toString(resource, StandardCharsets.UTF_8);

      final Import scssImport = new Import(uri, uri, source);
      return Collections.singleton(scssImport);
    }

    /**
     * Try to find a resource for this path.
     *
     * <p>A sass import like {@code @import "foo"} does not contain the partial prefix (underscore)
     * or file extension. This method will try the following namings to find the import file {@code
     * foo}:</p> <ul> <li>_foo.scss</li> <li>_foo.css</li> <li>_foo</li> <li>foo.scss</li>
     * <li>foo.css</li> <li>foo</li> </ul>
     *
     * @param path The path to resolve.
     * @return The resource URL of the resolved file or {@code null} if the file was not found.
     */
    private URL resolveResource(Path path) throws IOException {
      final Path dir = path.getParent();
      final String basename = path.getFileName().toString();

      for (String prefix : new String[]{"_", ""}) {
        for (String suffix : new String[]{".scss", ".sass", ".css", ""}) {
          final Resource resource = resourceLoader
            .getResource("classpath:/scss/" + prefix + basename + suffix);
          if (resource != null && resource.exists()) {

            if (basename.equals("variables")) {
              String content = IOUtils.toString(resource.getURL(), StandardCharsets.UTF_8);
              for (Entry<String, String> message : theme.getMessages().entrySet()) {
                content = content.replace("%" + message.getKey() + "%", message.getValue());
              }
              return InMemoryURLFactory.getInstance().build("memory", content);
            }
            if (basename.equals("custom")) {
              return InMemoryURLFactory.getInstance()
                .build("memory", theme.getMessages().get("additionnalScss"));
            }

            return resource.getURL();
          }
        }
      }

      return null;
    }

    @Override
    public Collection<Import> apply(String url, Import previous) {
      return doImport(url, previous);
    }
  }
}
