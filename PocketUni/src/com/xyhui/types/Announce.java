package com.xyhui.types;

import android.text.TextUtils;

public class Announce {
	public String id;
	public String title;
	public String school;
	public String cid;
	public String time;
	public String desc;
	public String content;

	public String school2;

	public Announce() {

	}

	public String getCate() {
		final String[] cate = { "", "学校发文", "学校通知", "院系通知", "其他" };

		int cateid = 0;
		try {
			cateid = Integer.parseInt(cid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (cateid > cate.length) {
			cateid = 0;
		}

		String s2 = "";

		if (cateid == 3 && !TextUtils.isEmpty(school2)) {
			s2 = "    院系：" + school2;
		}
		return cate[cateid] + s2;
	}
}
