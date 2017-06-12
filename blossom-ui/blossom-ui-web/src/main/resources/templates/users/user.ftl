<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


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

  <div class="row m-b-lg m-t-lg">
    <div class="col-md-6">

      <div class="profile-image">
        <img src="/blossom/administration/users/${user.id?c}/avatar" class="img-circle circle-border m-b-md"
             alt="profile">
      </div>
      <div class="profile-info">
        <div class="">
          <div>
            <h2 class="no-margins">
            ${user.firstname +' '+user.lastname}
            </h2>
            <h4>${user.function!''}</h4>
            <small>
            ${user.description!''}
            </small>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-3">

    </div>
    <div class="col-md-3">
    </div>
  </div>
  <div class="row">
    <div class="col-sm-12">
      <div class="ibox">
        <div class="ibox-title">
          <h3><@spring.message "users.user.panel.personal"/></h3>
        </div>
        <div class="ibox-content">
          <form class="form form-horizontal">
            <div class="form-group">
              <label class="col-sm-2 control-label">Firstname</label>

              <div class="col-sm-10">
                <p class="form-control-static">${user.firstname}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-2 control-label">Lastname</label>
              <div class="col-sm-10">
                <p class="form-control-static">${user.lastname}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-2 control-label">Civility</label>
              <div class="col-sm-10">
                <p class="form-control-static">${user.civility}</p>
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

            <div class="hr-line-dashed"></div>

            <div class="form-group">
              <label class="col-sm-2 control-label">Identifier</label>
              <div class="col-sm-10">
                <p class="form-control-static">${user.identifier}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-2 control-label">Activated</label>
              <div class="col-sm-10">
                <p class="form-control-static">${user.activated?string("yes","no")}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="col-sm-2 control-label">Last connexion</label>
              <div class="col-sm-10">
                <p class="form-control-static">${user.lastConnection?datetime}</p>
              </div>
            </div>

          </form>
        </div>
      </div>
    </div>
  </div>

</div>
</@master.default>
