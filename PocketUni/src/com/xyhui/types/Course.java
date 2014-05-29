package com.xyhui.types;

import android.text.TextUtils;

public class Course {
	// 课程id、名称、图标
	public String id;
	public String title;
	public String logoId;

	// 课程分类id、分类名称
	public String sortId;
	public String sortName;

	// 授课老师名字
	public String teacher;

	// 课程开始、结束、报名截止时间
	public String sTime;
	public String eTime;
	public String deadline;

	// 上课地点
	public String address;

	// 学分
	public String credit;

	// 已参加人数
	public String joinCount;
	// 剩余名额
	public String limitCount;

	// 课程简介
	public String description;

	// 是否已参加
	public boolean hasJoined;

	// 是否已经结束
	public boolean isFinished;

	// 是否可报名
	public boolean isAvailable;

	// 是否需要绑定手机号码(0:不需要 1：需要)
	public int need_tel;

	public String getTips() {
		return "课程时间：" + sTime + " 至 " + eTime + "\n报名截止时间：" + deadline + "\n参加人数(" + joinCount
				+ ")  剩余名额 (" + limitCount + ")";
	}

	public String getNotice() {
		String result = "";

		if (!TextUtils.isEmpty(sortName)) {
			result += "课程分类：" + sortName;
		}

		if (!TextUtils.isEmpty(teacher)) {
			result += "\n授课老师：" + teacher;
		}

		if (!TextUtils.isEmpty(sTime) && !TextUtils.isEmpty(eTime)) {
			result += "\n上课时间：" + sTime + " 至 " + eTime;
		}

		if (!TextUtils.isEmpty(deadline)) {
			result += "\n报名截止时间：" + deadline;
		}

		if (!TextUtils.isEmpty(address)) {
			result += "\n上课地点：" + address;
		}

		if (!TextUtils.isEmpty(credit)) {
			result += "\n课时：";
			result += credit;
		}

		if (!TextUtils.isEmpty(joinCount)) {
			result += "\n已报名人数：" + joinCount;
		}

		if (!TextUtils.isEmpty(limitCount)) {
			result += "\n剩余名额：" + limitCount;
		}

		if (!TextUtils.isEmpty(description)) {
			result += "\n课程介绍：" + description;
		}

		return result;
	}

	public Course() {

	}
}
