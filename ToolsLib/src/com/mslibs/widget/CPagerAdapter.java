package com.mslibs.widget;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public abstract class CPagerAdapter extends PagerAdapter {
	private Context mContext;
	private ArrayList<? extends Object> mDataList;

	private int mPagerCount;

	public abstract Object getItemView(int position);

	public CPagerAdapter(Context context) {
		super();
		mContext = context;
		mDataList = new ArrayList<Object>();
	}

	public Context getContext() {
		return mContext;
	}

	public ArrayList<? extends Object> getDataList() {
		return mDataList;
	}

	public void setDataList(ArrayList<? extends Object> dataList) {
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		if (mPagerCount > 0) {
			return mPagerCount;
		} else if (null == mDataList) {
			return 0;
		} else {
			return mDataList.size();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == (View) obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Object item = getItemView(position);
		((ViewPager) container).addView((View) item, 0);
		return item;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
