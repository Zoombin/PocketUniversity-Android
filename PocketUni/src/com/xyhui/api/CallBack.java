package com.xyhui.api;

public class CallBack {
	private Object extra;

	public CallBack() {

	}

	public void onSuccess(String response) {

	}

	public void onFailure(String message) {

	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
}
