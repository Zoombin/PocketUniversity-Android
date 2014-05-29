package com.xyhui.types;

import com.xyhui.utils.SpanUtils;

public class GroupTopic {

	public String id;
	public String uid;
	public String name;
	public String title;
	public String cid;
	public String viewcount;
	public String replycount;
	public String replytime;

	public String getDesc() {
		return "浏览：" + viewcount + "  回复：" + replycount + "  作者：" + name + "\n回复时间："
				+ SpanUtils.getTimeString(replytime);
	}
}
