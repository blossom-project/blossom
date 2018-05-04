<#import "/spring.ftl" as spring>

<div class="profilepassword_modal">
  <div class="modal-header">
    <h4 class="modal-title"><@spring.message "change.password.page.title"/></h4>
  </div>

  <div class="modal-body">
    <form id="updatePasswordForm" class="m-t" role="form" method="POST" action="/blossom/profile/password/_edit" novalidate>

    <@spring.bind "updatePasswordForm"/>
    <#assign hasGlobalError = spring.status.error/>

    <@spring.bind "updatePasswordForm.password"/>
      <div class="form-group <#if spring.status.error || hasGlobalError>has-error</#if>">
        <input type="password" name="password" class="form-control" placeholder="<@spring.message "password"/>" required="">
      <#list spring.status.errorMessages as error>
        <span class="help-block text-danger m-b-none">${error}</span>
      </#list>
      </div>

    <@spring.bind "updatePasswordForm.passwordRepeater"/>
      <div class="form-group <#if spring.status.error || hasGlobalError>has-error</#if>">
        <input type="password" name="passwordRepeater" class="form-control" placeholder="<@spring.message "change.password.passwordRepeater"/>" required="">
      <#list spring.status.errorMessages as error>
        <span class="help-block text-danger m-b-none">${error}</span>
      </#list>
      </div>

    <@spring.bind "updatePasswordForm"/>
    <#if hasGlobalError>
      <p class="alert alert-danger">
        <#list spring.status.errorMessages as error>
        ${error}
          <#if !error?is_last><br/></#if>
        </#list>
      </p>
    </#if>

    </form>
  </div>
</div>

<div class="modal-footer">
  <button id="cancel_update_password" class="btn btn-default" data-dismiss="modal">
    <@spring.message "cancel"/>
  </button>
  <button id="save_update_password" class="btn btn-primary">
    <@spring.message "save"/>
  </button>
</div>


<script>
  $(document).ready(function(){
    var form = $("#updatePasswordForm");

    form.submit(function(e){

      e.preventDefault();

      $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: form.serialize(),
        success: function (data) {
          form.closest(".modal").find(".modal-content").html(data);
        },
        error: function (data) {
          form.closest(".modal").find(".modal-content").html(data);
        }
      });
    });

    $("#save_update_password").click(function () {
      form.trigger('submit');
    });
  });
</script>
