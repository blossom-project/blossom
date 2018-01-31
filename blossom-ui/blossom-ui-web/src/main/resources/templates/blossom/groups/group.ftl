<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/tabulation.ftl" as tabulation>
<#import "/blossom/utils/privilege.ftl" as privilege>
<#import "/blossom/utils/buttons.ftl" as button>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-key"></i> ${group.name}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
      </li>
      <li>
        <a href="/blossom/administration/groups"><@spring.message "groups.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "groups.group.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="administration:groups:delete">
       <@button.delete id=group.id?c uri='/blossom/administration/groups/'+group.id?c+'/_delete'/>
      </@privilege.has>
    </div>
  </div>
</div>


<div class="wrapper wrapper-content">
  <@tabulation.tabs
    id="groupContent"
    currentUser=currentUser
    tabs=[
    {
    "isActive": true,
    "linkLabel": "panel.information",
    "view": "/blossom/administration/groups/${group.id?c}/_informations",
    "edit": "/blossom/administration/groups/${group.id?c}/_informations/_edit",
    "privilege":"administration:groups:read"
    },
    {
    "isActive": false,
    "linkLabel": "groups.panel.users",
    "view": "/blossom/administration/memberships/groups/${group.id?c}/users",
    "edit": "/blossom/administration/memberships/groups/${group.id?c}/users/_edit",
    "privilege":"administration:memberships:read"
    }
    ]
/>
</div>
</@master.default>
