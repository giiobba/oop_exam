package com.ticketmaster.api.discovery.response;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketmaster.discovery.model.Page;
import com.ticketmaster.discovery.model.Page.Link;
import com.ticketmaster.discovery.model.Page.PageInfo;

import java.io.IOException;

public class PagedResponse<T> extends Response<T> {

  private final JavaType javaType;
  private Page<T> page;

  public PagedResponse(okhttp3.Response httpResponse, ObjectMapper mapper, Class<T> type) {
    super(httpResponse, mapper, type);
    this.javaType = mapper.getTypeFactory().constructParametricType(Page.class, type);
  }

  protected void readContent() {
    // 202205 GP
    /***
    try {
      this.page = mapper.readValue(httpResponse.body().string(), javaType);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    this.content = page.getEmbedded();
    ***/
    try {
      super.readContent();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public T getContent() {
    // 202205 GP
    /***
    if (content == null) {
      readContent();
    }
    return content;
    ***/
    try {
      content = super.getContent();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return content;
  }

  // 202205 GP
  public Class<T> getType() { return type; }

  public Link getNextPageLink() {
    if (page == null) {
      readContent();
    }
    return page.getLinks().getNext();
  }

  public Link getPreviousPageLink() {
    if (page == null) {
      readContent();
    }
    return page.getLinks().getPrevious();
  }

  public PageInfo getPageInfo() {
    if (page == null) {
      readContent();
    }
    return page.getInfo();
  }

}
