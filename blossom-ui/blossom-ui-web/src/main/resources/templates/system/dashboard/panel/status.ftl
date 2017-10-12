<div id="status" class="ibox float-e-margins">
    <div class="ibox-title">
        <h5>Server status</h5>
        <div class="ibox-tools">
            <span class="label label-primary pull-right"></span>
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

        <h2 class="global <#if health.getStatus().getCode() == 'UP'>text-navy<#else>text-danger</#if>">
            <i class="fa fa-play <#if health.getStatus().getCode() == 'UP'>fa-rotate-270<#else>fa-rotate-90</#if>"></i>
        ${health.getStatus().getCode()}
        </h2>

        <table class="table table-stripped small m-t-md">
            <tbody>
            <#list health.getDetails()?keys as key>
            <tr>
                <td class="<#if health.getDetails()[key].getStatus().getCode() == 'UP'>text-navy<#else>text-danger</#if>">
                    <i
                            class="fa fa-play <#if health.getDetails()[key].getStatus().getCode() == 'UP'>fa-rotate-270<#else>fa-rotate-90</#if>"></i>
                ${health.getDetails()[key].getStatus().getCode()}
                </td>
                <td>
                ${key}
                </td>

                <td>
                  <#assign details = health.getDetails()[key].getDetails()/>
                  <#if details??>
                    <i class="fa fa-question-circle text-navy"
                       data-toggle="popover"
                       data-trigger="hover"
                       data-html="true"
                       data-container="body"
                       data-placement="auto"
                       data-content="<ul class='unstyled'><#list details?keys as detailKey><li><div><strong>${detailKey} :</strong> ${details[detailKey]}</li></#list></div></ul>">
                    </i>
                  </#if>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>

<script>
    $(document).ready(function(){
      $("#status .ibox-tools .label").text(moment.duration(${uptime?c}).humanize());
      $("#status [data-toggle='popover']").popover();
    });
</script>
