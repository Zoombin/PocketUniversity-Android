package com.xyhui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.weibo.WeiboEditActivity;
import com.xyhui.activity.weibo.WeiboList;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.User;
import com.xyhui.utils.Params;
import com.xyhui.widget.EventBannerLayout;
import com.xyhui.widget.FLTabActivity;

public class TabMeActivity extends FLTabActivity {

	private EventBannerLayout index_banner;

	private ImageView iv_userheader;
	private TextView tv_user;
	private Button btn_pujie;
	private Button btn_wuliao;
	private Button btn_status;

	private PullToRefreshListView weibo_listview;
	private WeiboList mWeiboListView;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tab_me);

		index_banner = (EventBannerLayout) findViewById(R.id.index_banner);
		iv_userheader = (ImageView) findViewById(R.id.iv_userheader);
		tv_user = (TextView) findViewById(R.id.tv_user); 
		btn_pujie = (Button) findViewById(R.id.btn_pujie);
		btn_wuliao = (Button) findViewById(R.id.btn_wuliao);
		btn_status = (Button) findViewById(R.id.btn_status);
		weibo_listview = (PullToRefreshListView) findViewById(R.id.weibo_listview);
	}

	@Override
	public void bindListener() {
		btn_pujie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabMeActivity.this, TabFriendActivity.class);
				startActivity(intent);
			}
		});

		btn_wuliao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

		btn_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TabMeActivity.this, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_NEW);
				startActivity(intent);
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
				tv_user.setText(mUser.uname);
				UrlImageViewHelper.setUrlDrawable(iv_userheader, mUser.face,
						R.drawable.img_default);
			}
			
			if (mWeiboListView == null) {
				mWeiboListView = new WeiboList(weibo_listview, mActivity, WeiboList.TIMELINE);
			} else {
				mWeiboListView.refreshListViewStart();
			}
		}
	};
}
