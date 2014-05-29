package com.xyhui.types;

import com.google.gson.JsonElement;

public class WeiboComment {
	public String comment_id;
	public String uid;
	public String content;
	public String ctime;
	public String reply_comment_id;
	public String type;
	public String isdel;
	public String uname;
	public User user;
	public String timestamp;

	public JsonElement status;
}
