<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>

<@master.default currentUser=currentUser>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
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
  <div class="row">
    <div class="col-sm-6">
      <div class="ibox float-e-margins">
        <div class="ibox-content sk-loading">
          <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
          </div>

          <div class="row">
            <div class="col-sm-12">
              <form class="form form-horizontal">
                <div class="form-group">
                  <input id="loggers-filter"
                         type="text"
                         placeholder="<@spring.message "list.searchbar.placeholder"/>"
                         class="input-sm form-control"
                  />
                </div>
              </form>
            </div>
          </div>

          <div id="loggers-tree"></div>

        </div>
      </div>
    </div>

    <div class="col-sm-6">
      <div class="ibox float-e-margins">
        <div class="ibox-content sk-loading">
          <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
          </div>
          <div id="loggers-detail" style="min-height:100px;"></div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>

  var colors = {
    'OFF' : "text-muted",
    'ERROR' : "text-danger text-",
    'WARN' : "text-warning",
    'INFO' : "text-info",
    'DEBUG' : "text-success",
    'TRACE' : "text-navy"
  };

  var enrich = function(node){
    node.li_attr = {class: colors[node.data]};
    $.each(node.children, function(index,node){
      enrich(node);
    });
  };

  var changeLogLevel = function(logger, level){
    $.post("loggers/"+logger+"/"+level, function(){
      $.get("loggers/tree", function (data) {
        enrich(data);
        $('#loggers-tree').jstree(true).settings.core.data = [data];
        $('#loggers-tree').jstree(true).refresh();
        $('#loggers-tree').jstree("select_node", logger);
      });
    });
  };

  $(document).ready(function () {
    $.get("loggers/tree", function (data) {

      enrich(data);

      $('#loggers-tree').closest(".ibox-content").removeClass("sk-loading");

      $('#loggers-tree').jstree({
        'core': {
          'data': [data],
          'multiple':false
        },
        'plugins': ["search", "types", "stackedicon"],
        'search': {
          'show_only_matches': true,
          'show_only_matches_children': true,
          'close_opened_onclear': false
        },
        'types': {
          'default' : {
            'icon' : "fa fa-pencil"
          }
        }
      }).on("select_node.jstree", function(event, node) {
        $('#loggers-detail').closest(".ibox-content").addClass("sk-loading")
        $('#loggers-detail').load("loggers/"+node.node.id, function(){
          $('#loggers-detail').closest(".ibox-content").removeClass("sk-loading");
        });
      }).on('ready.jstree', function(e, data) {
        $(this).jstree("select_node", 'ROOT');
      });
    });

    var to = false;
    $('#loggers-filter').keyup(function () {
      if (to) {
        clearTimeout(to);
      }
      to = setTimeout(function () {
        var v = $('#loggers-filter').val();
        $('#loggers-tree').jstree(true).search(v);
      }, 250);
    });
  });
</script>
</@master.default>
