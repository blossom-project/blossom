<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>

<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-lg-3">
      <div id="status" class="ibox float-e-margins">
        <div class="ibox-title">
          <div class="pull-right"><span class="label label-primary">started {{uptime}} ago</span></div>
          <h5>Server status</h5>
        </div>
        <div class="ibox-content" v-class="{'sk-loading':loading}">
          <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
          </div>

          <h2 class="global text-navy">
            <i class="fa fa-play"
               :class="{'fa-rotate-270': health.status === 'UP', 'fa-rotate-90': health.status != 'UP'}"></i>
            {{health.status}}
          </h2>

          <table class="table table-stripped small m-t-md">
            <tbody>
            <tr v-for="(detail, key, index) in health" v-if="key!='status'">
              <td :class="{ 'no-borders': index === 1, 'text-navy': detail.status === 'UP' }">
                <i class="fa fa-play"
                   :class="{'fa-rotate-270': detail.status === 'UP', 'fa-rotate-90': detail.status != 'UP'}"></i>
                {{detail.status}}
              </td>
              <td :class="{ 'no-borders': index === 1 }">
                {{key}}
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="col-lg-3">
      <div id="memory" class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>Memory</h5>
        </div>
        <div class="ibox-content" v-class="{'sk-loading':loading}">
          <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
          </div>

          <div class="row">
            <div class="col-xs-12">
              <ul class="stat-list">
                <li>
                  <h2 class="no-margins">{{memory.all.percentage}}%</h2>
                  <small>Global</small>
                  <div class="stat-percent">{{memory.all.used}} / {{memory.all.total}}</div>
                  <div class="progress progress-mini">
                    <div :style="{'width': memory.all.percentage+'%'}" class="progress-bar"></div>
                  </div>
                </li>
                <li>
                  <h2 class="no-margins ">{{memory.heap.percentage}}%</h2>
                  <small>Heap memory</small>
                  <div class="stat-percent">{{memory.heap.used}} / {{memory.heap.committed}}</div>
                  <div class="progress progress-mini">
                    <div :style="{'width': memory.heap.percentage+'%'}" class="progress-bar"></div>
                  </div>
                </li>
                <li>
                  <h2 class="no-margins ">{{memory.nonheap.percentage}}%</h2>
                  <small>Non-heap memory</small>
                  <div class="stat-percent">{{memory.nonheap.used}} / {{memory.nonheap.committed}}</div>
                  <div class="progress progress-mini">
                    <div :style="{'width': memory.nonheap.percentage+'%'}" class="progress-bar"></div>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-lg-3">
      <div class="ibox">
        <div class="ibox-title">
          <h5>Tomcat</h5>
        </div>
        <div class="ibox-content" v-class="{'sk-loading':loading}">
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

    <div class="col-lg-3">
      <div id="jvm" class="ibox">
        <div class="ibox-title">
          <h5>JVM</h5>
        </div>
        <div class="ibox-content" v-class="{'sk-loading':loading}">
          <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
          </div>

          <div class="row">
            <div class="col-xs-6">
              <h3>Classes</h3>
              <table class="table table-stripped small">
                <tbody>
                <tr>
                  <td class="no-borders">
                    Total
                  </td>
                  <td class="no-borders">
                    {{jvm.classes.total}}
                  </td>
                </tr>
                <tr>
                  <td>
                    Loaded
                  </td>
                  <td>
                    {{jvm.classes.loaded}}
                  </td>
                </tr>
                <tr>
                  <td>
                    Unloaded
                  </td>
                  <td>
                    {{jvm.classes.unloaded}}
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <div class="col-xs-6">
              <h3>GC</h3>
              <table class="table table-stripped small">
                <tbody>
                <tr v-for="(gc, key, index) in jvm.gc">
                  <td :class="{'no-borders':index ===0}">
                    {{key}}
                  </td>
                  <td :class="{'no-borders':index ===0}">
                    {{gc.count}} <br/> {{gc.time}}
                  </td>
                </tr>
                </tbody>
              </table>
            </div>

            <div class="col-xs-6">
              <h3>Threads</h3>
              <table class="table table-stripped small">
                <tbody>
                <tr>
                  <td class="no-borders">
                    Total
                  </td>
                  <td class="no-borders">
                    {{jvm.threads.total}}
                  </td>
                </tr>
                <tr>
                  <td>
                    Total started
                  </td>
                  <td>
                    {{jvm.threads.totalStarted}}
                  </td>
                </tr>
                <tr>
                  <td>
                    Peak
                  </td>
                  <td>
                    {{jvm.threads.peak}}
                  </td>
                </tr>
                <tr>
                  <td>
                    Daemons
                  </td>
                  <td>
                    {{jvm.threads.daemon}}
                  </td>
                </tr>
                </tbody>
              </table>
            </div>

            <div class="col-xs-6">
              <h3>Processors</h3>
              <table class="table table-stripped small">
                <tbody>
                <tr>
                  <td class="no-borders">
                    Total
                  </td>
                  <td class="no-borders">
                    {{jvm.processors.total}}
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


<script>
  $(document).ready(function () {

    var status = new Vue({
      el: '#status',
      data: {
        health: {},
        uptime: ''
      },
      methods: {
        update: function () {
          this.loading = true;
          var health = $.get("/blossom/actuator/health");
          var metrics = $.get("/blossom/actuator/metrics");

          $.when(health, metrics).done(function (h, m) {
            this.health = h[0];
            this.uptime = moment.duration(m[0]['uptime']).humanize();
            this.loading = false;
          }.bind(this));
        }
      },
      created: function () {
        this.update();
        setInterval(this.update, 5000);
      }
    });


    var memory = new Vue({
      el: '#memory',
      data: {
        memory: {
          all: {
            percentage: 0,
            total: 0,
            free: 0,
            used: 0,
          },
          heap: {
            percentage: 0,
            total: 0,
            committed: 0,
            init: 0,
            used: 0
          },
          nonheap: {
            percentage: 0,
            total: 0,
            committed: 0,
            init: 0,
            used: 0
          }
        }
      },
      methods: {
        bytesToSize: function (bytes, decimals) {
          if (bytes == 0) return '0 Bytes';
          var k = 1000,
            dm = decimals || 2,
            sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));
          return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
        },
        update: function () {
          this.loading = true;
          $.get("/blossom/actuator/metrics", function (metrics) {
            this.memory.all = {
              percentage: ((metrics['mem'] / (metrics['mem'] + metrics['mem.free'])) * 100).toFixed(2),
              total: this.bytesToSize((metrics['mem'] + metrics['mem.free']) * 1024, 1),
              free: this.bytesToSize(metrics['mem.free'] * 1024, 1),
              used: this.bytesToSize(metrics['mem'] * 1024, 1)
            };

            this.memory.heap = {
              percentage: !metrics['heap.committed'] || metrics['heap.committed'] === 0 ? 0 : ((metrics['heap.used'] / metrics['heap.committed']) * 100).toFixed(2),
              total: this.bytesToSize(metrics['heap'] * 1024, 1),
              committed: this.bytesToSize(metrics['heap.committed'] * 1024, 1),
              init: this.bytesToSize(metrics['heap.init'] * 1024, 1),
              used: this.bytesToSize(metrics['heap.used'] * 1024, 1)
            };

            this.memory.nonheap = {
              percentage: !metrics['nonheap.committed'] || metrics['nonheap.committed'] === 0 ? 0 : ((metrics['nonheap.used'] / metrics['nonheap.committed']) * 100).toFixed(2),
              total: this.bytesToSize(metrics['nonheap'] * 1024, 1),
              committed: this.bytesToSize(metrics['nonheap.committed'] * 1024, 1),
              init: this.bytesToSize(metrics['nonheap.init'] * 1024, 1),
              used: this.bytesToSize(metrics['nonheap.used'] * 1024, 1)
            };

            this.loading = false;
          }.bind(this));
        }
      },
      created: function () {
        this.update();
        setInterval(this.update, 5000);
      }
      ,
      updated: function () {
        this.$nextTick(function () {
          console.log("updated");
        });
      }
    });


    var jvm = new Vue({
      el: '#jvm',
      data: {
        jvm: {
          classes: {
            total: 0,
            loaded: 0,
            unloaded: 0
          },
          gc: {},
          processor:{
              total:0
          }
        }
      },
      methods: {
        update: function () {
          this.loading = true;
          $.get("/blossom/actuator/metrics", function (metrics) {
            this.jvm.classes = {
              total: metrics['classes'],
              loaded: metrics['classes.loaded'],
              unloaded: metrics['classes.unloaded']
            };

            this.jvm.gc = {};
            $.each(metrics, function (key, value) {
              var splittedKey = key.split(".");
              console.log(splittedKey);
              if (splittedKey[0] === 'gc') {
                if (!this.jvm.gc[splittedKey[1]]) {
                  this.jvm.gc[splittedKey[1]] = {};
                }

                if (splittedKey[2] === 'time') {
                  this.jvm.gc[splittedKey[1]][splittedKey[2]] = moment.duration(value).humanize();
                } else {
                  this.jvm.gc[splittedKey[1]][splittedKey[2]] = value;
                }
              }
            }.bind(this));

            this.jvm.threads = {
              total: metrics['threads'],
              daemon: metrics['threads.daemon'],
              peak: metrics['threads.peak'],
              totalStarted: metrics['threads.totalStarted'],
            };

            this.jvm.processors={
                total:metrics['processors']
            };

            this.loading = false;
          }.bind(this));
        }
      },
      created: function () {
        this.update();
        setInterval(this.update, 5000);
      }
      ,
      updated: function () {
        this.$nextTick(function () {
          console.log("updated");
        });
      }
    });

    var indicators = {
      health: {
        panels: ["#status"],
        callback: function (health) {
          console.log("Health", health);
          statusPanel.health = health;
          console.log(statusPanel);
        }
      },
      beans: {
        panels: ["#beans"],
        callback: function (beans) {
          console.log("Beans", beans);
        }
      },
      metrics: {
        panels: "#metrics",
        callback: function (metrics) {
          console.log("Metrics", metrics);
        }
      }
    };
    var updateIndicator = function (indicator) {
      $.get("/blossom/actuator/" + indicator, function (data) {
        indicators[indicator].callback(data);
      });
    };
    var updateAll = function () {
      $.each(indicators, function (indicator) {
        updateIndicator(indicator);
      });
    };

//    setInterval(updateAll, 5000);
//
//    updateAll();
  });

</script>
</@master.default>
