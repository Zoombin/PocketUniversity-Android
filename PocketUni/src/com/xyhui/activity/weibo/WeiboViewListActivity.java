package com.xyhui.activity.weibo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Weibo;
import com.xyhui.types.WeiboTypeData;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class WeiboViewListActivity extends FLActivity {
	private final String ZAN_SUCCESS = "1";

	private Button btn_back;
	private Button tabbtn_refresh;
	private Button tabbtn_reply;
	private Button tabbtn_forward;
	private Button tabbtn_favorite;
	private Button tabbtn_zan;
	private Button btn_delete;

	private PullToRefreshListView weibo_view_listview;
	private WeiboViewList mWeiboViewListView;

	private Weibo weibo;
	private WeiboTypeData type_data;
	private Weibo transpond_data;
	private WeiboTypeData transpond_type_data;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEIBO_FORWARD_DATA)) {
			transpond_type_data = getIntent().getParcelableExtra(
					Params.INTENT_EXTRA.WEIBO_FORWARD_DATA);
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEIBO_DATA)) {
			type_data = getIntent().getParcelableExtra(Params.INTENT_EXTRA.WEIBO_DATA);
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEIBO_FORWARD)) {
			transpond_data = getIntent().getParcelableExtra(Params.INTENT_EXTRA.WEIBO_FORWARD);
			transpond_data.type_data = transpond_type_data;
			VolleyLog.d("got WEIBO_FORWARD:%s", transpond_data.content);
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.WEIBO)) {
			weibo = getIntent().getParcelableExtra(Params.INTENT_EXTRA.WEIBO);
			weibo.type_data = type_data;
			VolleyLog.d("got weibo传入 %s", weibo.weibo_id);
		} else {
			VolleyLog.d("没有weibo传入");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_weibo_view);

		btn_back = (Button) findViewById(R.id.btn_back);

		btn_delete = (Button) findViewById(R.id.btn_delete);
		weibo_view_listview = (PullToRefreshListView) findViewById(R.id.weibo_view_listview);

		tabbtn_refresh = (Button) findViewById(R.id.tabbtn_refresh);
		tabbtn_reply = (Button) findViewById(R.id.tabbtn_reply);
		tabbtn_forward = (Button) findViewById(R.id.tabbtn_forward);
		tabbtn_favorite = (Button) findViewById(R.id.tabbtn_favorite);
		tabbtn_zan = (Button) findViewById(R.id.tabbtn_zan);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				finish();
			}
		});

		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(mActivity).setTitle("确定要删除吗?")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								new Api(delcallback, mActivity).delweibo(PuApp.get().getToken(),
										weibo.weibo_id);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						}).show();
			}
		});

		tabbtn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 刷新微博
				if (mWeiboViewListView != null) {
					mWeiboViewListView.refresh();
				}
			}
		});
		tabbtn_reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 评论微博
				Intent intent = new Intent(mActivity, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_REPLY);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_ID, weibo.weibo_id);
				startActivity(intent);
			}
		});
		tabbtn_forward.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 转发微博
				Intent intent = new Intent(mActivity, WeiboEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_FORWARD);
				intent.putExtra(Params.INTENT_EXTRA.WEIBO_ID, weibo.weibo_id);
				startActivity(intent);
			}
		});
		tabbtn_favorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 收藏微博
				tabbtn_favorite.setEnabled(false);
				Boolean selected = tabbtn_favorite.isSelected();

				if (selected) {
					new Api(favcallback, mActivity).unfav(PuApp.get().getToken(), weibo.weibo_id);
				} else {
					new Api(favcallback, mActivity).fav(PuApp.get().getToken(), weibo.weibo_id);
				}
			}
		});

		tabbtn_zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 赞微博
				tabbtn_zan.setEnabled(false);
				Boolean selected = tabbtn_zan.isSelected();

				if (selected) {
					new Api(zanCB, mActivity).delZan(PuApp.get().getToken(), weibo.weibo_id);
				} else {
					new Api(zanCB, mActivity).addZan(PuApp.get().getToken(), weibo.weibo_id);
				}
			}
		});
	}

	@Override
	public void ensureUi() {

		String uid = new PrefUtil().getPreference(Params.LOCAL.UID);

		if (!TextUtils.isEmpty(uid) && uid.equalsIgnoreCase(weibo.uid)) {
			btn_delete.setVisibility(View.VISIBLE);
		} else {
			btn_delete.setVisibility(View.GONE);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.WEIBOVIEW);
		registerReceiver(mEvtReceiver, filter);

		mWeiboViewListView = new WeiboViewList(weibo_view_listview, mActivity, weibo,
				transpond_data);

		if (weibo.favorited == 0) {
			tabbtn_favorite.setSelected(false);
		} else {
			tabbtn_favorite.setSelected(true);
		}

		if (weibo.is_hearted == 0) {
			tabbtn_zan.setSelected(false);
		} else {
			tabbtn_zan.setSelected(true);
		}
	}

	CallBack favcallback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			boolean selected = tabbtn_favorite.isSelected();

			if (selected) {
				tabbtn_favorite.setSelected(false);
			} else {
				tabbtn_favorite.setSelected(true);
			}
			tabbtn_favorite.setEnabled(true);
		}
	};

	CallBack zanCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			if (response.equals(ZAN_SUCCESS)) {
				Intent weiboList = new Intent().setAction(Params.INTENT_ACTION.WEIBOLIST);
				sendBroadcast(weiboList);
				Intent weiboView = new Intent().setAction(Params.INTENT_ACTION.WEIBOVIEW);
				sendBroadcast(weiboView);
				boolean selected = tabbtn_zan.isSelected();

				if (selected) {
					tabbtn_zan.setSelected(false);
					weibo.is_hearted = 0;
					weibo.heart = String.valueOf(Integer.parseInt(weibo.heart) - 1);
				} else {
					tabbtn_zan.setSelected(true);
					weibo.is_hearted = 1;
					weibo.heart = String.valueOf(Integer.parseInt(weibo.heart) + 1);
				}
				tabbtn_zan.setEnabled(true);
			}
		}
	};

	CallBack delcallback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			int result = 0;
			try {
				result = Integer.parseInt(response);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				NotificationsUtil.ToastBottomMsg(mActivity, "己删除");
				Intent intent = new Intent().setAction(Params.INTENT_ACTION.WEIBOLIST);
				sendBroadcast(intent);
				finish();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.WEIBOVIEW)) {
				if (mWeiboViewListView != null) {
					mWeiboViewListView.refresh();
				}
			}
		}
	};
}
