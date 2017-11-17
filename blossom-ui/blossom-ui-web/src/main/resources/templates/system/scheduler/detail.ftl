<#import "/spring.ftl" as spring>

<div class="ibox">
  <div class="ibox-title">
    <h5>
    <#if scheduler.standBy>
      <span class="label label-warning-light">Stand-by</span>
    <#else>
      <#if jobInfo.active && jobInfo.executing>
        <span class="label label-success"><span class="fa fa-spinner fa-spin"></span> In progress </span>
      <#elseif !jobInfo.executing>
        <span class="label label-primary">Active</span>
      <#else>
        <span class="label label-default">Idle</span>
      </#if>
    </#if>
      <span class="m-l-sm"><b>${jobInfo.key.group} - ${jobInfo.key.name}</b></span>
    </h5>
    <div class="ibox-tools">
      <a class="close-link" onclick="$(document).trigger('scheduledTaskDeselected')">
        <i class="fa fa-times"></i>
      </a>
    </div>
  </div>

  <div class="ibox-content">
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>

    <div class="row">
      <div class="col-lg-12 small text-muted m-b-lg">
        <span class="m-r-sm">Durable <i class="fa fa-check-circle <#if jobInfo.detail.durable >text-success</#if>"></i></span>
        <span class="m-r-sm">Persist data <i
          class="fa fa-check-circle <#if jobInfo.detail.persistJobDataAfterExecution >text-success</#if>"></i></span>
        <span class="m-r-sm">Concurrent execution <i
          class="fa fa-check-circle <#if !jobInfo.detail.concurrentExectionDisallowed >text-success</#if>"></i></span>
        <span class="m-r-sm">Recovery <i
          class="fa fa-check-circle <#if jobInfo.detail.requestsRecovery() >text-success</#if>"></i></span>
      </div>
    </div>

    <strong class="m-t-lg">Description</strong>

    <div class="row">
      <div class="col-lg-12">
      ${jobInfo.detail.description}
      </div>
    </div>
    <br/>
    <strong class="m-t-lg"><@spring.message "scheduler.triggers.title"/></strong>

    <div class="row">
      <div class="col-lg-12">
        <table class="table table-striped">
          <thead>
          <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Next fire</th>
          </tr>
          <tbody>
          <#list jobInfo.triggers as trigger>
            <tr>
              <td>${trigger.name}</td>
              <td><i class="${triggerIcon(trigger)}"></i> ${triggerType(trigger)}</td>
              <td>${trigger.nextFireTime?datetime!''}</td>
            </tr>
          </#list>
          <tr>
            <td><@spring.message "action.manual"/></td>
            <td><i class="fa fa-play"></i> <@spring.message "action.manual"/></td>
            <td>
              <button class="btn btn-danger btn-xs btn-block"
                      data-href="/blossom/system/scheduler/${jobInfo.key.group}/${jobInfo.key.name}/_execute"
                      onclick="$(document).trigger('scheduledTaskExecution',$(this).data('href'));">
                <@spring.message "now"/>
              </button>
            </td>
          </tr>
          </tbody>

          </thead>
        </table>
      </div>
    </div>
  </div>
</div>

<#function triggerIcon trigger>
  <#if isCron(trigger)>
    <#return 'fa fa-clock-o'/>
  <#elseif isSimple(trigger)>
    <#return 'fa fa-calendar'/>
  <#else>
    <#return 'fa fa-question-circle-o'/>
  </#if>
</#function>

<#function triggerType trigger>
  <#if isCron(trigger)>
    <#return 'cron'/>
  <#elseif isSimple(trigger)>
    <#return 'simple'/>
  <#else>
    <#return 'unknown'/>
  </#if>
</#function>

<#function isCron trigger>
  <#return trigger.class?contains("CronTrigger")>
</#function>

<#function isSimple trigger>
  <#return trigger.class?contains("SimpleTrigger")>
</#function>
