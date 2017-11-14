<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


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
        <a href="/blossom/system"><@spring.message "menu.system"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.liquibase"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <#list reports as report>
    <div class="ibox float-e-margins">
      <div class="ibox-title">
        <h5>${report.name}</h5>
      </div>
      <div class="ibox-content">
        <#assign displayedKeys = ["ID", "AUTHOR", "DATEEXECUTED", "ORDEREXECUTED", "EXECTYPE", "DESCRIPTION"]/>
        <table class="table table-stripped">
          <thead>
            <tr>
              <#list displayedKeys as key>
                <th>${key}</th>
              </#list>
            </tr>
          </thead>
          <tbody>
            <#list report.changeLogs as changeLog>
              <tr>
                <#list displayedKeys as key>
                  <td>${changeLog[key]!''}</td>
                </#list>
              </tr>
            </#list>
          </tbody>
        </table>
      </div>
    </div>
  </#list>
</div>
</@master.default>
