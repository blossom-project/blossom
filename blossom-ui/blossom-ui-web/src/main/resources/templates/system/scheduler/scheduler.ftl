<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
    <h2><@spring.message "scheduler.title"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "scheduler.title"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content scheduler">
  <div class="row">
    <div class="col-sm-8">
      <div class="ibox">
        <div class="ibox-content">
          <div class="scheduler-info m-t-md">
            <div class="form form-horizontal">
              <div class="row">
                <div class="col-lg-6">
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Scheduler name</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.name}</p></div>
                  </div>
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Started</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.start?datetime}</p></div>
                  </div>
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Standby</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.standBy?string('yes','no')}</p>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Started</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.started?string('yes','no')}</p>
                    </div>
                  </div>
                </div>
                <div class="col-lg-6">
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Pool size</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.poolsize}</p></div>
                  </div>
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Jobs</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.jobs}</p></div>
                  </div>
                  <div class="form-group">
                    <label class="col-lg-2 control-label">Triggers</label>
                    <div class="col-lg-10"><p class="form-control-static">${scheduler.triggers}</p></div>
                  </div>
                </div>
              </div>

            </div>
          </div>

          <div class="scheduled-tasks-list m-t-md">
            <ul class="nav nav-tabs">
              <#list groups as group>
                <li <#if group?is_first>class="active"</#if>>
                  <a data-toggle="tab"
                     data-scheduler-group="${group}"
                     data-href="/blossom/system/scheduler/${group}"
                     data-target="#${group}">
                  ${group}
                  </a>
                </li>
              </#list>
            </ul>
            <div class="tab-content m-t-">
              <#list groups as group>
                <div id="${group}" class="tab-pane <#if group?is_first>active</#if>">
                </div>
              </#list>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="scheduled-task-detail" class="col-sm-4">
      <div class="ibox">
        <div class="ibox-content">
          <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  var listTimer = null;
  var panelTimer = null;

  $('.scheduler a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    updateTabPane($(e.target));
  });

  var updateTabPane = function (target) {
    var targetId = target.data("target");
    var href = target.data("href");
    var group = target.data("scheduler-group");

    $(document).trigger("scheduledTaskDeselected");

    if (listTimer) {
      clearInterval(listTimer);
      listTimer = null;
    }

    var callback = function () {
      $.get(href, function (html) {
        $(targetId).html(html);
      });
    };

    callback();
    listTimer = setInterval(callback, 5000);
  };

  var updateDetailPane = function (href) {
    clearPanelTimer();
    $("#scheduled-task-detail").find('.ibox-content').toggleClass('sk-loading');

    if (!href) {
      $("#scheduled-task-detail").html(null);
    } else {
      var callback = function () {
        $.get(href, function (html) {
          setTimeout(function () {
            $("#scheduled-task-detail").html(html)
          }, 200);
        });
      };
      callback();
      panelTimer = setInterval(callback, 5000);
    }
  };

  var clearPanelTimer = function () {
    if (panelTimer) {
      clearInterval(panelTimer);
      panelTimer = null;
    }
  };

  $(document).on("scheduledTaskSelected", function (event, href) {
    updateDetailPane(href)
  });

  $(document).on("scheduledTaskDeselected", function (event) {
    updateDetailPane(null)
  });

  updateTabPane($('.scheduler  li.active a[data-toggle="tab"]'));

</script>
</@master.default>
