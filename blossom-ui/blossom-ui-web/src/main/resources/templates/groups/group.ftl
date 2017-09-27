<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/tabulation.ftl" as tabulation>


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
</div>


<div class="wrapper wrapper-content">
  <@tabulation.tabs
    id="groupContent"
  tabs=[
  {
  "isActive": true,
  "linkLabel": "panel.information",
  "view": "/blossom/administration/groups/${group.id?c}/_informations",
  "edit": "/blossom/administration/groups/${group.id?c}/_informations/_edit"
  },
  {
  "isActive": false,
  "linkLabel": "groups.panel.users",
  "view": "/blossom/administration/memberships/groups/${group.id?c}/users",
  "edit": "/blossom/administration/memberships/groups/${group.id?c}/users/_edit"
  }
  ]
/>
</div>
</@master.default>
