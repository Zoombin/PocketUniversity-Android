package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
	public String id;
	public String city;

	@Override
	public String toString() {
		return city;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(city);
	}

	public City(Parcel in) {
		id = in.readString();
		city = in.readString();
	}

	public City() {

	}

	public City(String id, String city) {
		this.id = id;
		this.city = city;
	}

	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
		public City createFromParcel(Parcel in) {
			return new City(in);
		}

		@Override
		public City[] newArray(int size) {
			return new City[size];
		}
	};

}
