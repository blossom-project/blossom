<#macro has currentUser privilege>
  <#if hasOne(currentUser, privilege)>
    <#nested/>
  </#if>
</#macro>

<#function hasOne currentUser privilege>
  <#if currentUser.hasPrivilege(privilege)>
    <#return true/>
  </#if>
  <#return false/>
</#function>
