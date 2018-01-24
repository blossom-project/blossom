<#import "/spring.ftl" as spring>

<div class="table-responsive">
  <table class="table table-hover table-striped">
    <tbody>
    <#list jobInfos as jobInfo>
    <tr>
      <td class="project-title">
        <a onclick="clickedJobInfos(this)" data-href="/blossom/system/scheduler/${jobInfo.key.group}/${jobInfo.key.name}">
          <@spring.messageText "scheduler.job.name."+jobInfo.key.name jobInfo.key.name/>
        </a>
        <br/>
        <small>
          <@spring.messageText "scheduler.job.description."+jobInfo.detail.description jobInfo.detail.description/>
        </small>
      </td>
      <td class="project-status">
        <#if scheduler.standBy>
          <span class="label label-warning-light">
            <@spring.message "scheduler.job.state.standby"/>
          </span>
        <#else>
          <#if jobInfo.active && jobInfo.executing>
            <span class="label label-success">
              <span class="fa fa-spinner fa-spin"></span>
              &nbsp;<@spring.message "scheduler.job.state.in_progress"/>
            </span>
          <#elseif jobInfo.active && !jobInfo.executing>
            <span class="label label-primary">
              <@spring.message "scheduler.job.state.active"/>
            </span>
          <#else>
            <span class="label label-default">
              <@spring.message "scheduler.job.state.idle"/>
            </span>
          </#if>
        </#if>
      </td>

      <td class="project-lastExecution">
        <#if jobInfo.lastExecutedTrigger??>
          <i class="fa fa-thumbs-up text-success"></i> ${jobInfo.lastExecutedTrigger.startTime?datetime}
        <#else>
          <#if jobInfo.previousFireTime??>
            <i class="fa fa-thumbs-up text-success"></i> ${jobInfo.previousFireTime?datetime}
          <#else>
            <i class="fa fa-thumbs-down text-danger"></i> <@spring.message "never"/>
          </#if>
        </#if>
      </td>

      <td class="project-nextExecution">
        <#if jobInfo.nextFireTime??>
          <i class="fa fa-calendar text-success"></i> ${jobInfo.nextFireTime?datetime}
        <#else>
          <i class="fa fa-calendar-times-o text-danger"></i> <@spring.message "never"/>
        </#if>
      </td>
    </tr>
    </#list>
    </tbody>
  </table>
</div>
<script>
  var clickedJobInfos = function (e) {
    $(document).trigger("scheduledTaskSelected", $(e).data("href"));
  }
</script>
