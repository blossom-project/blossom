<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>

<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
    <h2>
      <i class="fa fa-pencil"></i>
      <@spring.message "menu.system.loggers"/>
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/system"><@spring.message "menu.system"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "menu.system.loggers"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content scheduler">
  <div class="ibox float-e-margins">
    <div class="ibox-content">
      <div class="row">
        <div class="col-sm-9 m-b-xs">
        </div>
        <div class="col-sm-3">
          <div class="input-group">
            <input type="text"
                   placeholder="<@spring.message "list.searchbar.placeholder"/>"
                   class="table-search input-sm form-control"
                   onkeyup="var which = event.which || event.keyCode;if(which === 13) {$(this).closest('.input-group').find('button.table-search').first().click();}"
              <#if q?has_content> value="${q}"</#if>
            />

            <span class="input-group-btn">
              <button type="button"
                      class="btn btn-sm btn-primary table-search"
                      onclick="var value = $(this).closest('.input-group').children('input.table-search').first().val();window.location.href = $.updateQueryStringParameter(window.location.href,'q',value);">
                  <i class="fa fa-search"></i>
              </button>
            </span>
          </div>
        </div>
      </div>

      <table class="table table-stripped">
        <thead>
        <tr>
          <th>Name</th>
          <th>Level</th>
        </tr>
        </thead>
        <tbody>
          <#list loggers.loggers?keys as name>
          <tr>
            <td>
            ${name}
            </td>
            <td>
              <#list loggers.levels as level>
                <#assign levelClass = "btn-default"/>
                <#if level == 'OFF' && loggers.loggers[name].effectiveLevel==level>  <#assign levelClass = "bg-black"/>
                <#elseif level == 'ERROR' && loggers.loggers[name].effectiveLevel==level>  <#assign levelClass = "btn-danger"/>
                <#elseif level == 'WARN' && loggers.loggers[name].effectiveLevel==level>  <#assign levelClass = "btn-warning"/>
                <#elseif level == 'INFO' && loggers.loggers[name].effectiveLevel==level>  <#assign levelClass = "btn-info"/>
                <#elseif level == 'DEBUG' && loggers.loggers[name].effectiveLevel==level>  <#assign levelClass = "btn-success"/>
                <#elseif level == 'TRACE' && loggers.loggers[name].effectiveLevel==level>  <#assign levelClass = "btn-primary"/>
                </#if>
                <button class="btn btn-rounded btn-xs ${levelClass}" data-action="changeLogLevel" data-logger="${name}"
                        data-loglevel="${level}">${level}</button>
              </#list>
            </td>
          </tr>
          </#list>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
  $(document).ready(function () {
    $("[data-action='changeLogLevel']").click(function (e) {
      var logger = $(this).data("logger");
      var loglevel = $(this).data("loglevel");

      $.post("loggers/" + logger + "/" + loglevel, function () {
        window.location.reload(true);
      });
    });
  });
</script>
</@master.default>
