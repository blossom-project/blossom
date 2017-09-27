<#macro has currentUser privilege="" >
  <#if !(privilege?has_content) || currentUser.hasPrivilege(privilege)>
    <#nested/>
  </#if>
</#macro>
