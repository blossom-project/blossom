<#import "/spring.ftl" as spring>
<#import "/blossom/utils/civility.ftl" as civility>
<#import "/blossom/utils/buttons.ftl" as buttons>
<#import "/blossom/utils/tabulation.ftl" as tabulation>

<div class="ibox-content">
  <div class="sk-spinner sk-spinner-wave">
    <div class="sk-rect1"></div>
    <div class="sk-rect2"></div>
    <div class="sk-rect3"></div>
    <div class="sk-rect4"></div>
    <div class="sk-rect5"></div>
  </div>
  <form class="form form-horizontal">
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "groups.group.properties.name"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${group.name!''}</p>
      </div>
    </div>

    <div class="form-group">
      <label
        class="col-sm-2 control-label"><@spring.message "groups.group.properties.description"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${group.description!''}</p>
      </div>
    </div>
  </form>
</div>

<div class="ibox-footer">
  <div class="text-right">
    <button class="btn btn-primary btn-view" onclick="edit_groupinformations(this);">
    <@spring.message "edit"/>
    </button>
  </div>
</div>

<script>
  var edit_groupinformations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var edit = $(targetSelector).data("edit");
    $.get(edit).done(function (responseText, textStatus, jqXHR) {
      if (jqXHR.status === 200) {
        $(targetSelector).html(responseText);
      }
      $(targetSelector).removeClass("sk-loading");
    });
  };
</script>
