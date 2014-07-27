package com.xyhui.types;

import java.util.List;

import android.text.TextUtils;

public class EventBanner extends Banner {
//	{"id":"41280","adminCode":"FN41280","uid":"1448764","title":"\u6d4b\u8bd5\u6d3b\u52a87.26","sTime":"2014-07-26 13:29","eTime":"2014-12-31 13:29","sid":"-99",
//		"cid":"1","default_banner":"1","address":"\u82cf\u5dde\u5427","cost":"\u514d\u8d39","costExplain":"","contact":"","joinCount":"2","note":"0.0","noteUser":"0",
//		"startline":"1403242171","deadline":"2014-06-20 13:29 \u81f3 2014-12-26 13:29","is_school_event":"473","need_tel":"0","free_attend":"0",
//		"description":"\u6d4b\u8bd5\u7b7e\u5230\u529f\u80fd\uff01\uff01\uff01","isFinish":0,"showAttend":1,
//		"cover":"http:\/\/pocketuni.net\/data\/upthumb\/2014\/0726\/13\/53d33ce869c81_125x125f.png",
//		"banner":"http:\/\/pocketuni.net\/data\/upthumb\/2014\/0726\/13\/53d33ce869dd7_480x270f.jpg","isStart":0,"uname":"\u738b\u6653\u5cf0",
//		"uface":"http:\/\/pocketuni.net\/data\/uploads\/avatar\/1448764\/middle.jpg","sName":"\u53e3\u888b\u5927\u5b66","cName":"\u6587\u4f53\u827a\u672f","orga":"",
//		"hasJoin":1,"joinAudit":1,"canJoin":0,"hasNoted":0,"news":"0","photo":"0","hasFav":false,
	
//		"eventUser":[{"uid":"222526","realname":"yc_test","uface":"http:\/\/pocketuni.net\/data\/uploads\/avatar\/222526\/small.jpg"},
//		             {"uid":"1448764","realname":"\u738b\u6653\u5cf0","uface":"http:\/\/pocketuni.net\/data\/uploads\/avatar\/1448764\/small.jpg"}]}
	
	public int status = 1;
	public List<EUser> eventUser;
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
	public String joinAudit;
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
		
	public class EUser {
		public String uid;
		public String realname;
		public String uface;
		public String status;
	}
	
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

	public String getJoinAudit() {
		return joinAudit;
	}

	public void setJoinAudit(String joinAudit) {
		this.joinAudit = joinAudit;
	}
}
