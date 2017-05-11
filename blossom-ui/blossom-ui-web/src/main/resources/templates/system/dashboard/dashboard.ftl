<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/panel.ftl" as panel>


<@master.default currentUser=currentUser>
<div class="wrapper wrapper-content">
  <div class="row">

    <div class="col-lg-3">
      <@panel.autoReloadPanel id="status" url="/blossom/system/dashboard/status"/>
    </div>

    <div class="col-lg-4">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>Memory</h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-4 text-center">
              <span id="memory-global"></span>
            </div>

            <div class="col-xs-4 text-center">
              <span id="memory-heap"></span>
            </div>

            <div class="col-xs-4 text-center">
              <span id="memory-nonheap"></span>
            </div>
          </div>
        </div>

        <script>
          $(document).ready(function(){
            $("#memory-global").sparkline([25, 75], {
              type: 'pie',
              height: '70px',
              sliceColors: ['#e4f0fb', '#b3b3b3']});

            $("#memory-heap").sparkline([25, 5, 70], {
              type: 'pie',
              height: '70px',
              sliceColors: ['#1ab394', '#b3b3b3', '#e4f0fb']});

            $("#memory-nonheap").sparkline([25, 5, 70], {
              type: 'pie',
              height: '70px',
              sliceColors: ['red', '#b3b3b3', '#e4f0fb']});
          });
        </script>
      </div>
    </div>
    <div class="col-lg-4">
      <div class="ibox">
        <div class="ibox-title">
          <h5>Classes</h5>
        </div>
        <div class="ibox-content">
          <table class="table table-stripped small">
            <tbody>
            <tr>
              <td class="no-borders">
                1250
              </td>
              <td class="no-borders">
                Total
              </td>
            </tr>
            <tr>
              <td>
              1249
              </td>
              <td>
                Loaded
              </td>
            </tr>
            <tr>
              <td>
                0
              </td>
              <td>
                Unloaded
              </td>
            </tr>
            </tbody>
          </table>
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
</@master.default>
