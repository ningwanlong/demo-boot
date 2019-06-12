package com.boot.demo.tools;

public class ResultModel {

	private ResultCode resultCode;

	private String msg;
	
	public ResultModel() {
		this.resultCode = ResultCode.SUCCESS;
		this.msg = resultCode.getMsg();
	}

	public int getCode() {
		return resultCode.getCode();
	}

	public String getMsg() {
		return msg;
	}

	public void add(String msg) {
		this.resultCode = ResultCode.EXCEPTION;
		this.msg = msg;
	}
	public void addSuccess(String msg) {
		this.resultCode = ResultCode.SUCCESS;
		this.msg = msg;
	}

	public void add(ResultCode resultCode) {
		this.resultCode = resultCode;
		this.msg = resultCode.getMsg();
	}
}
