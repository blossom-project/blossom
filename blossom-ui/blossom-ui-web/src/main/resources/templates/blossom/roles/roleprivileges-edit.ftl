<#import "/spring.ftl" as spring>
<#import "/blossom/utils/civility.ftl" as civility>
<#import "/blossom/utils/buttons.ftl" as buttons>
<#import "/blossom/utils/notification.ftl" as notification>

<form class="form form-horizontal" onsubmit="submit_roleprivileges(this);return false;">

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

  <div class="ibox-footer">
    <div class="text-right">
      <button class="btn btn-default btn-view" type="button" onclick="cancel_roleprivileges(this);">
      <@spring.message "cancel"/>
      </button>

      <button class="btn btn-primary btn-view" type="submit">
      <@spring.message "save"/>
      </button>
    </div>
  </div>

</form>

<script>
  $(document).ready(function () {
    var selectedPrivileges=[<#list rolePrivilegeUpdateForm.privileges as privilege>"${privilege}"<#if privilege?has_next>,</#if></#list>];

    var transformNode = function(node){
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
          $("#privilegeTree").jstree(true).check_node(p.replace(new RegExp(":", 'g'), '.'));
        });
      });
    });
  });

  var submit_roleprivileges = function (button) {
    var targetSelector = '#' + $(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var edit = $(targetSelector).data("edit");
    var form = $(targetSelector + ' > form');

    var selected = $("#privilegeTree").jstree(true).get_checked(null, true);
    $.each(selected, function(index,value){
      form.append("<input type='hidden' name='privileges' value='"+value+"'/>");
    });


    $.post(edit, form.serialize()).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      <@notification.success message="updated"/>
      $(targetSelector + ' > .ibox-content').removeClass("sk-loading");
    });
  };

  var cancel_roleprivileges = function (button) {
    var targetSelector = '#' + $(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var view = $(targetSelector).data("view");
    $.get(view).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      $(targetSelector).removeClass("sk-loading");
    });
  };
</script>
