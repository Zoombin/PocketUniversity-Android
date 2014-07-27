package com.xyhui.types;

import android.text.TextUtils;

public class Gift {

	public String id;
	public String name;
	public String zjfrom;
	public String type;
	public String status;
	
	public String order_id;
	public String ctime;
	public String utime;
	public String gtime;
	public String description;
	public String content;

	public Gift() {

	}
	
	public String getNotice() {
		String result = "";

		if (!TextUtils.isEmpty(name)) {
			result += "奖品名称：" + name;
		}

		if (!TextUtils.isEmpty(zjfrom)) {
			result += "\n来源：" + zjfrom;
		}

		if (!TextUtils.isEmpty(type)) {
			result += "\n类型：" + type;
		}

		if (!TextUtils.isEmpty(status)) {
			result += "\n状态：" + status;
		}

		return result;
	}
	
	public String getGiftDetail() {
		String result = "";

		if (!TextUtils.isEmpty(name)) {
			result += "奖品名称：" + name;
		}

		if (!TextUtils.isEmpty(zjfrom)) {
			result += "\n来源：" + zjfrom;
		}

		if (!TextUtils.isEmpty(type)) {
			result += "\n类型：" + type;
		}

		if (!TextUtils.isEmpty(status)) {
			result += "\n状态：" + status;
		}

		if (!TextUtils.isEmpty(ctime)) {
			result += "\n中奖日期：" + ctime;
		}
		
		if (!TextUtils.isEmpty(gtime)) {
			result += "\n有效期：" + gtime;
		}
		
		if (!TextUtils.isEmpty(utime)) {
			result += "\n使用时间：" + utime;
		}
		
		if (!TextUtils.isEmpty(description)) {
			result += "\n奖品描述：" + description;
		}
		return result;
	}
}
