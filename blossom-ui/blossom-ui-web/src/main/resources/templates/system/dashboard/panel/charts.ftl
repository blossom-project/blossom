<#import "/spring.ftl" as spring>

<div class="row">
  <div class="col-lg-12">
    <div class="ibox float-e-margins">
      <div class="ibox-content text-center" style="position: relative">
        <p>
          <a class="btn btn-primary btn-xs btn-rounded" data-toggle="period" data-period="PAST_WEEK"><@spring.message "dashboard.charts.period.PAST_WEEK"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_5_DAYS"><@spring.message "dashboard.charts.period.PAST_5_DAYS"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_3_DAYS"><@spring.message "dashboard.charts.period.PAST_3_DAYS"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_2_DAYS"><@spring.message "dashboard.charts.period.PAST_2_DAYS"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="YESTERDAY"><@spring.message "dashboard.charts.period.YESTERDAY"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="TODAY"><@spring.message "dashboard.charts.period.TODAY"/></a>
          <br/>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_24_HOURS"><@spring.message "dashboard.charts.period.PAST_24_HOURS"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_12_HOURS"><@spring.message "dashboard.charts.period.PAST_12_HOURS"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_6_HOURS"><@spring.message "dashboard.charts.period.PAST_6_HOURS"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_HOUR"><@spring.message "dashboard.charts.period.PAST_HOUR"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_30_MINUTES"><@spring.message "dashboard.charts.period.PAST_30_MINUTES"/></a>
          <a class="btn btn-default btn-xs btn-rounded" data-toggle="period" data-period="PAST_5_MINUTES"><@spring.message "dashboard.charts.period.PAST_5_MINUTES"/></a>
        </p>
      </div>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-lg-12">
    <div id="requests" class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "dashboard.charts.request"/></h5>
      </div>
      <div class="ibox-content" style="position: relative">
        <div class="sk-spinner sk-spinner-wave">
          <div class="sk-rect1"></div>
          <div class="sk-rect2"></div>
          <div class="sk-rect3"></div>
          <div class="sk-rect4"></div>
          <div class="sk-rect5"></div>
        </div>

        <div id="requests-area-chart" style="height: 200px;"></div>
      </div>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-lg-4">
    <div id="response_status" class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "dashboard.charts.response_status"/></h5>
      </div>
      <div class="ibox-content">
        <div class="sk-spinner sk-spinner-wave">
          <div class="sk-rect1"></div>
          <div class="sk-rect2"></div>
          <div class="sk-rect3"></div>
          <div class="sk-rect4"></div>
          <div class="sk-rect5"></div>
        </div>
        <div id="response-status-donut-chart" style="height: 200px;"></div>
      </div>
    </div>
  </div>
  <div class="col-lg-4">
    <div id="response_time" class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "dashboard.charts.response_time"/></h5>
      </div>
      <div class="ibox-content">
        <div class="sk-spinner sk-spinner-wave">
          <div class="sk-rect1"></div>
          <div class="sk-rect2"></div>
          <div class="sk-rect3"></div>
          <div class="sk-rect4"></div>
          <div class="sk-rect5"></div>
        </div>
        <div id="response-time-bar-chart" style="height: 200px;"></div>
      </div>
    </div>
  </div>
  <div class="col-lg-4">
    <div id="response_content_type" class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "dashboard.charts.response_content_type"/></h5>
      </div>
      <div class="ibox-content">
        <div class="sk-spinner sk-spinner-wave">
          <div class="sk-rect1"></div>
          <div class="sk-rect2"></div>
          <div class="sk-rect3"></div>
          <div class="sk-rect4"></div>
          <div class="sk-rect5"></div>
        </div>
        <div id="response-content-type-donut-chart" style="height: 200px;"></div>
      </div>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-lg-6">
    <div id="top_uris" class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "dashboard.charts.top_uris"/></h5>
      </div>
      <div class="ibox-content">
        <div class="sk-spinner sk-spinner-wave">
          <div class="sk-rect1"></div>
          <div class="sk-rect2"></div>
          <div class="sk-rect3"></div>
          <div class="sk-rect4"></div>
          <div class="sk-rect5"></div>
        </div>
        <table class="table table-responsive table-stripped">
          <thead>
          <tr>
            <th><@spring.message "dashboard.charts.top_uris.uri"/></th>
            <th><@spring.message "dashboard.charts.top_uris.count"/></th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="col-lg-6">
    <div id="flop_uris" class="ibox float-e-margins">
      <div class="ibox-title">
        <h5><@spring.message "dashboard.charts.flop_uris"/></h5>
      </div>
      <div class="ibox-content">
        <div class="sk-spinner sk-spinner-wave">
          <div class="sk-rect1"></div>
          <div class="sk-rect2"></div>
          <div class="sk-rect3"></div>
          <div class="sk-rect4"></div>
          <div class="sk-rect5"></div>
        </div>

        <table class="table table-responsive table-stripped">
          <thead>
          <tr>
            <th><@spring.message "dashboard.charts.flop_uris.uri"/></th>
            <th><@spring.message "dashboard.charts.flop_uris.count"/></th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>


<script>
  $(document).ready(function () {
      var requestHistogramChart = Morris.Area({
        element: 'requests-area-chart',
        data: [],
        xkey: 'period',
        pointSize: 3,
        hideHover: 'auto',
        resize: true,
        lineWidth: 2,
        pointSize: 1
      });

      var responseTimeBarChart = Morris.Bar({
        element: 'response-time-bar-chart',
        data: [],
        xkey: 'time',
        ykeys: ['count'],
        labels: ['Count'],
        hideHover: 'auto',
        resize: true,
        barColors: ['#1ab394']
      });

      var responseStatusDonutChart = Morris.Donut({
        element: 'response-status-donut-chart',
        data: [{label: '200', value: 1}],
        resize: true,
        colors: ['#87d6c6', '#54cdb4', '#1ab394'],
      });


      var responseContentTypeDonutChart = Morris.Donut({
        element: 'response-content-type-donut-chart',
        data: [{label: 'text/html', value: 1}],
        resize: true,
        colors: ['#87d6c6', '#54cdb4', '#1ab394'],
      });


      var updateRequests = function (data) {
        var chartData = [];
        var keys = [];

        var histogrammBars = data.aggregations.request_histogram.buckets;
        $.each(histogrammBars, function (index, histogrammBar) {
          var item = {period: histogrammBar.key};
          var methods = histogrammBar.methods.buckets;
          $.each(methods, function (index2, method) {
            item[method.key] = method.doc_count;
            if (keys.indexOf(method.key) == -1) {
              keys.push(method.key)
            }
          });
          chartData.push(item);
        });


        if (chartData.length > 0) {
          var rainbow = new Rainbow();
          rainbow.setSpectrumByArray(['#1ab394', '#d3d3d3']);
          rainbow.setNumberRange(0, keys.length);
          var colors = [];
          $.each(keys, function (index, key) {
            colors.push('#' + rainbow.colourAt(index));
          });

          requestHistogramChart.options.lineColors = colors;
          requestHistogramChart.options.labels = keys;
          requestHistogramChart.options.ykeys = keys;
          requestHistogramChart.setData(chartData);
        }else{
          requestHistogramChart.setData(chartData);
        }

      };

      var updateResponseTime = function (data) {
        var chartData = [];
        var responseTimeBars = data.aggregations.response_time_histogram.buckets;
        $.each(responseTimeBars, function (index, responseTimeBar) {
          var item = {time: responseTimeBar.key, count: responseTimeBar.doc_count};
          chartData.push(item);
        });

        responseTimeBarChart.setData(chartData);

      };

      var updateResponseStatus = function (data) {
        var chartData = [];
        var responseStatuses = data.aggregations.response_status_stats.buckets;
        $.each(responseStatuses, function (index, responseStatus) {
          var key = responseStatus.key;
          var item = {label: key, value: responseStatus.doc_count};
          chartData.push(item);
        });
        if (chartData.length > 0) {
          var rainbow = new Rainbow();
          rainbow.setSpectrumByArray(['#1ab394', '#d3d3d3']);
          rainbow.setNumberRange(0, chartData.length);
          var colors = [];
          $.each(chartData, function (index) {
            colors.push('#' + rainbow.colourAt(index));
          });

          responseStatusDonutChart.options.colors = colors;
          responseStatusDonutChart.setData(chartData);
        }
        else {
          responseStatusDonutChart.setData([{label: 'No data', value: 0}]);
        }
      };


      var updateResponseContentType = function (data) {
        var chartData = [];
        var responseContentTypes = data.aggregations.response_content_type_stats.buckets;
        $.each(responseContentTypes, function (index, responseContentType) {
          var key = responseContentType.key.indexOf(';') != -1 ? responseContentType.key.split(';')[0] : responseContentType.key;
          var item = {label: key, value: responseContentType.doc_count};
          chartData.push(item);
        });
        if (chartData.length > 0) {
          var rainbow = new Rainbow();
          rainbow.setSpectrumByArray(['#1ab394', '#d3d3d3']);
          rainbow.setNumberRange(0, chartData.length);
          var colors = [];
          $.each(chartData, function (index) {
            colors.push('#' + rainbow.colourAt(index));
          });

          responseContentTypeDonutChart.options.colors = colors;
          responseContentTypeDonutChart.setData(chartData);
        } else {
          responseContentTypeDonutChart.setData([{label: 'No data', value: 0}]);
        }
      };

      var updateTopUris = function (data) {
        var html = '';
        var topUris = data.aggregations.top_uris.buckets;
        $.each(topUris, function (index, topUri) {
          var item = {label: topUri.key, value: topUri.doc_count};
          html += '<tr><td>' + topUri.key + '</td><td>' + topUri.doc_count + '</td></tr>'
        });
        $("#top_uris table tbody").html(html);
      };

      var updateFlopUris = function (data) {
        var html = '';
        var flopUris = data.aggregations.flop_uris.buckets;
        $.each(flopUris, function (index, flopUri) {
          var item = {label: flopUri.key, value: flopUri.doc_count};
          html += '<tr><td>' + flopUri.key + '</td><td>' + flopUri.doc_count + '</td></tr>'
        });
        $("#flop_uris table tbody").html(html);
      };

      var currentPeriod = 'PAST_WEEK';
      $("[data-toggle='period']").click(function () {
        var buttons = $("[data-toggle='period']")
        buttons.removeClass("btn-primary");
        buttons.addClass("btn-default");

        var current = $(this);
        current.removeClass("btn-default");
        current.addClass("btn-primary");

        currentPeriod = $(this).data("period");
        updateData();
      });

      var updateData = function () {
        $(document).find("#charts .ibox-content").toggleClass("sk-loading");
        $.get("/blossom/actuator/tracesstats?period=" + currentPeriod, function (data) {
          updateRequests(data);
          updateResponseTime(data);
          updateResponseStatus(data);
          updateResponseContentType(data);
          updateTopUris(data);
          updateFlopUris(data);

          $(document).find("#charts .ibox-content").toggleClass("sk-loading");
        });
      };

      setInterval(updateData, 10000);
      updateData();
    }
  );
</script>
