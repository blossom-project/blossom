<#import "/spring.ftl" as spring>

<#macro icon civility>
  <#if civility == 'MAN'>
  <i class="fa fa-male"></i>
  <#elseif civility == 'WOMAN'>
  <i class="fa fa-female"></i>
  <#else>
  <i class="fa fa-question-circle-o"></i>
  </#if>
</#macro>

<#macro label civility>
  <@spring.message "users.user.properties.civility."+civility/>
</#macro>
