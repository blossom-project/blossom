<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/tabulation.ftl" as tabulation>
<#import "/blossom/utils/privilege.ftl" as privilege>
<#import "/blossom/utils/buttons.ftl" as button>
<#import "/blossom/utils/civility.ftl" as civility>
<#import "/blossom/utils/modal.ftl" as modal>


<@master.default currentUser=currentUser>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> ${currentUser.user.firstname +' '+ currentUser.user.lastname}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "menu.profile"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-md-6 col-lg-offset-4 col-lg-4">
      <div class="contact-box center-version">
        <div class="content">
          <img alt="image" class="img-circle" width="160" height="160" src="/blossom/administration/users/${currentUser.user.id?c}/avatar">

          <h3 class="m-b-xs"><strong>${currentUser.user.firstname} ${currentUser.user.lastname}</strong></h3>

          <div class="font-bold">${currentUser.user.function}</div>
          <div class="font-bold">${currentUser.user.company}</div>
          <address class="m-t-md">
            <abbr title="Email"><i class="fa fa-envelope"></i> : </abbr> ${currentUser.user.email}<br/>
            <abbr title="Phone"><i class="fa fa-phone"></i> : </abbr> ${currentUser.user.phone}<br/>
          </address>
        </div>
      </div>
    </div>
  </div>
  <#if currentUser.updatable>
    <div class="row">
      <div class="col-md-6 col-lg-offset-4 col-lg-4">
        <button class="btn btn-primary btn-block"  data-toggle="modal" data-target="#profileUpdatePasswordForm">
        <@spring.message "change.password.action"/>
        </button>
      </div>
    </div>
  </#if>
</div>

<#if currentUser.updatable>
  <@modal.large id="profileUpdatePasswordForm" href="/blossom/profile/password/_edit"/>
</#if>

</@master.default>


