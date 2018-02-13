<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/table.ftl" as table>
<#import "/blossom/utils/privilege.ftl" as privilege>

<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> <@spring.message "users.title"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <@spring.message "menu.administration"/>
      </li>
      <li class="active">
        <strong><@spring.message "users.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="administration:users:create">
        <a href="/blossom/administration/users/_create" class="btn btn-primary"><i class="fa fa-plus"></i></a>
      </@privilege.has>
    </div>
  </div>
</div>

<div class="wrapper wrapper-content">
  <#include "table.ftl">
</div>
</@master.default>
