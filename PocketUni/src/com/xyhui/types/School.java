package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class School implements Parcelable {
	public String school;
	public String name;
	public String email;
	public String cityId;
	public String id;
	public String display_order;

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(school);
		out.writeString(name);
		out.writeString(email);
		out.writeString(cityId);
		out.writeString(id);
		out.writeString(display_order);
	}

	public School() {
	}

	public School(Parcel in) {

		school = in.readString();
		name = in.readString();
		email = in.readString();
		cityId = in.readString();
		id = in.readString();
		display_order = in.readString();
	}

	public static final Parcelable.Creator<School> CREATOR = new Parcelable.Creator<School>() {
		public School createFromParcel(Parcel in) {
			return new School(in);
		}

		@Override
		public School[] newArray(int size) {
			return new School[size];
		}
	};
}
