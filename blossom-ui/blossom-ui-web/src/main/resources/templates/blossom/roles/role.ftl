<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/tabulation.ftl" as tabulation>
<#import "/blossom/utils/privilege.ftl" as privilege>
<#import "/blossom/utils/buttons.ftl" as button>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-key"></i> ${role.name}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <@spring.message "menu.administration"/>
      </li>
      <li>
        <a href="/blossom/administration/roles"><@spring.message "roles.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "roles.role.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="administration:roles:delete">
       <@button.delete id=role.id?c uri='/blossom/administration/roles/'+role.id?c+'/_delete'/>
      </@privilege.has>
    </div>
  </div>
</div>


<div class="wrapper wrapper-content">
  <@tabulation.tabs
    id="roleContent"
    currentUser=currentUser
    tabs=[
  {
    "isActive": true,
    "linkLabel": "panel.information",
    "view": "/blossom/administration/roles/${role.id?c}/_informations",
    "edit": "/blossom/administration/roles/${role.id?c}/_informations/_edit",
    "privilege":"administration:roles:read"
  },
    {
    "isActive": false,
    "linkLabel": "roles.panel.privileges",
    "view": "/blossom/administration/roles/${role.id?c}/_privileges",
    "edit": "/blossom/administration/roles/${role.id?c}/_privileges/_edit",
    "privilege":"administration:roles:read"
    },
    {
    "isActive": false,
    "linkLabel": "roles.panel.users",
    "view": "/blossom/administration/responsabilities/roles/${role.id?c}/users",
    "edit": "/blossom/administration/responsabilities/roles/${role.id?c}/users/_edit",
    "privilege":"administration:responsabilities:read"
    }
  ]
/>
</div>
</@master.default>
