package com.xyhui.types;

import android.text.TextUtils;

public class GroupMember {
	public String uid;
	public String name;
	public String status;
	public String level;
	public String face;
	public String user_school1;
	public String user_school2;
	public String user_mobile;
	public String remark;

	public static final String GROUP_CREATOR = "1";
	public static final String GROUP_MANAGER = "2";

	public String getLevel() {
		if (isCreator()) {
			return "部落主席";
		} else if (isManager()) {
			return "管理者";
		}
		return "成员";
	}

	public boolean isCreator() {
		return GROUP_CREATOR.equalsIgnoreCase(level);
	}

	public boolean isManager() {
		return GROUP_MANAGER.equalsIgnoreCase(level);
	}

	public String getInfo() {
		String result = "";
		if (!TextUtils.isEmpty(user_school1)) {
			result += user_school1 + " ";
		}
		if (!TextUtils.isEmpty(user_school2)) {
			result += user_school2 + " ";
		}
		if (!TextUtils.isEmpty(user_mobile)) {
			result += user_mobile;
		}
		return result;

	}

}
