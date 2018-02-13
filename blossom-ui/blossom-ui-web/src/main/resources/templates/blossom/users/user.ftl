<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/tabulation.ftl" as tabulation>
<#import "/blossom/utils/privilege.ftl" as privilege>
<#import "/blossom/utils/buttons.ftl" as button>


<@master.default currentUser=currentUser>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> ${user.firstname +' '+ user.lastname}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <@spring.message "menu.administration"/>
      </li>
      <li>
        <a href="/blossom/administration/users"><@spring.message "users.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "users.user.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="administration:users:delete">
       <@button.delete id=user.id?c uri='/blossom/administration/users/'+user.id?c+'/_delete'/>
      </@privilege.has>
    </div>
  </div>
</div>

<div class="wrapper wrapper-content">
<@tabulation.tabs
    id="userContent"
    currentUser=currentUser
    tabs=[
        {
          "isActive": true,
          "linkLabel": "panel.information",
          "view": "/blossom/administration/users/${user.id?c}/_informations",
          "edit": "/blossom/administration/users/${user.id?c}/_informations/_edit",
          "privilege":"administration:users:read"
        },
        {
          "isActive": false,
          "linkLabel": "users.user.groups",
          "view": "/blossom/administration/memberships/users/${user.id?c}/groups",
          "edit": "/blossom/administration/memberships/users/${user.id?c}/groups/_edit",
          "privilege":"administration:memberships:read"
        },
        {
          "isActive": false,
          "linkLabel": "users.user.roles",
          "view": "/blossom/administration/responsabilities/users/${user.id?c}/roles",
          "edit": "/blossom/administration/responsabilities/users/${user.id?c}/roles/_edit",
          "privilege":"administration:responsabilities:read"
        }
    ]

/>
</div>
</@master.default>
