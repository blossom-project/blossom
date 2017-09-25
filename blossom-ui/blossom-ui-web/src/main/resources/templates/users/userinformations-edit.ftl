<#import "/spring.ftl" as spring>
<#import "/utils/civility.ftl" as gender>
<#import "/utils/buttons.ftl" as buttons>
<#import "/utils/notification.ftl" as notification>
<#import "/utils/modal.ftl" as modal>

<form class="form form-horizontal" onsubmit="submit_userinformations(this);return false;">

  <div class="ibox-content" >
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>


    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.identifier"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.identifier!''}</p>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.activated"/></label>
      <div class="col-sm-10">
          <@buttons.switch checked=userUpdateForm.activated name="activated"/>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastConnection"/></label>
      <div class="col-sm-10">
        <p class="form-control-static"><#if user.lastConnection??>${user.lastConnection?datetime}<#else><@spring.message "never"/></#if></p>
      </div>
    </div>

    <div class="hr-line-dashed"></div>

    <@spring.bind "userUpdateForm.firstname"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.firstname"/></label>
      <div class="col-sm-10">
        <input type="text" name="firstname" class="form-control" value="${userUpdateForm.firstname!''}" placeholder="<@spring.message "users.user.properties.firstname"/>">
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
      </div>
    </div>

    <@spring.bind "userUpdateForm.lastname"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastname"/></label>
      <div class="col-sm-10">
        <input type="text" name="lastname" class="form-control" value="${userUpdateForm.lastname!''}" placeholder="<@spring.message "users.user.properties.lastname"/>">
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
      </div>
    </div>

    <@spring.bind "userUpdateForm.description"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.description"/></label>
      <div class="col-sm-10">
        <textarea class="form-control" rows="5" name="description">${userUpdateForm.description!''}</textarea>
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
      </div>
    </div>


    <@spring.bind "userUpdateForm.civility"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.civility"/></label>
      <div class="col-sm-10">
        <#list civilities as civility>
          <div class="radio radio-success radio-inline">
            <input type="radio" class="radio" value="${civility}" id="gender_${civility}" name="civility" <#if userUpdateForm.civility == civility>checked</#if>>
            <label for="gender_${civility}"> <@gender.icon civility=civility/> <@gender.label civility=civility/> </label>
          </div>
        </#list>
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
      </div>
    </div>

    <@spring.bind "userUpdateForm.company"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.company"/></label>
      <div class="col-sm-10">
        <input type="text" name="company" class="form-control" value="${userUpdateForm.company!''}" placeholder="<@spring.message "users.user.properties.company"/>">
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
      </div>
    </div>

    <@spring.bind "userUpdateForm.function"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.function"/></label>
      <div class="col-sm-10">
        <input type="text" name="function" class="form-control" value="${userUpdateForm.function!''}" placeholder="<@spring.message "users.user.properties.function"/>">
      <#list spring.status.errorMessages as error>
        <span class="help-block text-danger m-b-none">${error}</span>
      </#list>
      </div>
    </div>

    <div class="hr-line-dashed"></div>

    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.avatar"/></label>
      <div class="col-sm-10 profile-image">
        <a data-target="#userAvatarUpdateForm" data-toggle="modal">
          <img src="/blossom/administration/users/${user.id?c}/avatar" class="img-circle circle-border m-b-md" alt="profile">
        </a>
      </div>
    </div>

    <div class="hr-line-dashed"></div>

    <@spring.bind "userUpdateForm.email"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.email"/></label>
      <div class="col-sm-10">
        <input type="text" name="email" class="form-control" value="${userUpdateForm.email!''}" placeholder="<@spring.message "users.user.properties.email"/>">
      <#list spring.status.errorMessages as error>
        <span class="help-block text-danger m-b-none">${error}</span>
      </#list>
      </div>
    </div>

    <@spring.bind "userUpdateForm.phone"/>
    <div class="form-group <#if spring.status.error>has-error</#if>">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.phone"/></label>
      <div class="col-sm-10">
        <input type="text" name="phone" class="form-control" value="${userUpdateForm.phone!''}" placeholder="<@spring.message "users.user.properties.phone"/>">
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
      </div>
    </div>
  </div>

  <div class="ibox-footer">
    <div class="text-right">
      <button class="btn btn-default btn-view" type="button" onclick="cancel_userinformations(this);">
      <@spring.message "cancel"/>
      </button>

      <button class="btn btn-primary btn-view" type="submit">
      <@spring.message "save"/>
      </button>
    </div>
  </div>
</form>

<script>
  var submit_userinformations = function (button) {
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

  var cancel_userinformations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var view = $(targetSelector).data("view");
    $.get(view).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      $(targetSelector).removeClass("sk-loading");
    });
  };
</script>


<@modal.large id="userAvatarUpdateForm" href="/blossom/administration/users/${id?c}/_avatar/_edit"/>
