<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-8">
        <h2><i class="fa fa-key"></i> <@spring.message "roles.title"/></h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
            <li>
                <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
            </li>
            <li class="active">
                <strong><@spring.message "roles.title"/></strong>
            </li>
        </ol>
    </div>
    <div class="col-sm-4">
    <div class="title-action">
      <a href="/blossom/administration/roles/_create" class="btn btn-primary"><i class="fa fa-plus"></i></a>
    </div>
  </div>
</div>

<div class="wrapper wrapper-content">
    <#include "./table.ftl">
</div>
</@master.default>
