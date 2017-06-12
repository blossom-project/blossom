<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> <@spring.message "users.creation"/></h2>
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
        <strong><@spring.message "users.creation"/></strong>
      </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
  <div class="ibox">
    <div class="ibox-title">
      <h3><@spring.message "users.creation"/></h3>
    </div>
    <div class="ibox-content">
      <form class="form form-horizontal" method="POST">


        <div class="form-group">
          <label class="col-sm-2 control-label">Identifier</label>
          <div class="col-sm-10">
            <input type="text" name="identifier" class="form-control" value="${user.identifier!''}">
          </div>
        </div>

        <div class="hr-line-dashed"></div>

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.firstname"/></label>

          <div class="col-sm-10">
            <input type="text" name="firstname" class="form-control" value="${user.firstname!''}">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastname"/></label>
          <div class="col-sm-10">
            <input type="text" name="lastname" class="form-control" value="${user.lastname!''}">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.civility"/></label>
          <div class="col-sm-10">
            <div>
              <label>
                <input type="radio" checked="" value="option1" id="optionsRadios1" name="optionsRadios">
                M
              </label>
            </div>
          </div>
        </div>

        <div class="hr-line-dashed"></div>

        <div class="form-group">
          <label class="col-sm-2 control-label">E-mail</label>
          <div class="col-sm-10">
            <input type="text" name="email" class="form-control" value="${user.email!''}">
          </div>
        </div>


        <div class="form-group">
          <label class="col-sm-2 control-label">Phone</label>
          <div class="col-sm-10">
            <input type="text" name="phone" class="form-control" value="${user.phone!''}">
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
</@master.default>
