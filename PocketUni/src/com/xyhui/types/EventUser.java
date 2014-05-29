package com.xyhui.types;

import android.text.TextUtils;

public class EventUser {
	public String id;
	public String realname;
	public String sex;
	public String path;

	public String ticket;
	public String school;
	public String sid;
	public Boolean canVote;

	public String getInfo() {
		String result = "";
		// if (!TextUtils.isEmpty(sex)) {
		// result += "性别：";
		// if ("1".equalsIgnoreCase(sex)) {
		// result += "男";
		// } else {
		// result += "女";
		// }
		// result += "\n";
		// }
		if (!TextUtils.isEmpty(school)) {
			result += "学校：";
			result += school;
			result += "\n";
		}
		return result;
	}

	public EventUser() {

	}
}
