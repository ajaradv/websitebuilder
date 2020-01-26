package com.websitebuilder.apijava.dto;

import lombok.Data;

@Data
public class Greeting {

  private final long id;
  private final String greeting;

  public Greeting (long id, String greeting) {
    this.id = id;
    this.greeting = greeting;
  }

}
