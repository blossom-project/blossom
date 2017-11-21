<!DOCTYPE html>

<html lang="en">

<body>

<form method="post">
  Blossom version : <select name="version"><#list initializr.versions as version><option value="${version.blossom}" <#if version?is_first>selected="selected"</#if>>${version.blossom}</option></#list></select><br/>
  Group : <input type="text" name="groupId" value="${project.groupId}"/><br/>
  ArtifactId : <input type="text" name="artifactId" value="${project.artifactId}"/><br/>
  Name : <input type="text" name="name" value="${project.name}"/><br/>
  Description : <input type="text" name="description" value="${project.description}"/><br/>
  Package name: <input type="text" name="packageName" value="${project.packageName}"/><br/>
  Packaging mode: <select name="packagingMode"><#list packagingModes as packagingMode><option value="${packagingMode}">${packagingMode.label}</option></#list></select><br/>

  <button type="submit">
    Generate project
  </button>

  <#list initializr.groups as group>
    <h2>${group.name}</h2>
    <p>${group.description}</p>

    <ul>
      <#list group.dependencies as dependency>
        <li>
          <input type="checkbox" value="${dependency.id}" name="dependencies"/> ${dependency.name}<br/>
          ${dependency.description}
        </li>
      </#list>
    </ul>

  </#list>

</form>
</body>

</html>
