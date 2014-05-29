package com.xyhui.types;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class DetailGroup implements Parcelable {

	public String id;
	public String uid;
	public String name;
	public String intro;

	public String logo;
	public String announce;
	public String membercount;
	public String threadcount;
	public String type = null;
	public String need_invite;
	public String need_verify;
	public String actor_level;
	public String brower_level;
	public String openWeibo;
	public String openBlog;
	public String openUploadFile;
	public String whoUploadFile;
	public String whoDownloadFile;
	public String openAlbum;
	public String whoCreateAlbum;
	public String whoUploadPic;
	public String anno;
	public String ipshow;
	public String invitepriv;

	public String createalbumpriv;
	public String uploadpicpriv;
	public String ctime;
	public String mtime;
	public String status;
	public String isrecom;
	public String is_del;
	public String cname0;
	public String cname1;
	public String schoolname;
	public String cid0;
	public String cid1;
	public String type_name;

	public ArrayList<Tag> tags;

	public String filecount;
	public String topiccount;
	public String usrename;

	public String tagstring;

	public int isinvited;
	public int ismember;
	public int isadmin;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(name);
		out.writeString(intro);
		out.writeString(logo);
		out.writeString(cname0);
		out.writeString(cid0);
		out.writeString(TextUtils.join(" , ", tags));
	}

	public DetailGroup() {
	}

	public DetailGroup(Parcel in) {
		id = in.readString();
		name = in.readString();
		intro = in.readString();
		logo = in.readString();
		cname0 = in.readString();
		cid0 = in.readString();
		tagstring = in.readString();
	}

	public static final Parcelable.Creator<DetailGroup> CREATOR = new Parcelable.Creator<DetailGroup>() {
		public DetailGroup createFromParcel(Parcel in) {
			return new DetailGroup(in);
		}

		@Override
		public DetailGroup[] newArray(int size) {
			return new DetailGroup[size];
		}
	};

}
