<#import "/spring.ftl" as spring>

<#macro switch checked disabled>
  <div class="switch">
    <div class="onoffswitch">
      <input type="checkbox" <#if checked>checked</#if> <#if disabled>disabled</#if> class="onoffswitch-checkbox" />
      <label class="onoffswitch-label">
        <span class="onoffswitch-inner"></span>
        <span class="onoffswitch-switch"></span>
      </label>
    </div>
  </div>
</#macro>
