package com.xyhui.activity.app;

import android.view.View;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class GiftListActivity extends FLActivity {
	
	private PullToRefreshListView gift_listview;
	private GiftList giftlist;
	private Button btn_back;
	
	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_gift_list);
		
		gift_listview = (PullToRefreshListView) findViewById(R.id.gift_listview);
		
		giftlist = new GiftList(gift_listview, mActivity);
		
		btn_back= (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}