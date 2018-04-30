<#import "/spring.ftl" as spring/>

<#macro default currentUser originalUser>
    <div class="row">
      <div class="col-xs-12">
        <div class="bg-danger p-sm text-center">
          <div><@spring.message "impersonation.in_progress"/></div>
          <div>
            <#assign usersArgs = ["${originalUser.user.firstname}", "${originalUser.user.lastname}", "${originalUser.user.identifier}","${currentUser.user.firstname}", "${currentUser.user.lastname}", "${currentUser.user.identifier}"]/>
            <@spring.messageArgs "impersonation.details" usersArgs/>
          </div>
          <a class="btn btn-warning btn-rounded"
             href="/blossom/administration/_impersonate/logout"><@spring.message "cancel"/></a>
        </div>
      </div>
    </div>
</#macro>
