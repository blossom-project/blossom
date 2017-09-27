<#import "/spring.ftl" as spring>
<#import "/utils/civility.ftl" as civility>
<#import "/utils/buttons.ftl" as buttons>
<#import "/utils/notification.ftl" as notification>

<form class="form form-horizontal" onsubmit="submit_roleprivileges(this);return false;">

<div class="ibox-content" >
  <div class="sk-spinner sk-spinner-wave">
    <div class="sk-rect1"></div>
    <div class="sk-rect2"></div>
    <div class="sk-rect3"></div>
    <div class="sk-rect4"></div>
    <div class="sk-rect5"></div>
  </div>

    <div class="row">
    <#list privileges?keys as namespace>
      <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
        <div class="form-group">
          <label class="col-sm-4 control-label">${namespace}</label>
          <div class="col-sm-8">
            <#list privileges[namespace] as privilege>
              <div>
                <label>
                  <input type="checkbox" value="${privilege.privilege()}" name="privileges" <#if rolePrivilegeUpdateForm.privileges?seq_contains(privilege.privilege())>checked="checked"</#if>>
                ${privilege.right()}
                </label>
              </div>
            </#list>
          </div>
        </div>
      </div>
    </#list>
    </div>
</div>

<div class="ibox-footer">
  <div class="text-right">
    <button class="btn btn-default btn-view" type="button" onclick="cancel_roleprivileges(this);">
      <@spring.message "cancel"/>
    </button>

    <button class="btn btn-primary btn-view" type="submit">
      <@spring.message "save"/>
    </button>
  </div>
</div>

</form>



<script>
  var submit_roleprivileges = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var edit = $(targetSelector).data("edit");
    var form = $(targetSelector + ' > form');

    $.post(edit, form.serialize()).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      <@notification.success message="updated"/>
      $(targetSelector+ ' > .ibox-content').removeClass("sk-loading");
    });
  };

  var cancel_roleprivileges = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var view = $(targetSelector).data("view");
    $.get(view).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      $(targetSelector).removeClass("sk-loading");
    });
  };
</script>
