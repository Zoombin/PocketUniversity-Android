package com.xyhui.api;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;

public class ListCallBack<T> extends CallBack {
	protected T mT;

	protected Type myType;

	protected CListView mListView;

	public ListCallBack(CListView listView) {
		mListView = listView;
	}

	public void setType() {

	}

	public String preProcess(String response) {
		return response;
	}

	public void addItems() {

	}

	@Override
	public void onSuccess(String response) {
		super.onSuccess(response);
		((CActivity) mListView.mActivity).dismissProgress();
		response = preProcess(response);

		setType();

		Gson gson = new Gson();
		try {
			mT = gson.fromJson(response, myType);
		} catch (JsonSyntaxException e) {
			VolleyLog.e(e, e.toString());
		}

		if (CListView.GETMORE != mListView.actionType) {
			mListView.mListItems.clear();
			mListView.mDateList.clear();
		}

		addItems();

		switch (mListView.actionType) {
		case CListView.INIT:
			mListView.initListViewFinish();
			break;
		case CListView.REFRESH:
			mListView.refreshListViewFinish();
			break;
		case CListView.GETMORE:
			mListView.getmoreListViewFinish();
			break;
		}

		mListView.actionType = CListView.IDLE;
	}

	@Override
	public void onFailure(String message) {
		((CActivity) mListView.mActivity).dismissProgress();
		mListView.actionType = CListView.IDLE;
	}
}
