<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2>
      <i class="fa fa-sitemap"></i>
      <@spring.message "menu.system.bpmn"/>
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/system"><@spring.message "menu.system"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.bpmn"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <div class="ibox float-e-margins">
    <div class="ibox-content">
      <table class="table table-stripped">
        <thead>
        <tr>
          <th>Name</th>
          <th>Key</th>
          <th>Category</th>
          <th>Version</th>
          <th>Instances</th>
        </tr>
        </thead>
        <tbody>
          <#if processDefinitions?size == 0>
            <tr>
              <td colspan="5">
                <div class="alert alert-default text-center"><@spring.message "list.no.element.found.label" /></div>
              </td>
            </tr>
          <#else>
            <#list processDefinitions as processDefinition>
            <tr>
              <td>
              ${processDefinition.name}
              </td>
              <td>
              ${processDefinition.key!''}
              </td>
              <td>
              ${processDefinition.category!''}
              </td>
              <td>
              ${processDefinition.version!''}
              </td>
              <td>
                <#if instances[processDefinition.id]??>
                  ${instances[processDefinition.id]?c}
                <#else>
                  <@spring.message "none"/>
                </#if>
              </td>
            </tr>
            </#list>
          </#if>
        </tbody>
      </table>
    </div>
  </div>
</div>

</@master.default>
