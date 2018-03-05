package com.blossomproject.module.article;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.event.CreatedEvent;
import com.blossomproject.core.common.event.UpdatedEvent;
import com.blossomproject.core.common.service.AssociationService;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceImplTest {

  @Mock
  private ArticleDao articleDao;

  @Mock
  private ArticleDTOMapper articleDTOMapper;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @Mock
  private PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry;

  private ArticleServiceImpl articleService;

  @Before
  public void setUp() throws Exception{
    this.articleService= spy(
            new ArticleServiceImpl(articleDao,articleDTOMapper,applicationEventPublisher,associationRegistry)
    );
  }

  @Test
  public void test_create_article() {
    ArticleCreateForm articleCreateForm = new ArticleCreateForm();
    ArticleDTO articleSaved= new ArticleDTO();
    given(articleDao.create(any(Article.class))).willReturn(new Article());
    given(articleDTOMapper.mapEntity(any(Article.class))).willReturn(articleSaved);
    ArticleDTO result = this.articleService.create(articleCreateForm);
    Assert.assertEquals(articleSaved,result);
    verify(applicationEventPublisher,times(1)).publishEvent(any(CreatedEvent.class));
  }

  @Test
  public void test_update_article(){
    ArticleDTO articleToUpdate = new ArticleDTO();
    articleToUpdate.setId(123456789l);
    given(articleDao.update(anyLong(),any(Article.class))).willReturn(new Article());
    given(articleDTOMapper.mapEntity(any(Article.class))).willReturn(articleToUpdate);
    Assert.assertEquals(articleToUpdate,articleService.update(123456789l,new ArticleUpdateForm(articleToUpdate)));
    verify(applicationEventPublisher,times(1)).publishEvent(any(UpdatedEvent.class));
  }




}
