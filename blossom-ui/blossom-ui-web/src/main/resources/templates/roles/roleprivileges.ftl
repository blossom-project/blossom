<#import "/spring.ftl" as spring>
<#import "/utils/civility.ftl" as civility>
<#import "/utils/buttons.ftl" as buttons>
<#import "/utils/privilege.ftl" as privilege>

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
        <h3>
          <@spring.messageText "right."+namespace namespace/>
        </h3>
        <div class="row">
          <#list privileges[namespace]?keys as feature>
            <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
              <div class="form-group">
                <div class="col-sm-12">
                  <h5>
                    <@spring.messageText "right."+namespace+"."+feature feature/>
                  </h5>
                  <#list privileges[namespace][feature] as privilege>
                    <div class="m-l-md">
                      <label>
                        <input type="checkbox" value="${privilege.privilege()}" name="privileges" disabled <#if role.privileges?? && role.privileges?seq_contains(privilege.privilege())>checked="checked"</#if>>
                      <@spring.messageText "right."+namespace+"."+feature+"."+privilege.right() privilege.right() />
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


<@privilege.has currentUser=currentUser privilege="administration:roles:write">
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
</@privilege.has>
