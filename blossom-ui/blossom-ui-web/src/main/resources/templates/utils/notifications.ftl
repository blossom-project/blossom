<#import "/spring.ftl" as spring>

<#macro success title="success" message="">
  <@toaster title=title message=message level="success"/>
</#macro>

<#macro error title="error" message="">
  <@toaster title=title message=message level="error"/>
</#macro>

<#macro warning title="warning" message="">
  <@toaster title=title message=message level="warning"/>
</#macro>

<#macro info title="info" message="">
  <@toaster title=title message=message level="info"/>
</#macro>

<#macro toaster title message level>
  toastr.${level}("<@spring.messageText message message/>", "<@spring.messageText title title/>");
</#macro>
