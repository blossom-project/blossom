<#macro default icon classes=[]>
	<i class="${icon} <#list classes as class>${class}<#sep> </#sep></#list>"></i>
</#macro>

<#macro x3 icon>
	<#if icon?contains("fa ")>
		<i class="${icon} fa-3x"></i>
	<#elseif icon?contains("icon ")>
		<i class="${icon} large-icons"></i>
	</#if>	
</#macro>