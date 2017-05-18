<div id="requests" class="ibox float-e-margins">
  <div class="ibox-title">
    <h5>Requests</h5>
  </div>
  <div class="ibox-content" style="position: relative">
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>

    <div id="morris-area-chart"></div>
  </div>
</div>


<script>
  $(document).ready(function () {
    var area = Morris.Area({
      element: 'morris-area-chart',
      data: [],
      xkey: 'period',
      pointSize: 3,
      hideHover: 'auto',
      resize: true,
      lineWidth: 2,
      pointSize: 1
    });

    var updateRequestsData = function () {
      $("#requests .ibox-content").toggleClass("sk-loading");
      $.get("/blossom/actuator/trace_stats", function (data) {
        var chartData = [];
        var keys = [];

        var histogrammBars = data.aggregations.histogram.buckets;
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


        area.options.labels = keys;
        area.options.ykeys = keys;

        var rainbow = new Rainbow();
        rainbow.setSpectrumByArray(['#1ab394','#d3d3d3']);
        rainbow.setNumberRange(0, keys.length);
        var colors = [];
        $.each(keys,function(index,key){
          console.log(index,key,rainbow.colourAt(index))
          colors.push('#'+rainbow.colourAt(index));
        });

        area.options.lineColors = colors;

        console.log(keys, chartData, colors);

        area.setData(chartData);
        setTimeout(function(){$("#requests .ibox-content").toggleClass("sk-loading")},500);
      });
    };

    setInterval(updateRequestsData, 10000);
    updateRequestsData();
  });
</script>
