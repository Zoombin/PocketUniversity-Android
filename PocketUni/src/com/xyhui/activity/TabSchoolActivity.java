package com.xyhui.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.xyhui.R;
import com.xyhui.activity.app.DonateListActivity;
import com.xyhui.activity.app.NavMapListActivity;
import com.xyhui.activity.event.EventListActivity;
import com.xyhui.activity.group.GroupListActivity;
import com.xyhui.types.Banner;
import com.xyhui.widget.AdBannerLayout;
import com.xyhui.widget.FLTabActivity;

public class TabSchoolActivity extends FLTabActivity {
	public final static String SUZHOU_CITY_ID = "1";
	public final static String VOLUNTEER_PKG_NAME = "com.lifeyoyo.volunteer.pu";
	public final static String MIAOPAI_DOWNLOAD_URL = "http://storage.video.sina.com.cn/apk/140226_wostore.apk?qq-pf-to=pcqq.c2c";
	public final static String MIAOPAI_PKG_NAME = "com.yixia.videoeditor";

	private ImageButton btn_school_bl;
	private ImageButton btn_hd;
	private ImageButton btn_school_dh;
	private ImageButton btn_school_weibo;

	private AdBannerLayout ad_banner;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tab_school);

		btn_school_bl = (ImageButton) findViewById(R.id.btn_school_bl);
		btn_hd = (ImageButton) findViewById(R.id.btn_hd);
		btn_school_dh = (ImageButton) findViewById(R.id.btn_school_dh);
		btn_school_weibo = (ImageButton) findViewById(R.id.btn_school_weibo);
		
		ad_banner = (AdBannerLayout) findViewById(R.id.ad_banner);
	}

	@Override
	public void bindListener() {
		btn_school_bl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开部落
				Intent intent = new Intent(mActivity, GroupListActivity.class);
				startActivity(intent);
			}
		});

		btn_hd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开活动
				Intent intent = new Intent(mActivity, EventListActivity.class);
				startActivity(intent);
			}
		});
		
		btn_school_dh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开导航
				Intent intent = new Intent(mActivity, NavMapListActivity.class);
				startActivity(intent);
			}
		});
		
		btn_school_weibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开发微博
				Intent intent = new Intent(mActivity, TabWeiboActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		ad_banner.init(Banner.TYPE_APP);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ad_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ad_banner.pause();
	}
}
