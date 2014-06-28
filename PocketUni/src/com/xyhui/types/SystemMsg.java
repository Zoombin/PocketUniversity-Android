package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;

public class SystemMsg implements Parcelable {
	
	public String id;
	public String from;
	public String face;
	public String is_read;
	public String ctime;
	public String title;
	public String body;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(from);
		out.writeString(face);
		out.writeString(is_read);
		out.writeString(ctime);
		out.writeString(title);
		out.writeString(body);
	}

	public SystemMsg() {

	}

	public SystemMsg(Parcel in) {
		id = in.readString();
		from = in.readString();
		face = in.readString();
		is_read = in.readString();
		ctime = in.readString();
		title = in.readString();
		body = in.readString();
	}

	public static final Parcelable.Creator<SystemMsg> CREATOR = new Parcelable.Creator<SystemMsg>() {
		public SystemMsg createFromParcel(Parcel in) {
			return new SystemMsg(in);
		}

		@Override
		public SystemMsg[] newArray(int size) {
			return new SystemMsg[size];
		}
	};
}
