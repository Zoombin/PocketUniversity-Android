package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtraMsg implements Parcelable {
	public String type;
	public String listid;
	public String uid;
	public String uname;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(type);
		out.writeString(listid);
		out.writeString(uid);
		out.writeString(uname);
	}

	public ExtraMsg(Parcel in) {
		type = in.readString();
		listid = in.readString();
		uid = in.readString();
		uname = in.readString();
	}

	public ExtraMsg() {

	}

	public static final Parcelable.Creator<ExtraMsg> CREATOR = new Parcelable.Creator<ExtraMsg>() {
		public ExtraMsg createFromParcel(Parcel in) {
			return new ExtraMsg(in);
		}

		@Override
		public ExtraMsg[] newArray(int size) {
			return new ExtraMsg[size];
		}
	};
}