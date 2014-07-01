package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;

public class Dynamic implements Parcelable {
	public String id;
	public String uid;
	public String content;
	public String ctime;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(uid);
		out.writeString(content);
		out.writeString(ctime);
	}

	public Dynamic() {

	}

	public Dynamic(Parcel in) {
		uid = in.readString();
		content = in.readString();
		ctime = in.readString();
	}

	public static final Parcelable.Creator<Dynamic> CREATOR = new Parcelable.Creator<Dynamic>() {
		public Dynamic createFromParcel(Parcel in) {
			return new Dynamic(in);
		}

		@Override
		public Dynamic[] newArray(int size) {
			return new Dynamic[size];
		}
	};
}
