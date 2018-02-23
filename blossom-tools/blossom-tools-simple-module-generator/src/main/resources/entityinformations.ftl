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
      %%FIELD_FORM%%
      %%FIELD_FORM_INPUT%%
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "%%ENTITY_NAME_PLURAL%%.%%ENTITY_NAME%%.properties.%%FIELD_NAME%%"/></label>
      <div class="col-sm-10">
        <p class="form-control-static"><#if %%ENTITY_NAME%%.%%FIELD_NAME%%??>${%%ENTITY_NAME%%.%%FIELD_NAME%%%%FIELD_CAST%%}</#if></p>
      </div>
    </div>
      %%/FIELD_FORM_INPUT%%

      %%FIELD_FORM_BOOLEAN%%
      <div class="form-group">
          <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
          <div class="col-sm-10">
          <#if %%ENTITY_NAME%%.%%FIELD_NAME%%??>
                      <@buttons.switch checked=%%ENTITY_NAME%%.%%FIELD_NAME%% disabled=true name="%%FIELD_NAME%%"/>
                  <#else>
            <@buttons.switch checked=false disabled=true name="%%FIELD_NAME%%"/>
          </#if>

          </div>
      </div>
      %%/FIELD_FORM_BOOLEAN%%

      %%FIELD_FORM_SELECT%%
      <div class="form-group">
          <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
          <div class="col-sm-10">
              <select class="form-control" name="%%FIELD_NAME%%" disabled>
                  %%FIELD_FORM_SELECT_OPTION%%
                    <option value="%%OPTION_VALUE%%" <#if "%%OPTION_VALUE%%"==%%ENTITY_NAME%%.%%FIELD_NAME%%!"">selected</#if>>%%OPTION_LABEL%%</option>
                  %%/FIELD_FORM_SELECT_OPTION%%
              </select>
          </div>
      </div>
      %%/FIELD_FORM_SELECT%%

      %%/FIELD_FORM%%
  </form>
</div>

<div class="ibox-footer">
  <div class="text-right">
    <button class="btn btn-primary btn-view" onclick="edit_entityinformations(this);">
    <@spring.message "edit"/>
    </button>
  </div>
</div>

<script>
  var edit_entityinformations = function (button) {
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
