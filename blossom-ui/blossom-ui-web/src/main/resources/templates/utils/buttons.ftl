<#import "/spring.ftl" as spring>

<#macro switch checked name="" disabled=false>
  <#assign id= 'button_'+.now?long?c />
<div class="switch">
    <div class="onoffswitch">
      <input name="${name}" type="checkbox" <#if checked>checked</#if> <#if disabled>disabled</#if> class="onoffswitch-checkbox" id="${id}"/>
      <label class="onoffswitch-label" for="${id}">
        <span class="onoffswitch-inner"></span>
        <span class="onoffswitch-switch"></span>
      </label>
    </div>
  </div>
</#macro>
