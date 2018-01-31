<#import "/spring.ftl" as spring>
<#import "/blossom/utils/civility.ftl" as civility>
<#import "/blossom/utils/buttons.ftl" as buttons>
<#import "/blossom/utils/privilege.ftl" as privilege>

<div class="ibox-content">
  <div class="sk-spinner sk-spinner-wave">
    <div class="sk-rect1"></div>
    <div class="sk-rect2"></div>
    <div class="sk-rect3"></div>
    <div class="sk-rect4"></div>
    <div class="sk-rect5"></div>
  </div>

  <div id="privilegeTree"></div>
</div>


<@privilege.has currentUser=currentUser privilege="administration:roles:write">
  <div class="ibox-footer">
    <div class="text-right">
      <button class="btn btn-primary btn-view" onclick="edit_roleprivileges(this);">
      <@spring.message "edit"/>
      </button>
    </div>
  </div>


  <script>
    $(document).ready(function () {
      var selectedPrivileges=[<#list role.privileges as privilege>"${privilege}"<#if privilege?has_next>,</#if></#list>];

      var transformNode = function(node){
        node.state = {
          checkbox_disabled: true,
          disabled: true
        };

        var children = [];
        $.each(node.children,function(index,value){
          children.push(transformNode(value));
        });
        node.children = children;
        return node;
      };


      $.get("privileges/tree", function (data) {
        $('#privilegeTree').closest(".ibox-content").removeClass("sk-loading");

        $('#privilegeTree').jstree({
          'core': {
            'data': [transformNode(data)]
          },
          'plugins': ["checkbox"]
        }).on('ready.jstree', function (e, data) {
          $("#privilegeTree").jstree(true).open_all();
          $.each(selectedPrivileges, function(i, p){
            $("#privilegeTree").jstree(true).check_node(p);
          });
        });
      });
    });

    var edit_roleprivileges = function (button) {
      var targetSelector = '#' + $(button).closest(".tab-pane").attr('id');
      $(targetSelector + ' > .ibox-content').addClass("sk-loading");
      var edit = $(targetSelector).data("edit");
      $.get(edit).done(function (responseText, textStatus, jqXHR) {
        if (jqXHR.status === 200) {
          $(targetSelector).html(responseText);
        }
        $(targetSelector).removeClass("sk-loading");
      });
    };
  </script>
</@privilege.has>
