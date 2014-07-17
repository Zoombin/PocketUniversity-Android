package com.xyhui.types;

public class WeiboTopic {
	public String name;
	public String count;

	public String getTopic() {
		return name + " (" + count + ") ";
	}
}
