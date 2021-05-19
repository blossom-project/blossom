<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>


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
        <@spring.message "menu.system"/>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.sessions"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="row wrapper wrapper-content scheduler">
  <div class="col-xs-12 col-md-6">
    <div class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "sessions.properties.active_sessions"/></h5>
      </div>
      <div class="ibox-content">

        <table class="table table-stripped">
          <thead>
          <tr>
            <th><@spring.message "sessions.properties.name"/></th>
            <th><@spring.message "sessions.properties.sessions"/></th>
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
                <table class="table table-stripped small">
                  <tbody>
                    <#list sessions as session>
                    <tr>
                      <td class="no-borders">
                        <i
                          class="fa fa-circle <#if session.expired>text-warning<#else>text-navy</#if>"></i>
                      </td>
                      <td class="no-borders">
                      ${session.sessionId}
                      </td>
                      <td class="no-borders text-muted">
                      ${session.lastRequest?datetime}
                      </td>
                      <td class="no-borders">
                        <button
                          class="btn btn-xs btn-danger"
                          data-action="expireSession"
                          data-session-id="${session.sessionId}">
                          <i class="fa fa-trash"></i>
                        </button>
                      </td>
                    </tr>
                    </#list>
                  </tbody>
                </table>
              </td>
            </tr>
            </#list>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="col-xs-12 col-md-6">
    <div class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "sessions.properties.failed_login_attemps"/></h5>
      </div>
      <div class="ibox-content">

        <table class="table table-stripped">
          <thead>
          <tr>
            <th><@spring.message "sessions.properties.identifier"/></th>
            <th><@spring.message "sessions.properties.ip_address"/></th>
            <th><@spring.message "sessions.properties.attempts"/></th>
            <th><@spring.message "sessions.properties.timeLock"/></th>
            <th><@spring.message "sessions.properties.clear"/></th>
          </tr>
          </thead>
          <tbody>

          <#assign now = .now>
            <#list attempts as identifier, datas>
            <tr>
              <td rowspan="${datas?size}">${identifier}</td>
              <#list datas as data>
                <td>${data.ip}</td>
                <td>${data.attemptNumber?c}</td>
                <td><#if data.attemptNumber == 10>${data.hours}h${data.minutes}</#if></td>
                <td>
                  <button
                          class="btn btn-xs btn-danger"
                          data-action="clearCache"
                          data-session-ip="${data.ip}"
                          data-session-id="${identifier}"
                  >
                    <i class="fa fa-trash"></i></td>
              </tr>
                <#if data_has_next>
                <tr></#if>
              </#list>
            </#list>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>


<script>
  $(document).ready(function () {
    $("[data-action='expireSession']").click(function (e) {
      var sessionId = $(this).data("session-id");

      $.post("sessions/" + sessionId + "/_invalidate", function () {
        window.location.reload(true);
      });
    });
    $("[data-action='clearCache']").click(function (e) {
      var sessionId = $(this).data("session-id");
      var ip = $(this).data("session-ip");

      $.post("sessions/" + sessionId + "/" + ip + "/clear", function () {
        window.location.reload(true);
      });
    });
  });
</script>
</@master.default>
