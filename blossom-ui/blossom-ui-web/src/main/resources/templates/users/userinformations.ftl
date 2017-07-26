<#import "/spring.ftl" as spring>
<#import "/utils/civility.ftl" as civility>
<#import "/utils/buttons.ftl" as buttons>

<form class="form form-horizontal">

    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.identifier"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.identifier!''}</p>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.activated"/></label>
      <div class="col-sm-10">
          <@buttons.switch checked=user.activated disabled=true/>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastConnection"/></label>
      <div class="col-sm-10">
        <p class="form-control-static"><#if user.lastConnection??>${user.lastConnection?datetime}<#else><@spring.message "never"/></#if></p>
      </div>
    </div>
    
    <div class="hr-line-dashed"></div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.firstname"/></label>
    
      <div class="col-sm-10">
        <p class="form-control-static">${user.firstname}</p>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastname"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.lastname}</p>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.description"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.description!""}</p>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.civility"/></label>
      <div class="col-sm-10">
        <p class="form-control-static"><@civility.icon civility=user.civility/> <@civility.label civility=user.civility/></p>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.company"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.company!""}</p>
      </div>
    </div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.function"/></label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.function!""}</p>
      </div>
    </div>
    
    <div class="hr-line-dashed"></div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label"><@spring.message "users.user.properties.avatar"/></label>
    
      <div class="col-sm-10 profile-image">
        <img src="/blossom/administration/users/${user.id?c}/avatar" class="img-circle circle-border m-b-md" alt="profile">
      </div>
    </div>
    
    <div class="hr-line-dashed"></div>
    
    <div class="form-group">
      <label class="col-sm-2 control-label">E-mail</label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.email}</p>
      </div>
    </div>
    
    
    <div class="form-group">
      <label class="col-sm-2 control-label">Phone</label>
      <div class="col-sm-10">
        <p class="form-control-static">${user.phone!''}</p>
      </div>
    </div>

</form>
