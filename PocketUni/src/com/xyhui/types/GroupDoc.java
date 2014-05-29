package com.xyhui.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.xyhui.utils.SpanUtils;

public class GroupDoc implements Parcelable {

	public String id;
	public String uid;
	public String name;
	public String note;
	public String filesize;
	public String filetype;
	public String fileurl;
	public String totaldowns;
	public String ctime;
	public String attachId;

	public String getDesc() {

		long filelength = 0;

		try {
			filelength = Long.parseLong(filesize);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "大小：" + SpanUtils.FormetFileSize(filelength) + " 下载：" + totaldowns + "次 \n上传时间："
				+ SpanUtils.getTimeString(ctime);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeString(id);
		out.writeString(uid);
		out.writeString(name);
		out.writeString(note);
		out.writeString(filesize);
		out.writeString(filetype);
		out.writeString(fileurl);
		out.writeString(totaldowns);
		out.writeString(ctime);
		out.writeString(attachId);

	}

	public GroupDoc() {
	}

	public GroupDoc(Parcel in) {

		id = in.readString();
		uid = in.readString();
		name = in.readString();
		note = in.readString();
		filesize = in.readString();
		filetype = in.readString();
		fileurl = in.readString();
		totaldowns = in.readString();
		ctime = in.readString();
		attachId = in.readString();
	}

	public static final Parcelable.Creator<GroupDoc> CREATOR = new Parcelable.Creator<GroupDoc>() {
		public GroupDoc createFromParcel(Parcel in) {
			return new GroupDoc(in);
		}

		@Override
		public GroupDoc[] newArray(int size) {
			return new GroupDoc[size];
		}
	};
}
