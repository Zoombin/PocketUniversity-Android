package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;

public class Weibo implements Parcelable {
	public String weibo_id;
	public String uid;
	public String content;
	public String ctime;
	public String from;
	public String comment;
	public String transpond_id;
	public String transpond;
	public String type;
	public WeiboTypeData type_data;
	public JsonElement from_data;
	public int favorited;
	public String isdel;
	public String uname;
	public String face;
	public JsonElement transpond_data;
	public String timestamp;
	public String heart;
	public int is_hearted;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(weibo_id);
		out.writeString(uid);
		out.writeString(content);
		out.writeString(ctime);
		out.writeString(from);
		out.writeString(comment);
		out.writeString(transpond_id);
		out.writeString(transpond);
		out.writeString(is_hearted + "");
		out.writeString(heart);
		out.writeString(type);
		out.writeString(favorited + "");
		out.writeString(isdel);
		out.writeString(uname);
		out.writeString(face);
		out.writeString(timestamp);
	}

	public Weibo() {

	}

	public Weibo(Parcel in) {
		weibo_id = in.readString();
		uid = in.readString();
		content = in.readString();
		ctime = in.readString();
		from = in.readString();
		comment = in.readString();
		transpond_id = in.readString();
		transpond = in.readString();
		is_hearted = Integer.parseInt(in.readString());
		heart = in.readString();
		type = in.readString();
		favorited = Integer.parseInt(in.readString());
		isdel = in.readString();
		uname = in.readString();
		face = in.readString();
		timestamp = in.readString();
	}

	public static final Parcelable.Creator<Weibo> CREATOR = new Parcelable.Creator<Weibo>() {
		public Weibo createFromParcel(Parcel in) {
			return new Weibo(in);
		}

		@Override
		public Weibo[] newArray(int size) {
			return new Weibo[size];
		}
	};
}
