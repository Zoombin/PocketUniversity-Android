package com.xyhui.types;

public class Message2 {
	public String message_id;
	public String list_id;
	public String ctime;
	public String content;
	public String from_uname;
	public String from_face;
	public String timestmap;
	public String from_uid;

	public String getHumanReadableTime() {
		long second = System.currentTimeMillis() / 1000 - Integer.valueOf(timestmap);

		if (second < 5 * 60) {
			return "刚刚";
		} else if (second < 60 * 60) {
			return second / 60 + "分钟前";
		} else if (second < 60 * 60 * 24) {
			return second / (60 * 60) + "小时前";
		} else if (second < 60 * 60 * 24 * 7) {
			return second / (60 * 60 * 24) + "天前";
		} else {
			return ctime;
		}
	}
}
