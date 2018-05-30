<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2>
      <i class="fa fa-flask"></i>
      <@spring.message "menu.system.liquibase"/>
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <@spring.message "menu.system"/>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.liquibase"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <#list reports as name, report>
    <div class="ibox float-e-margins">
      <div class="ibox-title">
        <h5>${name}</h5>
      </div>
      <div class="ibox-content">
        <#assign displayedKeys = ["id", "author", "dateExecuted", "orderExecuted", "execType", "description"]/>
        <table class="table table-stripped">
          <thead>
            <tr>
              <th>id</th>
              <th>author</th>
              <th>dateExecuted</th>
              <th>orderExecuted</th>
              <th>execType</th>
              <th>description</th>
            </tr>
          </thead>
          <tbody>
            <#assign changeSetsReverse = report.changeSets?reverse>
            <#list changeSetsReverse as changeSet>
              <tr>
                <td>${changeSet["id"]!''}</td>
                <td>${changeSet["author"]!''}</td>
                <td>${changeSet["dateExecuted"]}</td>
                <td>${changeSet["orderExecuted"]?c}</td>
                <td>${changeSet["execType"]!''}</td>
                <td>${changeSet["description"]!''}</td>
              </tr>
            </#list>
          </tbody>
        </table>
      </div>
    </div>
  </#list>
</div>
</@master.default>
