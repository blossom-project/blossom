package fr.mgargadennec.blossom.core.common.web.resources;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class BlossomResourceWithEmbeds<T> extends Resource<T> {

  @JsonUnwrapped
  private Resources<EmbeddedWrapper> embeds;

  public BlossomResourceWithEmbeds(T content, Link[] links, Resources embeds) {
    super(content, links);
    this.embeds = embeds;
  }

  public Resources<EmbeddedWrapper> getEmbeds() {
    return embeds;
  }

  public void setEmbeds(Resources<EmbeddedWrapper> embeds) {
    this.embeds = embeds;
  }

}
