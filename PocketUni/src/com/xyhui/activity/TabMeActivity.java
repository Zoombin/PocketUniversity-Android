package com.xyhui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.app.NavMapListActivity;
import com.xyhui.activity.app.NoticeListActivity;
import com.xyhui.activity.event.EventListActivity;
import com.xyhui.activity.group.GroupListActivity;
import com.xyhui.activity.more.AccountManageActivity;
import com.xyhui.activity.weibo.MessageActivity;
import com.xyhui.activity.weibo.WeiboList;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.User;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
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
	private WeiboList mWeiboListView;

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
		showProgress();
		index_banner.init();

		new Api(headercallback, mActivity).myinfo(PuApp.get().getToken());

		mWeiboListView = new WeiboList(weibo_listview, mActivity,
				WeiboList.TIMELINE);
		mWeiboListView.setTopicName("");
	}

	@Override
	protected void onResume() {
		super.onResume();

		index_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		index_banner.pause();
	}

	CallBack headercallback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			User mUser = JSONUtils.fromJson(response, User.class);

			if (mUser != null) {
				UrlImageViewHelper.setUrlDrawable(iv_userheader, mUser.face,
						R.drawable.img_default);
			}
		}
	};
}
