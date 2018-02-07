package com.blossom_project.core.common.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class SummaryDTO {
  private Long id;
  private String type;
  private String name;
  private String uri;
  private String description;

  @JsonCreator
  public SummaryDTO(@JsonProperty("id") Long id, @JsonProperty("type") String type,@JsonProperty("name") String name, @JsonProperty("uri") String uri, @JsonProperty("description") String description) {
    this.id = id;
    this.type=type;
    this.name = name;
    this.uri = uri;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public static class SummaryDTOBuilder {
    private Long id;
    private String type;
    private String name;
    private String description;
    private String uri;

    private SummaryDTOBuilder() {
    }

    public static SummaryDTOBuilder create() {
      return new SummaryDTOBuilder();
    }

    public SummaryDTOBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public SummaryDTOBuilder type(String type) {
      this.type = type;
      return this;
    }

    public SummaryDTOBuilder name(String name) {
      this.name = name;
      return this;
    }

    public SummaryDTOBuilder description(String description) {
      this.description = description;
      return this;
    }

    public SummaryDTOBuilder uri(String uri) {
      this.uri = uri;
      return this;
    }

    public SummaryDTO build() {
      Preconditions.checkNotNull(id);
      Preconditions.checkNotNull(type);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(uri);

      return new SummaryDTO(id, type, name, uri, description);
    }

  }
}
