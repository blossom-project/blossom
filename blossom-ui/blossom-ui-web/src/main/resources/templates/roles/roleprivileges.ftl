<#import "/spring.ftl" as spring>
<#import "/utils/civility.ftl" as civility>
<#import "/utils/buttons.ftl" as buttons>



<div class="ibox-content">
  <div class="sk-spinner sk-spinner-wave">
    <div class="sk-rect1"></div>
    <div class="sk-rect2"></div>
    <div class="sk-rect3"></div>
    <div class="sk-rect4"></div>
    <div class="sk-rect5"></div>
  </div>

  <form class="form form-horizontal">
    <#list privileges?keys as namespace>
      <div>
        <h3 class="text-capitalize">${namespace}</h3>
        <div class="row">
          <#list privileges[namespace]?keys as feature>
            <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
              <div class="form-group">
                <div class="col-sm-12">
                  <h5 class="text-capitalize">${feature}</h5>
                  <#list privileges[namespace][feature] as privilege>
                    <div>
                      <label>
                        <input type="checkbox" value="${privilege.privilege()}" name="privileges" disabled <#if role.privileges?? && role.privileges?seq_contains(privilege.privilege())>checked="checked"</#if>>
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
      <#if !(namespace?is_last)> <hr/></#if>
    </#list>
  </form>
</div>

<div class="ibox-footer">
  <div class="text-right">
    <button class="btn btn-primary btn-view" onclick="edit_roleprivileges(this);">
    <@spring.message "edit"/>
    </button>
  </div>
</div>


<script>
  var edit_roleprivileges = function (button) {
    var targetSelector = '#' + $(button).closest(".tab-pane").attr('id');
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
