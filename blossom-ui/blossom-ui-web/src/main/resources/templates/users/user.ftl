<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/civility.ftl" as civility>
<#import "/utils/buttons.ftl" as buttons>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> ${user.firstname +' '+ user.lastname}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
      </li>
      <li>
        <a href="/blossom/administration/users"><@spring.message "users.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "users.user.title"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-sm-12">
      <div class="ibox">
        <div class="ibox-title">
          <h3><@spring.message "panel.information"/></h3>
        </div>
        <div class="ibox-content">
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
                <p class="form-control-static">${user.description}</p>
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
                <p class="form-control-static">${user.company}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-2 control-label"><@spring.message "users.user.properties.function"/></label>
              <div class="col-sm-10">
                <p class="form-control-static">${user.function}</p>
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
                <p class="form-control-static">${user.phone}</p>
              </div>
            </div>

          </form>
        </div>
      </div>
    </div>
  </div>

</div>
</@master.default>
