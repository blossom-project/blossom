<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/tabulation.ftl" as tabulation>
<#import "/blossom/utils/privilege.ftl" as privilege>
<#import "/blossom/utils/buttons.ftl" as button>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-key"></i> ${article.name}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/content"><@spring.message "menu.content"/></a>
      </li>
      <li>
        <a href="/blossom/content/articles"><@spring.message "articles.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "articles.article.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="content:articles:delete">
       <@button.delete id=article.id?c uri='/blossom/content/articles/'+article.id?c+'/_delete'/>
      </@privilege.has>
    </div>
  </div>
</div>


<div class="wrapper wrapper-content">
  <@tabulation.tabs
    id="articleContent"
    currentUser=currentUser
    tabs=[
    {
    "isActive": true,
    "linkLabel": "panel.information",
    "view": "/blossom/content/articles/${article.id?c}/_informations",
    "edit": "/blossom/content/articles/${article.id?c}/_informations/_edit",
    "privilege":"content:articles:read"
    }
    ]
/>
</div>
</@master.default>
