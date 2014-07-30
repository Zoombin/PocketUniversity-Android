package com.xyhui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.weibo.WeiboTopicList;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.User;
import com.xyhui.widget.EventBannerLayout;
import com.xyhui.widget.FLTabActivity;

public class TabMeActivity extends FLTabActivity {

	private EventBannerLayout index_banner;

	private ImageView iv_userheader;
	private Button btn_pujie;
	private Button btn_wuliao;
	private Button btn_sixin;
	private Button btn_status;

	private PullToRefreshListView weibo_listview;
	private WeiboTopicList mWeiboListView;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tab_me);

		index_banner = (EventBannerLayout) findViewById(R.id.index_banner);
		iv_userheader = (ImageView) findViewById(R.id.iv_userheader);
		btn_pujie = (Button) findViewById(R.id.btn_pujie);
		btn_wuliao = (Button) findViewById(R.id.btn_wuliao);
		btn_sixin = (Button) findViewById(R.id.btn_sixin);
		btn_status = (Button) findViewById(R.id.btn_status);
		weibo_listview = (PullToRefreshListView) findViewById(R.id.weibo_listview);
	}

	@Override
	public void bindListener() {
		btn_pujie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		btn_wuliao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		btn_sixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		btn_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void ensureUi() {
		index_banner.init();
	}

	@Override
	protected void onResume() {
		super.onResume();


		
//		if (mWeiboListView == null) {
//		mWeiboListView = new WeiboTopicList(weibo_listview, mActivity );
//		weibo_listview.setVisibility(View.VISIBLE);
//	} else {
//		mWeiboListView.refreshListViewStart();
//	}

		index_banner.reload();
		
		IntentFilter filter = new IntentFilter("TabMeActivity");
		registerReceiver(broadcastreceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		index_banner.pause();
		unregisterReceiver(broadcastreceiver);
	}

	BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String key = intent.getStringExtra("key");
			if (key.equals("banner")) {
				new Api(headercallback, mActivity).myinfo(PuApp.get().getToken());
			}
		}
	};
	
	CallBack headercallback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			User mUser = JSONUtils.fromJson(response, User.class);

			if (mUser != null) {
				UrlImageViewHelper.setUrlDrawable(iv_userheader, mUser.face,
						R.drawable.img_default);
			}
		}
	};
}
