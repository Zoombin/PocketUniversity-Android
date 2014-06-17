package com.xyhui.activity.photo;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CActivity;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class AlbumListActivity extends FLActivity {

	private Button btn_back, btn_add;

	private PullToRefreshListView photo_listview;

	private String user_id;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.USER_ID)) {
			user_id = getIntent().getStringExtra(Params.INTENT_EXTRA.USER_ID);
			VolleyLog.d("got userid:%s", user_id);
		}

		if (TextUtils.isEmpty(user_id)) {
			VolleyLog.d("no userid and user_name");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_photo_albumlist);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_add = (Button) findViewById(R.id.btn_add);
		photo_listview = (PullToRefreshListView) findViewById(R.id.photo_listview);
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
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewAlbum();
			}
		});
	}

	public void addNewAlbum() {
		LinearLayout layout = new LinearLayout(mActivity);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText et_name = new EditText(mActivity);
		et_name.setHint("请输入相册名字");
		layout.addView(et_name);
		final Spinner sp_privacy = new Spinner(mActivity);
		{
			// 绑定数据
			// 建立数据源
			String[] mItems = getResources()
					.getStringArray(R.array.privacylist);
			// 建立Adapter并且绑定数据源
			ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, mItems);
			// 绑定 Adapter到控件
			sp_privacy.setAdapter(_Adapter);
		}
		layout.addView(sp_privacy);
		final EditText et_privacy = new EditText(mActivity);
		et_privacy.setHint("请输入访问密码");
		layout.addView(et_privacy);

		sp_privacy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				et_privacy.setVisibility(position == 3 ? View.VISIBLE
						: View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		new AlertDialog.Builder(mActivity).setView(layout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (et_name.getText().toString().trim().equals("")) {
							NotificationsUtil
									.ToastTopMsg(mActivity, "请输入相册名字！");
							return;
						}
						if (sp_privacy.getSelectedItemPosition() == 3) {
							if (et_privacy.getText().toString().trim()
									.equals("")) {
								NotificationsUtil.ToastTopMsg(mActivity,
										"请输入访问密码！");
								return;
							}
						}
						showProgress();
						// 添加相册
						new Api(albumCallBack, mActivity).addAlbum(PuApp.get()
								.getToken(), et_name.getText().toString()
								.trim(),
								sp_privacy.getSelectedItemPosition() + 1,
								user_id, et_privacy.getText().toString().trim());
					}
				}).setNegativeButton("取消", null).setTitle("添加相册").show();
	}

	CallBack albumCallBack = new CallBack() {
		@Override
		public void onSuccess(String response) {
			super.onSuccess(response);
			dismissProgress();

			try {
				JSONObject jsonObject = new JSONObject(response);
				int status = jsonObject.optInt("status");
				if (status == 1) {
					// 成功
					NotificationsUtil.ToastTopMsg(mActivity, "操作成功！");
				} else {
					// 失败
					NotificationsUtil.ToastTopMsg(mActivity,
							jsonObject.optString("msg"));
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			{
				Intent intent = new Intent(Params.INTENT_ACTION.ALBUMLIST);
				sendBroadcast(intent);
			}
		}

		public void onFailure(String message) {
			super.onFailure(message);
			dismissProgress();
		};
	};

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.ALBUMLIST);
		registerReceiver(mEvtReceiver, filter);
		mAlbumList = new AlbumList(photo_listview, mActivity, user_id);
	}

	private AlbumList mAlbumList;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.ALBUMLIST)) {
				if (mAlbumList != null) {
					mAlbumList.refreshListViewStart();
				}
			}
		}
	};
}
