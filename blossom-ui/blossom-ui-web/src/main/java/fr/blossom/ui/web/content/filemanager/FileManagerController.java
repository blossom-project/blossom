package fr.blossom.ui.web.content.filemanager;

import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.module.filemanager.FileDTO;
import fr.blossom.module.filemanager.FileService;
import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@BlossomController
@RequestMapping("/content/filemanager")
@OpenedMenu("filemanager")
public class FileManagerController {

  private static final Logger logger = LoggerFactory.getLogger(FileManagerController.class);
  private final FileService fileService;
  private final SearchEngineImpl<FileDTO> searchEngine;

  public FileManagerController(FileService fileService, SearchEngineImpl<FileDTO> searchEngine) {
    this.fileService = fileService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('content:filemanager:read')")
  public ModelAndView getPage(Model model) {
    return new ModelAndView("blossom/filemanager/filemanager", model.asMap());
  }

  @GetMapping("/files")
  @PreAuthorize("hasAuthority('content:filemanager:read')")
  public ModelAndView getFiles(Model model,
    @PageableDefault(size = 20) Pageable pageable,
    @RequestParam(value = "q", defaultValue = "", required = false) String q) {
    Page<FileDTO> files = null;
    if (!StringUtils.isEmpty(q)) {
      files = searchEngine.search(q, pageable).getPage();
    } else {
      files = fileService.getAll(pageable);
    }
    model.addAttribute("files", files);
    return new ModelAndView("blossom/filemanager/filelist", model.asMap());
  }

  @PostMapping(value = "/files", consumes = "multipart/form-data")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAuthority('content:filemanager:create')")
  public void fileUpload(@RequestParam("file") MultipartFile uploadedFile,
    @RequestParam(value = "tags", required = false) Optional<List<String>> tags) {
    if (uploadedFile.isEmpty()) {
      return;
    }
    try {
      fileService.upload(uploadedFile);
    } catch (IOException | SQLException e) {
      logger.error("Cannot save multipart file !", e);
    }
  }

}
