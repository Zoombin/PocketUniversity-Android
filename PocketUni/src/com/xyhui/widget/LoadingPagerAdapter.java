package com.xyhui.widget;

import android.app.Activity;
import android.content.Context;

import com.mslibs.widget.CPagerAdapter;

public class LoadingPagerAdapter extends CPagerAdapter {

	public LoadingPagerAdapter(Activity activity, Context context) {
		super(context);
	}

	@Override
	public Object getItemView(int position) {
		return getDataList().get(position);
	}
}
