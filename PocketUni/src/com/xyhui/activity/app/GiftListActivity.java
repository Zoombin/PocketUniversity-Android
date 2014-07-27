package com.xyhui.activity.app;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class GiftListActivity extends FLActivity {
	
	private PullToRefreshListView gift_listview;
	private GiftList giftlist;
	
	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_gift_list);
		
		gift_listview = (PullToRefreshListView) findViewById(R.id.gift_listview);
		
		giftlist = new GiftList(gift_listview, mActivity);
	}
}