package com.mslibs.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.GridView;

public abstract class CGridView {

	public int actionType = IDLE;
	public static final int IDLE = 0;
	public static final int INIT = 1;
	public static final int REFRESH = 2;
	public static final int GETMORE = 3;

	// 基础变量
	private GridView mGridView;
	private CListViewAdapter mAdapter; // 适配器
	private ArrayList<ArrayList<CListViewParam>> mDateList; // 适配数据
	private ArrayList<Object> mListItems; // 原型数据
	private Context mContext; // 上下文

	// 页面控制变量
	private int mPerpage = 12;

	// 资源变量
	private int mListItemResource;
	private int mGetMoreResource;
	private int mGetMoreTitleRID;
	private String mGetMoreTitle = "";
	private int mHeaderResource;
	private int mFooterResource;
	private int mSingleResource;

	public abstract ArrayList<CListViewParam> matchListItem(Object obj, int index);

	public abstract void asyncData();

	public abstract void initListItemResource();

	public CGridView(GridView gv, Context context) {
		mGridView = gv;
		mContext = context;
		mListItems = new ArrayList<Object>();
		initListItemResource();
		ensureUi();
	}

	public int getPerPage() {
		return mPerpage;
	}

	public void setPerPage(int perPage) {
		mPerpage = perPage;
	}

	public Context getContext() {
		return mContext;
	}

	public ArrayList<Object> getListItems() {
		return mListItems;
	}

	public void setListItemResource(int listItemResource) {
		mListItemResource = listItemResource;
	}

	public void setGetMoreResource(int getMoreResource, int titleRID, String title) {
		mGetMoreResource = getMoreResource;
		mGetMoreTitleRID = titleRID;
		mGetMoreTitle = title;
	}

	public void setHeaderResource(int headerResource) {
		mHeaderResource = headerResource;
	}

	public void setFooterResource(int footerResource) {
		mFooterResource = footerResource;
	}

	public void setSingleResource(int singleResource) {
		mSingleResource = singleResource;
	}

	public void setItemOnclickLinstener(OnClickListener onClickListener) {
		mAdapter.setItemOnclickLinstener(onClickListener);
	}

	public void setGetMoreClickListener(OnClickListener onClickListener) {
		mAdapter.setGetMoreClickListener(onClickListener);
	}

	public void ensureUi() {

		mDateList = new ArrayList<ArrayList<CListViewParam>>();
		mAdapter = new CListViewAdapter(mContext, mListItemResource);

		mAdapter.setGetMoreResource(mGetMoreResource);
		mAdapter.setHeaderResource(mHeaderResource);
		mAdapter.setFooterResource(mFooterResource);
		mAdapter.setSingleResource(mSingleResource);

		mAdapter.setData(mDateList);

		mAdapter.ItemViewEmptyInvisible = true; // 隐藏空值的控件

		mGridView.setAdapter(mAdapter);
	}

	public void initListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = INIT;
		// 异步获取数据
		asyncData();
	}

	public void initListViewFinish() {

		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}
		setMoreLVP();
		mAdapter.notifyDataSetChanged();
	}

	public void refreshListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = REFRESH;
		// 异步获取数据
		asyncData();
	}

	public void refreshListViewFinish() {

		for (int i = 0; i < mListItems.size(); i++) {
			mDateList.add(matchListItem(mListItems.get(i), i));
		}
		setMoreLVP();
		mAdapter.notifyDataSetChanged();
		mGridView.setSelection(0);
	}

	public void getmoreListViewStart() {
		if (actionType != IDLE)
			return;
		actionType = GETMORE;

		// 异步获取数据
		asyncData();
	}

	public void getmoreListViewFinish() {
		for (int i = mDateList.size() - 1, m = mListItems.size(); i < m; i++) {
			mDateList.add(i, matchListItem(mListItems.get(i), i));
		}
		mAdapter.notifyDataSetChanged();
	}

	public void setMoreLVP() {
		if (mGetMoreResource == 0) {
			return;
		}

		if (mListItems.size() >= mPerpage) {
			ArrayList<CListViewParam> getMoreLVP = new ArrayList<CListViewParam>();
			getMoreLVP.add(new CListViewParam(mGetMoreTitleRID, mGetMoreTitle));
			mDateList.add(getMoreLVP);
		}
	}
}
