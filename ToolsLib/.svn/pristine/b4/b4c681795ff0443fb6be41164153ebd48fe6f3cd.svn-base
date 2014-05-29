package com.mslibs.widget;

import android.view.View.OnClickListener;

public class CListViewParam {

	private int mItemRsID; // 单个view的id
	private Object mDate; // 付值数据
	private boolean mVisibility = true; // 单个可见设置参数
	private boolean mIsSetVisibility; // 是否设置可见参数

	private OnClickListener mOnClickListener; // 单个view点击事件
	private Object mTag;// 付给view.tag
	private boolean mIsImgAsync;// 是否异步读取图片
	private int mImgRoundCorner;

	public CListViewParam(int ItemRsID, Object Date, Boolean Visibility) {
		mItemRsID = ItemRsID;
		mDate = Date;
		mVisibility = Visibility;
		mIsSetVisibility = true;
	}

	public CListViewParam(int ItemRsID, Object Date) {
		mItemRsID = ItemRsID;
		mDate = Date;
		mIsSetVisibility = false;
	}

	public void setItemTag(Object tag) {
		mTag = tag;
	}

	public void setImgAsync(boolean b) {
		mIsImgAsync = b;
	}

	public boolean getImgAsync() {
		return mIsImgAsync;
	}

	public Object getItemTag() {
		return mTag;
	}

	public int getItemRsID() {
		return mItemRsID;
	}

	public Object getDate() {
		return mDate;
	}

	public boolean getVisibility() {
		return mVisibility;
	}

	public boolean isSetVisibility() {
		return mIsSetVisibility;
	}

	public OnClickListener getOnclickListner() {
		return mOnClickListener;
	}

	public void setOnclickLinstener(OnClickListener onClickListener) {
		mOnClickListener = onClickListener;
	}

	public int getImgRoundCorner() {
		return mImgRoundCorner;
	}

	public void setImgRoundCorner(int imgRoundCorner) {
		mImgRoundCorner = imgRoundCorner;
	}
}
