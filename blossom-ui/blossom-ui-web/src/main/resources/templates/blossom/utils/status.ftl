<#import "/spring.ftl" as spring>

<#macro label status>
    <@spring.message "articles.article.properties.status."+status/>
</#macro>