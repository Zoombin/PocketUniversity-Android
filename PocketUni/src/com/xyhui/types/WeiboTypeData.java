package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WeiboTypeData implements Parcelable {
	public String thumburl;
	public String thumbmiddleurl;
	public String picurl;
	public int attach_id;

	@SerializedName("0")
	public WeiboTypeData data;

	public void fix() {
		if (data != null) {
			picurl = picurl + data.picurl;
			thumburl = thumburl + data.thumburl;
			thumbmiddleurl = thumbmiddleurl + data.thumbmiddleurl;
			data = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(thumburl);
		out.writeString(thumbmiddleurl);
		out.writeString(picurl);
	}

	public WeiboTypeData() {
	}

	public WeiboTypeData(Parcel in) {
		thumburl = in.readString();
		thumbmiddleurl = in.readString();
		picurl = in.readString();

	}

	public static final Parcelable.Creator<WeiboTypeData> CREATOR = new Parcelable.Creator<WeiboTypeData>() {
		public WeiboTypeData createFromParcel(Parcel in) {
			return new WeiboTypeData(in);
		}

		@Override
		public WeiboTypeData[] newArray(int size) {
			return new WeiboTypeData[size];
		}
	};
}
