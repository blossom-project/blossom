<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
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

<div class="wrapper wrapper-content animated fadeInRight">


  <div class="row m-b-lg m-t-lg">
    <div class="col-md-6">

      <div class="profile-image">
        <img src="/img/a4.jpg" class="img-circle circle-border m-b-md" alt="profile">
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


    private String identifier;
    private String email;
    private String firstname;
    private String lastname;
    private String description;
    private boolean activated;
    private String passwordHash;
    private String phone;
    private String function;
  </div>
  <div class="row">
    <div class="col-lg-3">
      <div class="ibox">
        <div class="ibox-content">
          <h3><@spring.message "users.user.panel.account"/></h3>

          <p class="small">
            ${user.identifier}
          </p>

          <p class="small">
            ${user.passwordHash}
          </p>

          <p class="small">
            ${user.lastConnection?datetime}
          </p>


        </div>
      </div>
    </div>

    <div class="col-lg-3">
      <div class="ibox">
        <div class="ibox-content">
          <h3><@spring.message "users.user.panel.contact"/></h3>

          <p class="small">
          ${user.description}
          </p>

          <p class="small font-bold">
            <span><i class="fa fa-circle text-navy"></i> Online status</span>
          </p>

        </div>
      </div>
    </div>
  </div>

</div>
</@master.default>
