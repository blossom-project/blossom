package com.blossomproject.ui.theme;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.servlet.ThemeResolver;


public class ThemeServlet extends HttpServlet {

  public final static String BLOSSOM_THEME_SCSS_SERVLET = "/blossom/public/theme/style.css";
  private final ThemeResolver themeResolver;
  private final ThemeSource themeSource;
  private final ThemeCompiler themeCompiler;

  public ThemeServlet(ThemeResolver themeResolver,
    ThemeSource themeSource, ThemeCompiler themeCompiler) {
    this.themeResolver = themeResolver;
    this.themeSource = themeSource;
    this.themeCompiler = themeCompiler;
  }

  @Override
  protected void doGet(
    final HttpServletRequest request,
    final HttpServletResponse response
  ) throws ServletException, IOException {
    String themeName = themeResolver.resolveThemeName(request);
    if (themeName == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
      return;
    }
    Theme theme = (Theme) themeSource.getTheme(themeName);
    if (theme == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
      return;
    }

    response.setContentType("text/css");
    themeCompiler.getCss(theme.getName(), response.getOutputStream());
  }
}
