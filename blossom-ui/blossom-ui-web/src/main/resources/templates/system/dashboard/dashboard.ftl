<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<!-- Morris -->
<link href="/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">

<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-lg-4">
      <div class="row">

        <div class="col-lg-12">
          <div id="status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Server status</h5>
            </div>
            <div class="ibox-content" style="min-height: 200px;">
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

        <div class="col-lg-12">
          <div id="memory" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Server status</h5>
            </div>
            <div class="ibox-content" style="min-height: 200px;">
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

        <div class="col-lg-12">
          <div id="jvm" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Server status</h5>
            </div>
            <div class="ibox-content" style="min-height: 200px;">
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

    <div id="charts" class="col-lg-8">
    </div>
  </div>
</div>


<script>
  $(document).ready(function () {

    var updateStatus = function () {
      $("#status .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/status").always(function (data) {
          $("#status").replaceWith(data);
      });
    };

    var updateMemory = function () {
      $("#memory .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/memory").always(function (data) {
          $("#memory").replaceWith(data);
      });
    };
    var updateJvm = function () {
      $("#jvm .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/jvm").always(function (data) {
          $("#jvm").replaceWith(data);
      });
    };

    var updateCharts = function () {
      $("#charts .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/charts").always(function (data) {
          $("#charts").html(data);
      });
    };

    var updateAll = function () {
      updateStatus();
      updateMemory();
      updateJvm();
    };

    setInterval(updateAll, 5000);
    updateAll();
    updateCharts();
  });
</script>


<!-- Morris -->
<script src="/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script src="/js/plugins/morris/morris.js"></script>

<!-- Morris -->
<script src="/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script src="/js/plugins/morris/morris.js"></script>
</@master.default>
