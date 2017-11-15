package fr.blossom.core.common.search;

import com.google.common.base.Preconditions;

public class SummaryDTO {
    private Long id;
    private String name;
    private String uri;
    private String description;

    public SummaryDTO(Long id, String name, String uri, String description) {
      this.id = id;
      this.name = name;
      this.uri= uri;
      this.description = description;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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
      this.uri=uri;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }



  public static class SummaryDTOBuilder {

    private Long id;
    private String name;
    private String description;
    private String uri;

    private SummaryDTOBuilder() {}

    public static SummaryDTOBuilder create(){
      return new SummaryDTOBuilder();
    }

    public SummaryDTOBuilder setId(Long id) {
      this.id = id;
      return this;
    }

    public SummaryDTOBuilder setName(String name) {
      this.name = name;
      return this;
    }

    public SummaryDTOBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    public SummaryDTOBuilder setUri(String uri) {
      this.uri = uri;
      return this;
    }

    public SummaryDTO build() {
      Preconditions.checkNotNull(id);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(uri);

      return new SummaryDTO(id, name, uri, description);
    }

  }
}
