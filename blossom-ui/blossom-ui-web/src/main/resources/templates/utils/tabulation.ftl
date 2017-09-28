<#import "/utils/privilege.ftl" as privilege>

<#macro tabs id tabs currentUser>
<div class="row">
  <div class="col-sm-12">
    <div id="${id}" class="tabs-container">
      <ul class="nav nav-tabs">
        <#list tabs as nav>
          <#if !(nav.privilege?has_content) || privilege.hasOne(currentUser, nav.privilege)>
            <li>
              <a data-target="#${id}_tab_${nav?index}" data-toggle="tabajax">
                <@spring.message "${nav.linkLabel}"/>
              </a>
            </li>
          </#if>
        </#list>
      </ul>

      <div class="tab-content">
        <#list tabs as nav>
          <#if !(nav.privilege?has_content) || privilege.hasOne(currentUser,nav.privilege)>
            <#assign tabId = id+'_tab_'+nav?index />
            <div id="${tabId}" class="tab-pane" data-view="${nav.view}" <#if nav.edit??>data-edit="${nav.edit}"</#if>>
              <div class="ibox-content sk-loading">
                <div class="sk-spinner sk-spinner-wave">
                  <div class="sk-rect1"></div>
                  <div class="sk-rect2"></div>
                  <div class="sk-rect3"></div>
                  <div class="sk-rect4"></div>
                  <div class="sk-rect5"></div>
                </div>
              </div>
            </div>
          </#if>
        </#list>
      </div>
    </div>
  </div>
</div>


<script>

  var load_${id} = function(targetSelector){
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");

    var view = $(targetSelector).data("view");

    $.get(view).done(function (responseText, textStatus, jqXHR) {
      if (jqXHR.status === 200) {
        $(targetSelector).html(responseText);
      }
      $(targetSelector).removeClass("sk-loading");
    });
  };

  $(document).ready(function () {
    $('#${id}').on('click', '[data-toggle="tabajax"]', function (e) {
      var $this = $(this);
      var targetSelector = $this.data('target');
      if(!$this.parent().hasClass("active")){
        $this.tab('show');
        load_${id}(targetSelector);
      }
      return false;
    });

    $('#${id}').find('[data-toggle="tabajax"]').first().click();
  });
</script>
</#macro>
