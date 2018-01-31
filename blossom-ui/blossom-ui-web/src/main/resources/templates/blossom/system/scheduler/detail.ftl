<#import "/spring.ftl" as spring>

<div class="ibox">
  <div class="ibox-title">
    <h5>
    <#if scheduler.standBy>
      <span class="label label-warning-light"><@spring.message "scheduler.job.state.standby"/></span>
    <#else>
      <#if jobInfo.active && jobInfo.executing>
        <span class="label label-success"><span class="fa fa-spinner fa-spin"></span>&nbsp;<@spring.message "scheduler.job.state.in_progress"/></span>
      <#elseif !jobInfo.executing>
        <span class="label label-primary"><@spring.message "scheduler.job.state.active"/></span>
      <#else>
        <span class="label label-default"><@spring.message "scheduler.job.state.idle"/></span>
      </#if>
    </#if>
      <span class="m-l-sm">
        <b>
          <@spring.messageText "scheduler.group."+jobInfo.key.group jobInfo.key.group />&nbsp;-&nbsp;<@spring.messageText "scheduler.job.name."+jobInfo.key.name jobInfo.key.name/>
        </b>
      </span>
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
        <span class="m-r-sm">
          <@spring.message "scheduler.job.properties.durable"/>
          <i class="fa fa-check-circle <#if jobInfo.detail.durable >text-success</#if>"></i>
        </span>
        <span class="m-r-sm">
          <@spring.message "scheduler.job.properties.persist_data"/>
          <i class="fa fa-check-circle <#if jobInfo.detail.persistJobDataAfterExecution >text-success</#if>"></i>
        </span>
        <span class="m-r-sm">
          <@spring.message "scheduler.job.properties.concurrent_execution"/>
          <i class="fa fa-check-circle <#if !jobInfo.detail.concurrentExectionDisallowed >text-success</#if>"></i>
        </span>
        <span class="m-r-sm">
          <@spring.message "scheduler.job.properties.recovery"/>
          <i class="fa fa-check-circle <#if jobInfo.detail.requestsRecovery() >text-success</#if>"></i>
        </span>
      </div>
    </div>

    <strong class="m-t-lg"><@spring.message "scheduler.job.description"/></strong>

    <div class="row">
      <div class="col-lg-12">
        <@spring.messageText "scheduler.job.description."+jobInfo.detail.description jobInfo.detail.description/>
      </div>
    </div>
    <br/>
    <strong class="m-t-lg"><@spring.message "scheduler.triggers.title"/></strong>

    <div class="row">
      <div class="col-lg-12">
        <table class="table table-striped">
          <thead>
            <tr>
              <th><@spring.message "scheduler.triggers.name"/></th>
              <th><@spring.message "scheduler.triggers.type"/></th>
              <th><@spring.message "scheduler.triggers.next_fire"/></th>
            </tr>
          </thead>
          <tbody>
          <#list jobInfo.triggers as trigger>
            <tr>
              <td><@spring.messageText "scheduler.trigger.name."+trigger.name trigger.name/></td>
              <td><i class="${triggerIcon(trigger)}"></i> ${triggerType(trigger)}</td>
              <td><#if trigger.nextFireTime??>${trigger.nextFireTime?datetime}</#if></td>
            </tr>
          </#list>
          <tr>
            <td><@spring.message "action.manual"/></td>
            <td><i class="fa fa-play"></i> <@spring.message "action.manual"/></td>
            <td>
              <button class="btn btn-danger btn-xs btn-block"
                      data-refresh-href="/blossom/system/scheduler/${jobInfo.key.group}/${jobInfo.key.name}"
                      data-execute-now-href="/blossom/system/scheduler/${jobInfo.key.group}/${jobInfo.key.name}/_execute"
                      onclick="$(document).trigger('scheduledTaskExecution', [$(this).data('execute-now-href'), $(this).data('refresh-href')]);">
                <@spring.message "now"/>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <br/>
    <strong class="m-t-lg"><@spring.message "scheduler.history.title"/></strong>

    <div class="row">
      <div class="col-lg-12">
        <table class="table table-striped">
          <thead>
          <tr>
            <th><@spring.message "scheduler.trigger.title"/></th>
            <th><@spring.message "scheduler.history.start_time"/></th>
            <th><@spring.message "scheduler.history.end_time"/></th>
          </tr>
          </thead>
          <tbody>
            <#list jobInfo.history as history>
              <tr>
                <td><@spring.messageText "scheduler.trigger.name."+history.triggerKey.name history.triggerKey.name/></td>
                <td><#if history.startTime??>${history.startTime?datetime}</#if></td>
                <td><#if history.endTime??>${history.endTime?datetime}</#if></td>
              </tr>
            </#list>
          </tbody>
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
