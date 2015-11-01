package com.juanpabloprado.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Juan on 10/31/2015.
 */
public class ImagesConfiguration extends Configuration {

  @NotEmpty
  private String dest = "/uploads";


  @JsonProperty
  public String getDest() {
    return dest;
  }

  @JsonProperty
  public void setDest(String name) {
    this.dest = name;
  }
}