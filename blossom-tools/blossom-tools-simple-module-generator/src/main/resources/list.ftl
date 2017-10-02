<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>
<#import "/utils/privilege.ftl" as privilege>
<#import "/utils/table.ftl" as table>

<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-question"></i> <@spring.message "%%ENTITY_NAME_PLURAL%%.title"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/modules"><@spring.message "menu.modules"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "%%ENTITY_NAME_PLURAL%%.title"/></strong>
      </li>
    </ol>
  </div>
  <div class="col-sm-4">
    <div class="title-action">
      <@privilege.has currentUser=currentUser privilege="%%PRIVILEGE_CREATE%%">
        <a href="%%LINK_CREATE%%" class="btn btn-primary"><i class="fa fa-plus"></i></a>
      </@privilege.has>
    </div>
  </div>
</div>

<div class="wrapper wrapper-content">

  <@table.pagetable
  page=items
  label='%%ENTITY_NAME_PLURAL%%.label'
  iconPath='%%ICON_PATH%%'
  columns= {
    %%ENTITY_COLUMNS%%
  }
  filters=[]
  searchable=true
  q=q
/>
</div>
</@master.default>
