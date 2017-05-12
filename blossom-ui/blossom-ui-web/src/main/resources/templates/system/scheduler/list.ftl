<#import "/spring.ftl" as spring>

<div class="table-responsive">
  <table class="table table-hover table-striped">
    <tbody>
    <#list jobInfos as jobInfo>
    <tr>
      <td class="project-title">
        <a onclick="clickedJobInfos(this)" data-href="/blossom/system/scheduler/${jobInfo.key.group}/${jobInfo.key.name}">${jobInfo.key.name}</a>
        <br/>
        <small>${jobInfo.detail.description}</small>
      </td>
      <td class="project-status">
        <#if jobInfo.active && jobInfo.executing>
          <span class="label label-success"><span class="fa fa-spinner fa-spin"></span> In progress </span>
        <#elseif !jobInfo.executing>
          <span class="label label-primary">Active</span>
        <#else>
          <span class="label label-default">Idle</span>
        </#if>
      </td>

      <td class="project-lastExecution">
        <#if jobInfo.previousFireTime??>
          <i class="fa fa-thumbs-up text-success"></i> ${jobInfo.previousFireTime?datetime}
        <#else>
          <i class="fa fa-thumbs-down text-danger"></i> <@spring.message "scheduler.task.previousFireTime.never"/>
        </#if>
      </td>

      <td class="project-nextExecution">
        <#if jobInfo.nextFireTime??>
          <i class="fa fa-calendar text-success"></i> ${jobInfo.nextFireTime?datetime}
        <#else>
          <i class="fa fa-calendar-times-o text-danger"></i> <@spring.message "scheduler.task.nextFireTime.never"/>
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
