<div id="status" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5>Server status</h5>
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
      </tr>
      </#list>
      </tbody>
    </table>
  </div>
</div>
