package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
	public String id;
	public String userId;
	public String name;
	public String mTime;
	public String photoCount;
	public String privacy;
	public String cover;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(userId);
		out.writeString(name);
		out.writeString(mTime);
		out.writeString(photoCount);
		out.writeString(privacy);
		out.writeString(cover);
	}

	public Album() {

	}

	public Album(Parcel in) {
		id = in.readString();
		userId = in.readString();
		name = in.readString();
		mTime = in.readString();
		photoCount = in.readString();
		privacy = in.readString();
		cover = in.readString();
	}

	public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
		public Album createFromParcel(Parcel in) {
			return new Album(in);
		}

		@Override
		public Album[] newArray(int size) {
			return new Album[size];
		}
	};
}
