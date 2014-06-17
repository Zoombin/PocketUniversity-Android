package com.xyhui.activity.photo;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class PhotoListActivity extends FLActivity {

	private Button btn_back, btn_menu;

	private GridView photo_listview;

	private String album_id;
	private String album_name;
	private String user_id;
	private String privacy;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.USER_ID)) {
			user_id = getIntent().getStringExtra(Params.INTENT_EXTRA.USER_ID);
			VolleyLog.d("got userid:%s", user_id);
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.PHOTO_PRIVACY)) {
			privacy = getIntent().getStringExtra(
					Params.INTENT_EXTRA.PHOTO_PRIVACY);
			VolleyLog.d("got privacy:%s", privacy);
		}
		if (getIntent().hasExtra(Params.INTENT_EXTRA.ALBUM_ID)) {
			album_id = getIntent().getStringExtra(Params.INTENT_EXTRA.ALBUM_ID);
			VolleyLog.d("got albumid:%s", album_id);
		}
		if (getIntent().hasExtra(Params.INTENT_EXTRA.ALBUM_NAME)) {
			album_name = getIntent().getStringExtra(
					Params.INTENT_EXTRA.ALBUM_NAME);
			VolleyLog.d("got albumid:%s", album_id);
		}

		if (TextUtils.isEmpty(user_id)) {
			VolleyLog.d("no userid and user_name");
			finish();
			return;
		}

	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_photo_album);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_menu = (Button) findViewById(R.id.btn_menu);
		photo_listview = (GridView) findViewById(R.id.event_photo_gridview);
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
		btn_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSelectDialog();
			}
		});
	}

	private void showSelectDialog() {
		Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("请选择");

		builder.setItems(R.array.photo_menu,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: {
							// 修改
							editNewAlbum();
							break;
						}
						case 1: {
							// 删除
							deleteAlbum();
							break;
						}
						case 2: {
							// 上传
							break;
						}

						}
						dialog.cancel();
					}
				}).show();
	}

	CallBack photoCallBack = new CallBack() {
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
					finish();
				} else {
					// 失败
					NotificationsUtil.ToastTopMsg(mActivity,
							jsonObject.optString("msg"));
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			// 通知更新
			{
				Intent intent = new Intent(Params.INTENT_ACTION.PHOTOLIST);
				sendBroadcast(intent);
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
	
	public void deleteAlbum(){
		new AlertDialog.Builder(mActivity).setTitle("删除相册").setMessage("是否删除确定删除相册").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Api(photoCallBack, mActivity).delAlbum(PuApp.get()
						.getToken(), album_id);
			}
		}).setNegativeButton("取消", null).show();
	}

	public void editNewAlbum() {
		LinearLayout layout = new LinearLayout(mActivity);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText et_name = new EditText(mActivity);
		if (album_name != null)
			et_name.setText(album_name);
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
						// 修改相册
						new Api(photoCallBack, mActivity).editAlbum(PuApp.get()
								.getToken(), album_id, et_name.getText()
								.toString().trim(), sp_privacy
								.getSelectedItemPosition() + 1, user_id,
								et_privacy.getText().toString().trim());
					}
				}).setNegativeButton("取消", null).setTitle("修改相册").show();
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.PHOTOLIST);
		registerReceiver(mEvtReceiver, filter);

		String localUserid = new PrefUtil().getPreference(Params.LOCAL.UID);
		if (privacy.equals("4") && !localUserid.equals(user_id)) {
			// 需要密码
			final EditText et = new EditText(mActivity);
			new AlertDialog.Builder(mActivity)
					.setView(et)
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (et.getText().toString().trim()
											.equals("")) {
										NotificationsUtil.ToastTopMsg(
												mActivity, "请输入密码！");
										return;
									}
									mPhotoGridView = new PhotoGridView(
											photo_listview, mActivity,
											album_id, user_id, et.getText()
													.toString().trim());
									dialog.dismiss();
								}
							}).setPositiveButton("取消", null)
					.setTitle("请输入访问密码！").show();
		} else {
			// 不需要密码 从服务器返回数据
			mPhotoGridView = new PhotoGridView(photo_listview, mActivity,
					album_id, user_id, null);
		}

	}

	private PhotoGridView mPhotoGridView;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.PHOTOLIST)) {
				if (mPhotoGridView != null) {
					mPhotoGridView.refreshListViewStart();
				}
			}
		}
	};
}
