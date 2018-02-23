<#import "/spring.ftl" as spring>
<#import "/blossom/utils/civility.ftl" as gender>
<#import "/blossom/utils/buttons.ftl" as buttons>
<#import "/blossom/utils/notification.ftl" as notification>
<#import "/blossom/utils/modal.ftl" as modal>

<form class="form form-horizontal" onsubmit="submit_entityinformations(this);return false;">

  <div class="ibox-content" >
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>

    <@spring.bind "%%UPDATE_FORM%%"/>
    <#if spring.status.error>
      <p class="alert alert-danger">
        <#list spring.status.errorMessages as error>
        ${error}<#if !error?is_last><br/></#if>
        </#list>
      </p>
    </#if>

      %%FIELD_FORM%%
      %%FIELD_FORM_INPUT%%
  <@spring.bind "%%UPDATE_FORM%%.%%FIELD_NAME%%"/>
      <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
          <div class="col-sm-10">
              <input type="%%FIELD_TYPE%%" name="%%FIELD_NAME%%" class="form-control" value="<#if %%UPDATE_FORM%%.%%FIELD_NAME%%??>${%%UPDATE_FORM%%.%%FIELD_NAME%%%%FIELD_CAST%%}</#if>"
                     placeholder="%%FIELD_LABEL%%">
          <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
          </#list>
          </div>
      </div>
      %%/FIELD_FORM_INPUT%%

      %%FIELD_FORM_BOOLEAN%%
      <div class="form-group">
          <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
          <div class="col-sm-10">
          <#if %%UPDATE_FORM%%.%%FIELD_NAME%%??>
                      <@buttons.switch checked=%%UPDATE_FORM%%.%%FIELD_NAME%% name="%%FIELD_NAME%%"/>
                  <#else>
            <@buttons.switch checked=false name="%%FIELD_NAME%%"/>
          </#if>

          </div>
      </div>
      %%/FIELD_FORM_BOOLEAN%%
      %%/FIELD_FORM%%


  </div>

  <div class="ibox-footer">
    <div class="text-right">
      <button class="btn btn-default btn-view" type="button" onclick="cancel_entityinformations(this);">
      <@spring.message "cancel"/>
      </button>

      <button class="btn btn-primary btn-view" type="submit">
      <@spring.message "save"/>
      </button>
    </div>
  </div>
</form>

<script>
  var submit_entityinformations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var edit = $(targetSelector).data("edit");
    var form = $(targetSelector + ' > form');

    $.post(edit, form.serialize())
    .done(function(data, textStatus, jqXHR){
      $(targetSelector).html(data);
      <@notification.success message="updated"/>
    })
    .fail(function( jqXHR, textStatus, errorThrown){
      $(targetSelector).html(jqXHR.responseText);
      <@notification.error message="errored"/>
    }).always(function(){
      $(targetSelector+ ' > .ibox-content').removeClass("sk-loading");
    });
  };

  var cancel_entityinformations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var view = $(targetSelector).data("view");
    $.get(view).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      $(targetSelector).removeClass("sk-loading");
    });
  };
</script>
