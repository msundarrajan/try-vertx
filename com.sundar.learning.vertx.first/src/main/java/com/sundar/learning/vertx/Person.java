package com.sundar.learning.vertx;

import java.util.concurrent.atomic.AtomicInteger;

public class Person {
  private static final AtomicInteger COUNTER = new AtomicInteger();

  private final int id;

  private String name;

  private String origin;

  public Person(String name, String origin) {
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.origin = origin;
  }

  public Person() {
    this.id = COUNTER.getAndIncrement();
  }

  public String getName() {
    return name;
  }

  public String getOrigin() {
    return origin;
  }

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }
}
