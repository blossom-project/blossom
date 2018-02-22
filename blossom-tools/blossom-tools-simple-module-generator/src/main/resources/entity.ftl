<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/tabulation.ftl" as tabulation>
<#import "/blossom/utils/privilege.ftl" as privilege>
<#import "/blossom/utils/buttons.ftl" as button>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-key"></i> ${%%ENTITY_NAME%%.name}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <@spring.message "menu.modules"/>
      </li>
      <li>
        <a href="/blossom/modules/%%ENTITY_NAME_PLURAL%%"><@spring.message "%%ENTITY_NAME_PLURAL%%.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "%%ENTITY_NAME_PLURAL%%.%%ENTITY_NAME%%.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="modules:%%ENTITY_NAME_PLURAL%%:delete">
       <@button.delete id=%%ENTITY_NAME%%.id?c uri='/blossom/modules/%%ENTITY_NAME_PLURAL%%/'+%%ENTITY_NAME%%.id?c+'/_delete'/>
      </@privilege.has>
    </div>
  </div>
</div>


<div class="wrapper wrapper-content">
  <@tabulation.tabs
    id="%%ENTITY_NAME%%Content"
    currentUser=currentUser
    tabs=[
    {
    "isActive": true,
    "linkLabel": "panel.information",
    "view": "/blossom/modules/%%ENTITY_NAME_PLURAL%%/${%%ENTITY_NAME%%.id?c}/_informations",
    "edit": "/blossom/modules/%%ENTITY_NAME_PLURAL%%/${%%ENTITY_NAME%%.id?c}/_informations/_edit",
    "privilege":"modules:%%ENTITY_NAME_PLURAL%%:read"
    }
    ]
/>
</div>
</@master.default>
