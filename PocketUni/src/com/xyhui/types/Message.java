package com.xyhui.types;

import com.google.gson.annotations.SerializedName;

public class Message {
	public String message_id;
	public String list_id;
	public String member_uid;
	public String talk_uid;
	public String message_num;
	public String ctime;
	public String list_ctime;
	public String type;
	public String title;
	public String member_num;
	public String content;
	public String from_uname;
	public String talk_uname;
	public String from_face;
	public String timestmap;
	public int from_uid;
	public String uid;
	@SerializedName("new")
	public String isNew;

	public Message() {

	}
}
