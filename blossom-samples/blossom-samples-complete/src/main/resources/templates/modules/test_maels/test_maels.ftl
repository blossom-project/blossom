<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>
<#import "/utils/privilege.ftl" as privilege>
<#import "/utils/table.ftl" as table>

<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-question"></i> <@spring.message "test_maels.title"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/modules"><@spring.message "menu.modules"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "test_maels.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="modules:test_maels:create">
        <a href="/blossom/modules/test_maels/_create" class="btn btn-primary"><i class="fa fa-plus"></i></a>
      </@privilege.has>
    </div>
  </div>
</div>

<div class="wrapper wrapper-content">

  <@table.pagetable
  page=items
  label='test_maels.label'
  iconPath='fa fa-question'
  columns= {
    "name": { "label":"test_maels.test_mael.properties.name", "sortable":true, "link":"/blossom/modules/test_maels/{id}"},
  "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
  filters=[]
  searchable=true
  q=q
/>
</div>
</@master.default>
