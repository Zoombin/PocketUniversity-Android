package com.xyhui.activity.photo;

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
import android.widget.EditText;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class PhotoListActivity extends FLActivity {

	private Button btn_back;

	private GridView photo_listview;

	private String album_id;
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
			album_id = getIntent().getStringExtra(
					Params.INTENT_EXTRA.ALBUM_ID);
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
	}

	@Override
	public void ensureUi() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.PHOTOLIST);
		registerReceiver(mEvtReceiver, filter);
		if (privacy.equals("4")) {
			// 需要密码
			final EditText et = new EditText(mActivity);
			new AlertDialog.Builder(mActivity).setView(et).setNegativeButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(et.getText().toString().trim().equals("")){
						NotificationsUtil.ToastTopMsg(mActivity, "请输入密码！");
						return;
					}
					mPhotoGridView = new PhotoGridView(photo_listview, mActivity,album_id ,user_id, et.getText().toString().trim());	
					dialog.dismiss();
				}
			}).setPositiveButton("取消", null).setTitle("请输入访问密码！").show();
		} else {
			// 不需要密码 从服务器返回数据
			mPhotoGridView = new PhotoGridView(photo_listview, mActivity,album_id, user_id, null);
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
