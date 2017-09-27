<#import "/spring.ftl" as spring>
<#import "/utils/table.ftl" as table>
<#import "/utils/notification.ftl" as notification>

<#function isAssociated associations userId roleId>
  <#list associations as association>
    <#if association.a.id == userId && association.b.id == roleId>
      <#return true/>
    </#if>
  </#list>
  <#return false/>
</#function>

<form  class="form form-horizontal" onsubmit="submit_role_users_associations(this);return false;">
  <div class="ibox-content">
    <div class="sk-spinner sk-spinner-wave">
      <div class="sk-rect1"></div>
      <div class="sk-rect2"></div>
      <div class="sk-rect3"></div>
      <div class="sk-rect4"></div>
      <div class="sk-rect5"></div>
    </div>

    <div class="row">
      <select id="association-selector" name="ids" multiple>
        <#list users as user>
          <option value="${user.id?c}" <#if isAssociated(associations, user.id, role.id)>selected</#if>>${user.firstname} ${user.lastname} (${user.identifier})</option>
        </#list>
      </select>
    </div>
  </div>

  <div class="ibox-footer">
    <div class="text-right">
      <button class="btn btn-default btn-view" type="button" onclick="cancel_role_users_associations(this);">
      <@spring.message "cancel"/>
      </button>

      <button class="btn btn-primary btn-view" type="submit">
      <@spring.message "save"/>
      </button>
    </div>
  </div>
</form>


<script>
  $(document).ready(function(){
    $('#association-selector').bootstrapDualListbox({
      nonSelectedListLabel: '<@spring.message "state.available.plural"/>',
      selectedListLabel: '<@spring.message "state.associated.plural"/>',
      infoText:'<@spring.message "state.showing.count"/>',
      infoTextFiltered:'<@spring.message "state.showing.filtered.count"/>',
      infoTextEmpty:'<@spring.message "state.empty"/>',
      selectorMinimalHeight:350
    });
  });

  var submit_role_users_associations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var edit = $(targetSelector).data("edit");
    var form = $(targetSelector + ' > form');

    $.post(edit, form.serialize()).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
    <@notification.success message="updated"/>
      $(targetSelector+ ' > .ibox-content').removeClass("sk-loading");
    });
  };

  var cancel_role_users_associations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var view = $(targetSelector).data("view");
    $.get(view).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      $(targetSelector).removeClass("sk-loading");
    });
  };
</script>
