
<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-lg-12">
      <div class="ibox float-e-margins">
        <div class="ibox-content">
          <div>
                                        <span class="pull-right text-right">
                                        <small>Average value of sales in the past month in: <strong>United states</strong></small>
                                            <br>
                                            All sales: 162,862
                                        </span>
            <h1 class="m-b-xs">$ 50,992</h1>
            <h3 class="font-bold no-margins">
              Half-year revenue margin
            </h3>
            <small>Sales marketing.</small>
          </div>

          <div><iframe class="chartjs-hidden-iframe" style="width: 100%; display: block; border: 0px; height: 0px; margin: 0px; position: absolute; left: 0px; right: 0px; top: 0px; bottom: 0px;"></iframe>
            <canvas id="lineChart" height="320" style="display: block; width: 1373px; height: 320px;" width="1373"></canvas>
          </div>

          <div class="m-t-md">
            <small class="pull-right">
              <i class="fa fa-clock-o"> </i>
              Update on 16.07.2015
            </small>
            <small>
              <strong>Analysis of sales:</strong> The value has been changed over time, and last month reached a level over $50,000.
            </small>
          </div>

        </div>
      </div>
    </div>
  </div>


  <div class="row">

    <div class="col-lg-4">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-primary pull-right">Today</span>
          <h5>visits</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">22 285,400</h1>
          <div class="stat-percent font-bold text-navy">20% <i class="fa fa-level-up"></i></div>
          <small>New orders</small>
        </div>
      </div>
    </div>
    <div class="col-lg-4">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-info pull-right">Monthly</span>
          <h5>Orders</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">60 420,600</h1>
          <div class="stat-percent font-bold text-info">40% <i class="fa fa-level-up"></i></div>
          <small>New orders</small>
        </div>
      </div>
    </div>
    <div class="col-lg-4">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-warning pull-right">Annual</span>
          <h5>Income</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">$ 120 430,800</h1>
          <div class="stat-percent font-bold text-warning">16% <i class="fa fa-level-up"></i></div>
          <small>New orders</small>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-lg-6">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>New data for the report</h5>
          <div class="ibox-tools">
            <a class="collapse-link">
              <i class="fa fa-chevron-up"></i>
            </a>
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
              <i class="fa fa-wrench"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
              <li><a href="#">Config option 1</a>
              </li>
              <li><a href="#">Config option 2</a>
              </li>
            </ul>
            <a class="close-link">
              <i class="fa fa-times"></i>
            </a>
          </div>
        </div>
        <div class="ibox-content ibox-heading">
          <h3>Stock price up
            <div class="stat-percent text-navy">34% <i class="fa fa-level-up"></i></div>
          </h3>
          <small><i class="fa fa-stack-exchange"></i> New economic data from the previous quarter.</small>
        </div>
        <div class="ibox-content">
          <div>

            <div class="pull-right text-right">

              <span class="bar_dashboard" style="display: none;">5,3,9,6,5,9,7,3,5,2,4,7,3,2,7,9,6,4,5,7,3,2,1,0,9,5,6,8,3,2,1</span><svg class="peity" height="16" width="100"><rect fill="#1ab394" x="0" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="3.2580645161290325" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#1ab394" x="6.516129032258065" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#d7d7d7" x="9.774193548387098" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#1ab394" x="13.03225806451613" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="16.290322580645164" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#1ab394" x="19.548387096774196" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#d7d7d7" x="22.806451612903228" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#1ab394" x="26.06451612903226" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="29.322580645161292" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="32.58064516129033" y="8.88888888888889" width="2.2580645161290325" height="7.111111111111111"></rect><rect fill="#d7d7d7" x="35.83870967741936" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#1ab394" x="39.09677419354839" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="42.35483870967742" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="45.612903225806456" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#d7d7d7" x="48.87096774193549" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#1ab394" x="52.12903225806452" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#d7d7d7" x="55.38709677419355" y="8.88888888888889" width="2.2580645161290325" height="7.111111111111111"></rect><rect fill="#1ab394" x="58.645161290322584" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="61.903225806451616" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#1ab394" x="65.16129032258065" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="68.41935483870968" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="71.67741935483872" y="14.222222222222221" width="2.2580645161290325" height="1.7777777777777777"></rect><rect fill="#d7d7d7" x="74.93548387096774" y="15" width="2.2580645161290325" height="1"></rect><rect fill="#1ab394" x="78.19354838709678" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#d7d7d7" x="81.45161290322581" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#1ab394" x="84.70967741935485" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#d7d7d7" x="87.96774193548387" y="1.7777777777777786" width="2.2580645161290325" height="14.222222222222221"></rect><rect fill="#1ab394" x="91.22580645161291" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="94.48387096774194" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="97.74193548387098" y="14.222222222222221" width="2.2580645161290325" height="1.7777777777777777"></rect></svg>
              <br>
              <small class="font-bold">$ 20 054.43</small>
            </div>
            <h4>NYS report new data!
              <br>
              <small class="m-r"><a href="graph_flot.html"> Check the stock price! </a> </small>
            </h4>
          </div>
        </div>
      </div>
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>Read below comments and tweets</h5>
          <div class="ibox-tools">
            <a class="collapse-link">
              <i class="fa fa-chevron-up"></i>
            </a>
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
              <i class="fa fa-wrench"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
              <li><a href="#">Config option 1</a>
              </li>
              <li><a href="#">Config option 2</a>
              </li>
            </ul>
            <a class="close-link">
              <i class="fa fa-times"></i>
            </a>
          </div>
        </div>
        <div class="ibox-content no-padding">
          <ul class="list-group">
            <li class="list-group-item">
              <p><a class="text-info" href="#">@Alan Marry</a> I belive that. Lorem Ipsum is simply dummy text of the printing and typesetting industry.</p>
              <small class="block text-muted"><i class="fa fa-clock-o"></i> 1 minuts ago</small>
            </li>
            <li class="list-group-item">
              <p><a class="text-info" href="#">@Stock Man</a> Check this stock chart. This price is crazy! </p>
              <small class="block text-muted"><i class="fa fa-clock-o"></i> 2 hours ago</small>
            </li>
            <li class="list-group-item">
              <p><a class="text-info" href="#">@Kevin Smith</a> Lorem ipsum unknown printer took a galley </p>
              <small class="block text-muted"><i class="fa fa-clock-o"></i> 2 minuts ago</small>
            </li>
            <li class="list-group-item ">
              <p><a class="text-info" href="#">@Jonathan Febrick</a> The standard chunk of Lorem Ipsum</p>
              <small class="block text-muted"><i class="fa fa-clock-o"></i> 1 hour ago</small>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="col-lg-6">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>Timeline</h5>
          <span class="label label-primary">Meeting today</span>
          <div class="ibox-tools">
            <a class="collapse-link">
              <i class="fa fa-chevron-up"></i>
            </a>
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
              <i class="fa fa-wrench"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
              <li><a href="#">Config option 1</a>
              </li>
              <li><a href="#">Config option 2</a>
              </li>
            </ul>
            <a class="close-link">
              <i class="fa fa-times"></i>
            </a>
          </div>
        </div>

        <div class="ibox-content inspinia-timeline">

          <div class="timeline-item">
            <div class="row">
              <div class="col-xs-3 date">
                <i class="fa fa-briefcase"></i>
                6:00 am
                <br>
                <small class="text-navy">2 hour ago</small>
              </div>
              <div class="col-xs-7 content no-top-border">
                <p class="m-b-xs"><strong>Meeting</strong></p>

                <p>Conference on the sales results for the previous year. Monica please examine sales trends in marketing and products.</p>

              </div>
            </div>
          </div>
          <div class="timeline-item">
            <div class="row">
              <div class="col-xs-3 date">
                <i class="fa fa-file-text"></i>
                7:00 am
                <br>
                <small class="text-navy">3 hour ago</small>
              </div>
              <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Send documents to Mike</strong></p>
                <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since.</p>
              </div>
            </div>
          </div>
          <div class="timeline-item">
            <div class="row">
              <div class="col-xs-3 date">
                <i class="fa fa-coffee"></i>
                8:00 am
                <br>
              </div>
              <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Coffee Break</strong></p>
                <p>
                  Go to shop and find some products.
                  Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's.
                </p>
              </div>
            </div>
          </div>
          <div class="timeline-item">
            <div class="row">
              <div class="col-xs-3 date">
                <i class="fa fa-phone"></i>
                11:00 am
                <br>
                <small class="text-navy">21 hour ago</small>
              </div>
              <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Phone with Jeronimo</strong></p>
                <p>
                  Lorem Ipsum has been the industry's standard dummy text ever since.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>

</div>
<script>
  $(document).ready(function() {

    var lineData = {
      labels: ["January", "February", "March", "April", "May", "June", "July"],
      datasets: [
        {
          label: "Example dataset",
          backgroundColor: "rgba(26,179,148,0.5)",
          borderColor: "rgba(26,179,148,0.7)",
          pointBackgroundColor: "rgba(26,179,148,1)",
          pointBorderColor: "#fff",
          data: [28, 48, 40, 19, 86, 27, 90]
        },
        {
          label: "Example dataset",
          backgroundColor: "rgba(220,220,220,0.5)",
          borderColor: "rgba(220,220,220,1)",
          pointBackgroundColor: "rgba(220,220,220,1)",
          pointBorderColor: "#fff",
          data: [65, 59, 80, 81, 56, 55, 40]
        }
      ]
    };

    var lineOptions = {
      responsive: true
    };


    var ctx = document.getElementById("lineChart").getContext("2d");
    new Chart(ctx, {type: 'line', data: lineData, options:lineOptions});

  });
</script>
</@master.default>
