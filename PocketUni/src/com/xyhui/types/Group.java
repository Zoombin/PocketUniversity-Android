package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {
	public String id;
	public String name;
	public String logo;
	public String membercount;
	public String ctime;
	public String cid0;
	public String cid1;
	public String cname0;
	public String cname1;
	public String schoolname;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(name);
		out.writeString(logo);
		out.writeString(membercount);
		out.writeString(ctime);
		out.writeString(cid0);
		out.writeString(cid1);
		out.writeString(cname0);
		out.writeString(cname1);
		out.writeString(schoolname);
	}

	public Group() {

	}

	public Group(Parcel in) {
		id = in.readString();
		name = in.readString();
		logo = in.readString();
		membercount = in.readString();
		ctime = in.readString();
		cid0 = in.readString();
		cid1 = in.readString();
		cname0 = in.readString();
		cname1 = in.readString();
		schoolname = in.readString();
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		public Group createFromParcel(Parcel in) {
			return new Group(in);
		}

		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	};

}
