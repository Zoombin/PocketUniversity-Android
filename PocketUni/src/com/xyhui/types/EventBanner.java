package com.xyhui.types;

import android.text.TextUtils;

public class EventBanner extends Banner {
	public int status = 1;
	public String msg;
	public String id;
	public String title;
	public String banner;
	public String sTime;
	public String eTime;
	public String uid;
	public String sid;
	public String cid;
	public String joinCount;
	public String note;
	public String noteUser;
	public String isTop;
	public String cover;
	public String uname;
	public String sName;
	public String cName;
	public int hasJoin;
	public int canJoin;
	public int hasNoted;
	public int showAttend;
	public int need_tel;
	public String isStart;
	public String adminCode;

	public String is_school_event = "0";

	public String address;
	public String cost;
	public String costExplain;
	public String deadline;
	public String contact;
	public String uface;
	public String orga;
	public String news;
	public String photo;
	public String description;

	public boolean hasFav = false;

	public String getTips() {
		return "活动时间：" + sTime + " 至 " + eTime + "\n发起人：" + uname + "\n参加人数（" + joinCount + "）"+"\n活动评分："+note+"分";
	}

	public String getNotice() {
		String result = "";
		if (!TextUtils.isEmpty(address)) {
			result += "活动地点：" + address;
		}
		if (!TextUtils.isEmpty(sTime) && !TextUtils.isEmpty(eTime)) {
			result += "\n活动时间：" + sTime + " 至 " + eTime;
		}
		if (!TextUtils.isEmpty(deadline)) {
			result += "\n报名截止时间：" + deadline;
		}
		if (!TextUtils.isEmpty(cost)) {
			result += "\n活动经费：";
			if ("0".equalsIgnoreCase(cost)) {
				result += "免费";
			} else {
				result += cost;
			}
			if (!TextUtils.isEmpty(costExplain)) {
				result += "(" + costExplain + ")";
			}
		}
		if (!TextUtils.isEmpty(note)) {
			result += "\n活动评分：" + note + "分";
		}
		if (!TextUtils.isEmpty(description)) {
			result += "\n活动简介：" + description;
		}

		if (!TextUtils.isEmpty(contact)) {
			result += "\n联系方式：" + contact;
		}

		return result;
	}

	public EventBanner() {

	}
}
