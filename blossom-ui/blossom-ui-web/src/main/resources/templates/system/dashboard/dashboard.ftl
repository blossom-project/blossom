<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>

<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-lg-4">
      <div id="status" class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>Server status</h5>
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
      <script>
        $(document).ready(function () {
        var updateStatus = function () {
            $("#status .ibox-content").toggleClass("sk-loading");
            $.get("/blossom/system/dashboard/status", function (data) {
              $("#status").replaceWith(data);
            });
          };
          setInterval(updateStatus, 5000);
          updateStatus();
        });
      </script>

      <div class="col-lg-4">
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

      <div class="col-lg-4">
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
            </div>
            <div class="row">
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
    <div class="row">
      <div class="col-lg-12">
        <div id="loggers" class="ibox">
          <div class="ibox-title">
            <h5>Loggers</h5>
          </div>
          <div class="ibox-content" v-class="{'sk-loading':loading}">
            <div class="sk-spinner sk-spinner-wave">
              <div class="sk-rect1"></div>
              <div class="sk-rect2"></div>
              <div class="sk-rect3"></div>
              <div class="sk-rect4"></div>
              <div class="sk-rect5"></div>
            </div>
            <table class="table table-stripped">
              <tbody>
              <tr v-for="(logger, key,index) in loggers.loggers">
                <td :class="{ 'no-borders': index === 0}">
                  {{key}}
                </td>
                <td :class="{ 'no-borders': index === 0}">
                  <button class="btn " :class="{'btn-danger': logger.effectiveLevel ==='ERROR'}" type="button">ERROR
                  </button>
                  <button class="btn " :class="{'btn-warning': logger.effectiveLevel ==='WARN'}" type="button">WARN
                  </button>
                  <button class="btn " :class="{'btn-info': logger.effectiveLevel ==='INFO'}" type="button">INFO
                  </button>
                  <button class="btn " :class="{'btn-success': logger.effectiveLevel ==='DEBUG'}" type="button">DEBUG
                  </button>
                  <button class="btn " :class="{'btn-primary': logger.effectiveLevel ==='TRACE'}" type="button">TRACE
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>


  <script>
    $(document).ready(function () {

//    var status = new Vue({
//      el: '#status',
//      data: {
//        health: {},
//        uptime: ''
//      },
//      methods: {
//        update: function () {
//          this.loading = true;
//          var health = $.get("/blossom/actuator/health");
//          var metrics = $.get("/blossom/actuator/metrics");
//
//          $.when(health, metrics).done(function (h, m) {
//            this.health = h[0];
//            this.uptime = moment.duration(m[0]['uptime']).humanize();
//            this.loading = false;
//          }.bind(this));
//        }
//      },
//      created: function () {
//        this.update();
//        setInterval(this.update, 5000);
//      }
//    });


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
            processor: {
              total: 0
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

              this.jvm.processors = {
                total: metrics['processors']
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


      var loggers = new Vue({
        el: '#loggers',
        data: {
          loggers: {},
        },
        methods: {
          update: function () {
            this.loading = true;
            var loggers = $.get("/blossom/actuator/loggers");

            $.when(loggers).done(function (l) {
              this.loggers = l;
              this.loading = false;
            }.bind(this));
          }
        },
        created: function () {
          this.update();
          setInterval(this.update, 60000);
        }
      });
    });
  </script>
</@master.default>
