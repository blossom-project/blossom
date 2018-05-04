<#import "/spring.ftl" as spring>

<div class="profilepassword_modal">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal">
      <span>&times;</span><span class="sr-only">Close</span>
    </button>

    <h4 class="modal-title"><@spring.message "profile.change.password.done.title"/></h4>
  </div>
  <div class="modal-body">
    <p class="alert alert-success">
      <@spring.message "profile.change.password.done.message"/>
    </p>
  </div>
</div>

<div class="modal-footer">
  <button class="btn btn-primary" data-dismiss="modal">
    <@spring.message "ok"/>
  </button>
</div>
