package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

public class QrCode implements Parcelable {
	public String id;
	public String title;

	public String code;

	// 0=关注用户 1=用户签到活动 2=管理员签到活动 3=其他
	public static final int ADDFRIEND = 0;
	public static final int EVENTCHECKIN = 1;
	public static final int ADMINEVENTCHECKIN = 2;
	public static final int OTHER = 3;

	public int type = OTHER;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(title);
		out.writeString(code);
		out.writeString(type + "");
	}

	public QrCode() {

	}

	public QrCode(Parcel in) {
		id = in.readString();
		title = in.readString();
		code = in.readString();
		type = Integer.parseInt(in.readString());
	}

	public static final Parcelable.Creator<QrCode> CREATOR = new Parcelable.Creator<QrCode>() {
		public QrCode createFromParcel(Parcel in) {
			return new QrCode(in);
		}

		@Override
		public QrCode[] newArray(int size) {
			return new QrCode[size];
		}
	};
}
