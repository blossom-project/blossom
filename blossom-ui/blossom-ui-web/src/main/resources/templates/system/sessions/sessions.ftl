<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2>
      <i class="fa fa-plug"></i>
      <@spring.message "menu.system.sessions"/>
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/system"><@spring.message "menu.system"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.sessions"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content scheduler">
  <div class="ibox float-e-margins">
    <div class="ibox-content">

      <table class="table table-stripped">
        <thead>
        <tr>
          <th>Name</th>
          <th>Sessions</th>
        </tr>
        </thead>
        <tbody>
          <#list sessions as principal, sessions>
          <tr>
            <td>
            <a href="/blossom/administration/users/${principal.user.id?c}">
              ${principal.user.identifier}
            </a>
            </td>
            <td>
              <ul>
                <#list sessions as session>
                  <li>${session.lastRequest?datetime} / ${session.sessionId} /  ${session.expired?string('yes','no')}</li>
                </#list>
              </ul>
            </td>
          </tr>
          </#list>
        </tbody>
      </table>
    </div>
  </div>
</div>
</@master.default>
