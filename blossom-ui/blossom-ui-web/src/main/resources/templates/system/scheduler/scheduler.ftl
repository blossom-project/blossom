<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
    <h2>Tâches planifiées</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom">Accueil</a>
      </li>
      <li class="active">
        <strong>Tâches planifiées</strong>
      </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
  <div class="wrapper wrapper-content animated fadeInUp">

    <div class="col-lg-12">
      <div class="tabs-container">
        <div class="tabs-left">
          <ul class="nav nav-tabs">
            <#list jobInfos?keys as groupName>
              <li <#if groupName?is_first>class="active"</#if>><a data-toggle="tab" href="#${groupName}">${groupName}</a></li>
            </#list>
          </ul>
          <div class="tab-content">
            <#list jobInfos?keys as groupName>
              <div id="${groupName}" class="tab-pane <#if groupName?is_first>active</#if>">
                <div class="panel-body">
                      <div class="project-list">
                        <table class="table table-hover">
                          <tbody>
                          <#list jobInfos[groupName] as jobInfo>
                            <tr>
                              <td class="project-title">
                                <a href="/blossom/system/scheduler/${jobInfo.key.group}/${jobInfo.key.name}">${jobInfo.key.name}</a>
                                <br>
                                <small>${jobInfo.detail.description}</small>
                              </td>
                              <td class="project-status">
                                  <#if jobInfo.jobExecutionContexts?size gt 0>
                                      <span class="label label-primary">Active</span>
                                  <#else>
                                      <span class="label label-default">Idle</span>
                                  </#if>
                              </td>
                              <td class="project-triggers">
                                <span>${jobInfo.triggers?size} triggers</span>
                              </td>
                              <td class="project-lastExecution">
                                <#if jobInfo.lastExecutionTime??>
                                    <i class="fa fa-calendar"></i> ${jobInfo.lastExecutionTime?datetime}
                                <#else>
                                    <i class="fa fa-calendar"></i> Never
                                </#if>
                              </td>
                            </tr>
                          </#list>
                          </tbody>
                        </table>
                      </div>
                </div>
              </div>
            </#list>
          </div>

        </div>

      </div>
    </div>

  </div>
</div>
</@master.default>
