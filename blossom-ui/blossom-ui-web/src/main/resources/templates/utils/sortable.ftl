<#macro icon name order="">
	<#assign ASC="ASC"/>
	<#assign DESC="DESC"/>
	<#assign NONE=""/>
	
	<#if order == ASC>
		<i class="fa fa-sort-up clickable" onclick="window.location.href = $.updateQueryStringParameter(window.location.href,'sort','${name},${DESC}')"></i>
	<#elseif order == DESC>
		<i class="fa fa-sort-down clickable" onclick="window.location.href = $.updateQueryStringParameter(window.location.href,'sort','')"></i>
	<#else>
		<i class="fa fa-sort clickable" onclick="window.location.href = $.updateQueryStringParameter(window.location.href,'sort','${name},${ASC}')"></i>
	</#if>
</#macro>
