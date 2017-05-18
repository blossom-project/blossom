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

    <div class="col-lg-8">
      <div class="row">
        <div class="col-lg-12">
          <div id="requests" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Requests</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
      <div class="row">
        <div class="col-lg-4">
          <div id="response_status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Response status</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
        <div class="col-lg-4">
          <div id="response_status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Response time</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
        <div class="col-lg-4">
          <div id="response_status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Response status</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
      <div class="row">
        <div class="col-lg-4">
          <div id="response_status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Top URIs</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
        <div class="col-lg-4">
          <div id="response_status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Flop URIs</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
        <div class="col-lg-4">
          <div id="response_status" class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Response status</h5>
            </div>
            <div class="ibox-content sk-loading" style="min-height: 200px;">
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
  </div>
</div>


<script>
  $(document).ready(function () {

    var updateStatus = function () {
      $("#status .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/status").always(function (data) {
        setTimeout(function () {
          $("#status").replaceWith(data);
        }, 500);
      });
    };

    var updateMemory = function () {
      $("#memory .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/memory").always(function (data) {
        setTimeout(function () {
          $("#memory").replaceWith(data);
        }, 500);
      });
    };
    var updateJvm = function () {
      $("#jvm .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/jvm").always(function (data) {
        setTimeout(function () {
          $("#jvm").replaceWith(data);
        }, 500);
      });
    };

    var updateRequests = function () {
      $("#requests .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/system/dashboard/requests").always(function (data) {
        setTimeout(function () {
          $("#requests").replaceWith(data);
        }, 500);
      });
    };

    var updateAll = function () {
      updateStatus();
      updateMemory();
      updateJvm();
    };

    setInterval(updateAll, 5000);
    updateAll();
    updateRequests();
  });
</script>


<!-- Morris -->
<script src="/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script src="/js/plugins/morris/morris.js"></script>
</@master.default>
