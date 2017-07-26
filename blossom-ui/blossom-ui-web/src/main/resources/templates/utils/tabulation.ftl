<#macro tabulation tabnavlist idContent idPanelContent>
 <div class="row">
    <div class="col-sm-12">
      <div class="tabs-container">
        <ul class="nav nav-tabs">
            <#list tabnavlist as nav>
                <#if nav.isActive>
                <li class="active">
                <#else>
                <li>
                </#if>
                <a href="${nav.href}" onclick="${nav.onClick}" data-toggle="tab">
                       <@spring.message "${nav.linkLabel}"/> 
                    </a>
                </li>
            </#list>
        </ul>
        <div class="tab-content">
            <div id="${idContent}" class="tab-pane active">
                <div class="panel-body" id="${idPanelContent}">
                </div>
            </div>
        </div>
      </div>
    </div>
  </div>
</#macro>
