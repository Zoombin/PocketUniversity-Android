package com.xyhui.widget;

import java.util.ArrayList;

import android.content.Context;

import com.mslibs.widget.CPagerAdapter;
import com.mslibs.widget.CPagerItem;
import com.xyhui.types.AdBanner;
import com.xyhui.types.EventBanner;

public class BannerPagerAdapter extends CPagerAdapter {
	private ArrayList<? extends CPagerItem> mBannerPagerItems;

	public BannerPagerAdapter(Context context) {
		super(context);
	}

	@Override
	public Object getItemView(int position) {
		return mBannerPagerItems.get(position);
	}

	@Override
	public void setDataList(ArrayList<? extends Object> dataList) {
		super.setDataList(dataList);
		initPagerItems();
	}

	@SuppressWarnings("unchecked")
	private void initPagerItems() {
		if (null != getDataList() && getDataList().size() > 0) {
			if (getDataList().get(0) instanceof EventBanner) {
				mBannerPagerItems = new ArrayList<EventBannerPagerItem>();
				for (Object obj : getDataList()) {
					EventBannerPagerItem item = new EventBannerPagerItem(getContext());
					item.initItem(obj);
					((ArrayList<EventBannerPagerItem>) mBannerPagerItems).add(item);
				}
			} else if (getDataList().get(0) instanceof AdBanner) {
				mBannerPagerItems = new ArrayList<AdBannerPagerItem>();
				for (Object obj : getDataList()) {
					AdBannerPagerItem item = new AdBannerPagerItem(getContext());
					item.initItem(obj);
					((ArrayList<AdBannerPagerItem>) mBannerPagerItems).add(item);
				}
			} else if (getDataList().get(0) instanceof String) {
				mBannerPagerItems = new ArrayList<DonateBannerPagerItem>();
				for (Object obj : getDataList()) {
					DonateBannerPagerItem item = new DonateBannerPagerItem(getContext());
					item.initItem(obj);
					((ArrayList<DonateBannerPagerItem>) mBannerPagerItems).add(item);
				}
			}
		}
	}
}
