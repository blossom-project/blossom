package fr.mgargadennec.blossom.ui.web.content.article;

import com.google.common.base.Strings;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.module.article.ArticleCreateForm;
import fr.mgargadennec.blossom.module.article.ArticleDTO;
import fr.mgargadennec.blossom.module.article.ArticleService;
import fr.mgargadennec.blossom.module.article.ArticleUpdateForm;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@BlossomController("/content/articles")
@OpenedMenu("articles")
public class ArticlesController {
    private static final Logger logger = LoggerFactory.getLogger(ArticlesController.class);
    private final ArticleService articleService;
    private final SearchEngineImpl<ArticleDTO> searchEngine;

    public ArticlesController(ArticleService articleService, SearchEngineImpl<ArticleDTO> searchEngine) {
        this.articleService = articleService;
        this.searchEngine = searchEngine;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('content:articles:read')")
    public ModelAndView getArticlesPage(@RequestParam(value = "q", required = false) String q,
                                        @PageableDefault(size = 25) Pageable pageable, Model model) {
        return tableView(q, pageable, model, "articles/articles");
    }

    private ModelAndView tableView(String q, Pageable pageable, Model model, String viewName) {
        Page<ArticleDTO> articles;

        if (Strings.isNullOrEmpty(q)) {
            articles = this.articleService.getAll(pageable);
        } else {
            articles = this.searchEngine.search(q, pageable).getPage();
        }

        model.addAttribute("articles", articles);
        model.addAttribute("q", q);

        return new ModelAndView(viewName, model.asMap());
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
        try {
            ArticleDTO article = this.articleService.create(articleCreateForm);
            return new ModelAndView("redirect:../articles/" + article.getId());
        } catch (Exception e) {
            logger.error("Error on creating article, name " + articleCreateForm.getName() + " already exists ", e);
            return this.createView(articleCreateForm, model);
        }

    }

    private ModelAndView createView(ArticleCreateForm articleCreateForm, Model model) {
        model.addAttribute("articleCreateForm", articleCreateForm);
        return new ModelAndView("articles/create", model.asMap());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('content:articles:read')")
    public ModelAndView getGroup(@PathVariable Long id, Model model, HttpServletRequest request) {
        ArticleDTO article = this.articleService.getOne(id);
        if (article == null) {
            throw new NoSuchElementException(String.format("Article=%s not found", id));
        }
        model.addAttribute("article", article);
        return new ModelAndView("articles/article", "article", article);
    }

    @PostMapping("/{id}/_delete")
    @PreAuthorize("hasAuthority('content:articles:delete')")
    public String deleteGroup(@PathVariable Long id) {
        this.articleService.delete(this.articleService.getOne(id));
        return "redirect:..";
    }


    @GetMapping("/{id}/_informations")
    @PreAuthorize("hasAuthority('content:articles:read')")
    public ModelAndView getRoleInformations(@PathVariable Long id, HttpServletRequest request) {
        ArticleDTO article = this.articleService.getOne(id);
        if (article == null) {
            throw new NoSuchElementException(String.format("Article=%s not found", id));
        }
        return this.viewArticleInformationView(article);
    }

    @GetMapping("/{id}/_informations/_edit")
    @PreAuthorize("hasAuthority('content:articles:write')")
    public ModelAndView getArticleInformationsForm(@PathVariable Long id, Model model) {
        ArticleDTO article = this.articleService.getOne(id);
        if (article == null) {
            throw new NoSuchElementException(String.format("Article=%s not found", id));
        }
        return this.updateArticleInformationView(new ArticleUpdateForm(article), model);
    }

    @PostMapping("/{id}/_informations/_edit")
    @PreAuthorize("hasAuthority('content:articles:write')")
    public ModelAndView handleArticleInformationsForm(@PathVariable Long id, Model model,
                                                      @Valid @ModelAttribute("articleUpdateForm") ArticleUpdateForm articleUpdateForm,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.updateArticleInformationView(articleUpdateForm, model);
        }

        ArticleDTO article = this.articleService.getOne(id);
        if (article == null) {
            throw new NoSuchElementException(String.format("Article=%s not found", id));
        }
        article.setName(articleUpdateForm.getName());
        ArticleDTO updatedArticle = this.articleService.update(id, article);

        return this.viewArticleInformationView(updatedArticle);
    }

    private ModelAndView viewArticleInformationView(ArticleDTO article) {
        return new ModelAndView("articles/articleinformations", "article", article);
    }

    private ModelAndView updateArticleInformationView(ArticleUpdateForm articleUpdateForm, Model model) {
        return new ModelAndView("articles/articleinformations-edit", "articleUpdateForm", articleUpdateForm);
    }
}
