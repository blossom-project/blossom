<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/tabulation.ftl" as tabulation>


<@master.default currentUser=currentUser>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> ${user.firstname +' '+ user.lastname}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
      </li>
      <li>
        <a href="/blossom/administration/users"><@spring.message "users.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "users.user.title"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
<@tabulation.tabs
    id="userContent"
    tabs=[
        {
          "isActive": true,
          "linkLabel": "panel.information",
          "view": "/blossom/administration/users/${user.id?c}/_informations",
          "edit": "/blossom/administration/users/${user.id?c}/_informations/_edit"
        },
        {
          "isActive": false,
          "linkLabel": "users.user.groups",
          "view": "/blossom/administration/memberships/users/${user.id?c}/groups",
          "edit": "/blossom/administration/memberships/users/${user.id?c}/groups/_edit"
        },
        {
          "isActive": false,
          "linkLabel": "users.user.roles",
          "view": "/blossom/administration/responsabilities/users/${user.id?c}/roles",
          "edit": "/blossom/administration/responsabilities/users/${user.id?c}/roles/_edit"
        }
    ]

/>
</div>
</@master.default>
