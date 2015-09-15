package com.plmt.boommall.entity;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String ppid;

	private String ppmc;

	private String pplx;
	
	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public String getPpmc() {
		return ppmc;
	}

	public void setPpmc(String ppmc) {
		this.ppmc = ppmc;
	}

	public String getPplx() {
		return pplx;
	}

	public void setPplx(String pplx) {
		this.pplx = pplx;
	}

}
