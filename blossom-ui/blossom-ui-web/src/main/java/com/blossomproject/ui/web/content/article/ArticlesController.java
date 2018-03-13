package com.blossomproject.ui.web.content.article;

import com.blossomproject.module.article.*;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@BlossomController
@RequestMapping("/content/articles")
@OpenedMenu("articles")
public class ArticlesController {

  private static final Logger logger = LoggerFactory.getLogger(ArticlesController.class);
  private final ArticleService articleService;
  private final SearchEngineImpl<ArticleDTO> searchEngine;

  public ArticlesController(ArticleService articleService,
    SearchEngineImpl<ArticleDTO> searchEngine) {
    this.articleService = articleService;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('content:articles:read')")
  public ModelAndView getArticlesPage(@RequestParam(value = "q", required = false) String q,
    @PageableDefault(size = 25) Pageable pageable, Model model) {
    Page<ArticleDTO> articles;

    if (Strings.isNullOrEmpty(q)) {
      articles = this.articleService.getAll(pageable);
    } else {
      articles = this.searchEngine.search(q, pageable).getPage();
    }

    model.addAttribute("articles", articles);
    model.addAttribute("q", q);

    return new ModelAndView("blossom/articles/articles", model.asMap());
  }

  @GetMapping("/_create")
  @PreAuthorize("hasAuthority('content:articles:create')")
  public ModelAndView getArticleCreatePage(Model model, Locale locale) {
    ArticleCreateForm articleCreateForm = new ArticleCreateForm();
    return this.createView(articleCreateForm, model);
  }

  @PostMapping("/_create")
  @PreAuthorize("hasAuthority('content:articles:create')")
  public ModelAndView handleArticleCreateForm(
    @Valid @ModelAttribute("articleCreateForm") ArticleCreateForm articleCreateForm,
    BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return this.createView(articleCreateForm, model);
    }
      ArticleDTO article = this.articleService.create(articleCreateForm);
      return new ModelAndView("redirect:../articles/" + article.getId());
  }

  private ModelAndView createView(ArticleCreateForm articleCreateForm, Model model) {
    model.addAttribute("articleCreateForm", articleCreateForm);
    return new ModelAndView("blossom/articles/create", model.asMap());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('content:articles:read')")
  public ModelAndView getArticle(@PathVariable Long id, Model model, HttpServletRequest request) {
    ArticleDTO article = this.articleService.getOne(id);
    if (article == null) {
      throw new NoSuchElementException(String.format("Article=%s not found", id));
    }
    model.addAttribute("article", article);
    return new ModelAndView("blossom/articles/article", "article", article);
  }

  @PostMapping("/{id}/_delete")
  @PreAuthorize("hasAuthority('content:articles:delete')")
  public @ResponseBody
  ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> deleteArticle(
    @PathVariable Long id,
    @RequestParam(value = "force", required = false, defaultValue = "false") Boolean force) {
    Optional<Map<Class<? extends AbstractDTO>, Long>> result = this.articleService
      .delete(this.articleService.getOne(id), force);

    if (!result.isPresent() || result.get().isEmpty()) {
      return new ResponseEntity<>(Maps.newHashMap(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(result.get(), HttpStatus.CONFLICT);
    }
  }


  @GetMapping("/{id}/_informations")
  @PreAuthorize("hasAuthority('content:articles:read')")
  public ModelAndView getArticleInformations(@PathVariable Long id, Model model, HttpServletRequest request) {
    ArticleDTO article = this.articleService.getOne(id);
    if (article == null) {
      throw new NoSuchElementException(String.format("Article=%s not found", id));
    }
    return this.viewArticleInformationView(article,model);
  }

  @GetMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('content:articles:write')")
  public ModelAndView getArticleInformationsForm(@PathVariable Long id, Model model, Locale locale) {
    ArticleDTO article = this.articleService.getOne(id);
    if (article == null) {
      throw new NoSuchElementException(String.format("Article=%s not found", id));
    }
    return this.updateArticleInformationView(new ArticleUpdateForm(article),model,locale);
  }

  @PostMapping("/{id}/_informations/_edit")
  @PreAuthorize("hasAuthority('content:articles:write')")
  public ModelAndView handleArticleInformationsForm(@PathVariable Long id, Model model,
    @Valid @ModelAttribute("articleUpdateForm") ArticleUpdateForm articleUpdateForm,
    BindingResult bindingResult,Locale locale) {
    if (bindingResult.hasErrors()) {
      return this.updateArticleInformationView(articleUpdateForm,model,locale);
    }

    ArticleDTO article = this.articleService.getOne(id);
    if (article == null) {
      throw new NoSuchElementException(String.format("Article=%s not found", id));
    }
    ArticleDTO updatedArticle = this.articleService.update(id, articleUpdateForm);
    return this.viewArticleInformationView(updatedArticle,model);
  }

  private ModelAndView viewArticleInformationView(ArticleDTO article,Model model) {
    model.addAttribute("statuslist", Article.Status.values());
    model.addAttribute("article", article);
    return new ModelAndView("blossom/articles/articleinformations", model.asMap());
  }

  private ModelAndView updateArticleInformationView(ArticleUpdateForm articleUpdateForm,Model model,Locale locale) {
    model.addAttribute("statuslist", Article.Status.values());
      if(locale.toString().equals("fr"))
          model.addAttribute("translate","fr-FR");
      else model.addAttribute("translate","en-US");
      model.addAttribute("articleUpdateForm",articleUpdateForm);
    return new ModelAndView("blossom/articles/articleinformations-edit", model.asMap());
  }
}
