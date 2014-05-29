package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Maplist implements Parcelable {
	public String id;
	public String title;
	public String school;
	public String uid;
	public String ctime;
	public String desc;
	public String sort;

	public String lat;
	public String lng;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(title);
		out.writeString(school);
		out.writeString(uid);
		out.writeString(ctime);
		out.writeString(desc);
		out.writeString(lat);
		out.writeString(lng);
	}

	public Maplist() {
	}

	public Maplist(Parcel in) {
		id = in.readString();
		title = in.readString();
		school = in.readString();
		uid = in.readString();
		ctime = in.readString();
		desc = in.readString();
		lat = in.readString();
		lng = in.readString();
	}

	public static final Parcelable.Creator<Maplist> CREATOR = new Parcelable.Creator<Maplist>() {
		public Maplist createFromParcel(Parcel in) {
			return new Maplist(in);
		}

		@Override
		public Maplist[] newArray(int size) {
			return new Maplist[size];
		}
	};
}
