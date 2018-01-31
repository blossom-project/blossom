<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>


<h4>${logger}</h4>

<#if !(loggerLevels.configuredLevel??)>
  <div class="alert alert-warning"><@spring.message "logger.not.configured"/></div>
</#if>

<div class="">
  <#list levels as level>
    <#assign levelClass = "btn-default"/>

    <#if level == 'OFF' && loggerLevels.effectiveLevel==level>  <#assign levelClass = "bg-black active"/>
      <#elseif level == 'ERROR' && loggerLevels.effectiveLevel==level>  <#assign levelClass = "btn-danger active"/>
      <#elseif level == 'WARN' && loggerLevels.effectiveLevel==level>  <#assign levelClass = "btn-warning active"/>
      <#elseif level == 'INFO' && loggerLevels.effectiveLevel==level>  <#assign levelClass = "btn-info active"/>
      <#elseif level == 'DEBUG' && loggerLevels.effectiveLevel==level>  <#assign levelClass = "btn-success active"/>
      <#elseif level == 'TRACE' && loggerLevels.effectiveLevel==level>  <#assign levelClass = "btn-primary active"/>
    </#if>

    <button class="btn btn-rounded btn-xs ${levelClass}" onclick="changeLogLevel('${logger}', '${level}')">${level}</button>
  </#list>
</div>
