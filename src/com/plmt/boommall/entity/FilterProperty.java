package com.plmt.boommall.entity;

import java.io.Serializable;

public class FilterProperty implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String title;
	
	private String content;
	
	private String url;

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final String getUrl() {
		return url;
	}

	public final void setUrl(String url) {
		this.url = url;
	}

}
