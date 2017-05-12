<#import "/spring.ftl" as spring>

<div class="ibox">
  <div class="ibox-title">
    <h5>
    <#if jobInfo.active && jobInfo.executing>
      <span class="label label-success"><span class="fa fa-spinner fa-spin"></span> In progress </span>
    <#elseif !jobInfo.executing>
      <span class="label label-primary">Active</span>
    <#else>
      <span class="label label-default">Idle</span>
    </#if>
      <span class="m-l-sm">${jobInfo.key.group} - ${jobInfo.key.name}</span>
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
        <span class="m-r-sm">Persist data <i class="fa fa-check-circle <#if jobInfo.detail.persistJobDataAfterExecution >text-success</#if>"></i></span>
        <span class="m-r-sm">Concurrent execution <i class="fa fa-check-circle <#if !jobInfo.detail.concurrentExectionDisallowed >text-success</#if>"></i></span>
        <span class="m-r-sm">Recovery <i class="fa fa-check-circle <#if jobInfo.detail.requestsRecovery() >text-success</#if>"></i></span>
      </div>
    </div>

    <strong class="m-t-lg">Description</strong>

    <div class="row">
      <div class="col-lg-12">
      ${jobInfo.detail.description}
      </div>
    </div>

    <#--<strong class="m-t-lg"><@spring.message "scheduler.triggers.title"/></strong>-->

    <#--<div class="row">-->
    <#--<#list jobInfo.triggers as trigger>-->
      <#--<div class="col-lg-12 b-r-xs">-->
        <#--<h4><strong>${trigger.name}</strong></h4>-->
        <#--<p><i class="${triggerIcon(trigger)}"></i> ${triggerType(trigger)}</p>-->
        <#--<dl>-->
          <#--<dt>test</dt>-->
          <#--<dd>test</dd>-->
        <#--</dl>-->
      <#--</div>-->
    <#--</#list>-->
    <#--</div>-->
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
  <#return "Trigger"/>
</#function>

<#function isCron trigger>
  <#return trigger.class?contains("CronTrigger")>
</#function>

<#function isSimple trigger>
  <#return trigger.class?contains("SimpleTrigger")>
</#function>
