package com.blossomproject.ui.web.content.article;


import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.common.search.SearchResult;

import com.blossomproject.module.article.ArticleCreateForm;
import com.blossomproject.module.article.ArticleDTO;
import com.blossomproject.module.article.ArticleService;
import com.blossomproject.module.article.ArticleUpdateForm;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;



import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticlesControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ArticleService articleService;

    @Mock
    private SearchEngineImpl<ArticleDTO> searchEngine;


    private Locale locale = new Locale("en");

    private ArticlesController articlesController;

    @Before
    public void setUp(){articlesController=new ArticlesController(articleService,searchEngine);}

    @Test
    public void should_display_paged_articles_without_query_parameter() throws Exception{
        when(articleService.getAll(any(Pageable.class))).thenAnswer(a-> new PageImpl<ArticleDTO>(Lists.newArrayList()));
        ModelAndView modelAndView = articlesController.getArticlesPage(null, PageRequest.of(0,20),new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/articles/articles"));
        assertTrue(modelAndView.getModel().get("articles") instanceof Page);
        verify(articleService,times(1)).getAll(any(Pageable.class));
    }

    @Test
    public void should_display_paged_articles_with_query_parameter() throws Exception {
        when(searchEngine.search(any(String.class), any(Pageable.class)))
                .thenAnswer(a -> new SearchResult<>(0, new PageImpl<ArticleDTO>(Lists.newArrayList())));
        ModelAndView modelAndView = articlesController.getArticlesPage("test", PageRequest.of(0, 10), new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/articles/articles"));
        assertTrue(modelAndView.getModel().get("articles") instanceof Page);
        verify(searchEngine, times(1)).search(eq("test"), any(Pageable.class));
    }



    @Test
    public void should_display_create_page() throws Exception {
        ArticleCreateForm articleCreateForm = new ArticleCreateForm();
        ModelAndView modelAndView = articlesController.getArticleCreatePage(new ExtendedModelMap(), Locale.FRANCE);
        assertTrue(modelAndView.getViewName().equals("blossom/articles/create"));
        assertTrue(EqualsBuilder.reflectionEquals(articleCreateForm, modelAndView.getModel().get("articleCreateForm")));
    }

    @Test
    public void should_handle_create_without_form_error() throws Exception {
        ArticleCreateForm form = new ArticleCreateForm();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        ArticleDTO articleToCreate = new ArticleDTO();
        articleToCreate.setId(1L);
        when(articleService.create(any(ArticleCreateForm.class))).thenReturn(articleToCreate);
        ModelAndView modelAndView = articlesController.handleArticleCreateForm(form, result, new ExtendedModelMap());
        verify(articleService, times(1)).create(eq(form));
        assertTrue(modelAndView.getViewName().equals("redirect:../articles/1"));
    }


    @Test
    public void should_handle_create_with_form_error() throws Exception {
        ArticleCreateForm form = new ArticleCreateForm();
        form.setName("name");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        ModelAndView modelAndView = articlesController.handleArticleCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/articles/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("articleCreateForm")));
    }


    @Test
    public void should_handle_create_with_exception() throws Exception {
        ArticleCreateForm form = new ArticleCreateForm();
        form.setName("name");
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(this.articleService.create(form)).thenThrow(new NullPointerException());
        ModelAndView modelAndView = articlesController.handleArticleCreateForm(form, result, new ExtendedModelMap());
        assertTrue(modelAndView.getViewName().equals("blossom/articles/create"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("articleCreateForm")));
    }

    @Test
    public void should_display_one_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(articleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Article=%s not found", 1L));
        articlesController.getArticle(1L, new ExtendedModelMap(), req);
    }

    @Test
    public void should_display_one_with_id_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(1L);
        when(articleService.getOne(any(Long.class))).thenReturn(articleDTO);
        ModelAndView modelAndView = articlesController.getArticle(1L, new ExtendedModelMap(), req);
        assertTrue(modelAndView.getViewName().equals("blossom/articles/article"));
        assertTrue(EqualsBuilder.reflectionEquals(articleDTO, modelAndView.getModel().get("article")));
        verify(articleService, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_informations_with_id_not_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(articleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Article=%s not found", 1L));
        articlesController.getArticleInformations(1L,req);
    }

    @Test
    public void should_display_one_informations_with_id_found() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(1L);
        when(articleService.getOne(any(Long.class))).thenReturn(articleDTO);
        ModelAndView modelAndView = articlesController.getArticleInformations(1L,req);
        assertTrue(modelAndView.getViewName().equals("blossom/articles/articleinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(articleDTO, modelAndView.getModel().get("article")));
        verify(articleService, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_display_one_form_edit_with_id_not_found() throws Exception {
        when(articleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Article=%s not found", 1L));
        articlesController.getArticleInformationsForm(1L, new ExtendedModelMap(),locale);
    }

    @Test
    public void should_display_one_form_edit_with_id_found() throws Exception {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(1L);
        articleDTO.setName("name");
        when(articleService.getOne(any(Long.class))).thenReturn(articleDTO);
        ModelAndView modelAndView = articlesController.getArticleInformationsForm(1L, new ExtendedModelMap(),locale);
        assertTrue(modelAndView.getViewName().equals("blossom/articles/articleinformations-edit"));
        ArticleUpdateForm articleUpdateForm = (ArticleUpdateForm) modelAndView.getModel().get("articleUpdateForm");
        assertTrue(articleUpdateForm.getName().equals(articleDTO.getName()));
        verify(articleService, times(1)).getOne(eq(1L));
    }

    @Test
    public void should_handle_update_without_id_found() throws Exception {
        BindingResult result = mock(BindingResult.class);
        when(articleService.getOne(any(Long.class))).thenReturn(null);
        thrown.expect(NoSuchElementException.class);
        thrown.expectMessage(String.format("Article=%s not found", 1L));
        articlesController.handleArticleInformationsForm(1L, new ExtendedModelMap(), new ArticleUpdateForm(), result,locale);
    }

    @Test
    public void should_handle_update_without_form_error() throws Exception {
        ArticleUpdateForm form = new ArticleUpdateForm();
        form.setName("name");
        form.setViewable(false);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        ArticleDTO articleToUpdate = new ArticleDTO();
        articleToUpdate.setId(1L);
        articleToUpdate.setName("eman");
        articleToUpdate.setViewable(false);

        ArticleDTO articleUpdated = new ArticleDTO();
        articleUpdated.setId(1L);
        articleUpdated.setName("name");
        articleToUpdate.setViewable(false);

        when(articleService.getOne(any(Long.class))).thenReturn(articleToUpdate);
        when(articleService.update(any(Long.class), any(ArticleUpdateForm.class))).thenReturn(articleUpdated);

        ModelAndView modelAndView = articlesController.handleArticleInformationsForm(1L, new ExtendedModelMap(), form, result,locale);
        verify(articleService, times(1)).update(eq(1L), eq(form));
        assertTrue(modelAndView.getViewName().equals("blossom/articles/articleinformations"));
        assertTrue(EqualsBuilder.reflectionEquals(articleUpdated, modelAndView.getModel().get("article")));
    }

    @Test
    public void should_handle_update_with_form_error() throws Exception {
        ArticleUpdateForm form = new ArticleUpdateForm();
        form.setName("name");

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        ModelAndView modelAndView = articlesController.handleArticleInformationsForm(1L, new ExtendedModelMap(), form, result,locale);
        assertTrue(modelAndView.getViewName().equals("blossom/articles/articleinformations-edit"));
        assertTrue(EqualsBuilder.reflectionEquals(form, modelAndView.getModel().get("articleUpdateForm")));
    }


    @Test
    public void should_delete_one_with_id_not_found() throws Exception {
        Long id = 1L;
        when(articleService.getOne(any(Long.class))).thenReturn(null);
        ResponseEntity<Map<Class<? extends AbstractDTO>, Long>> response = articlesController.deleteArticle(id, false);
        verify(articleService, times(1)).getOne(eq(id));
        verify(articleService, times(0)).delete(any(ArticleDTO.class), anyBoolean());
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody().isEmpty());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }




}
