package com.xyhui.activity.more;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.User;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class AccountManageActivity extends FLActivity {
	private Button btn_back;
	private TextView text_nickname;
	private TextView text_credit;
	private TextView text_total_score;
	private TextView text_pu_coin;
	private TextView text_integrity;
	// private LinearLayout btn_signout;

	private LinearLayout baseinfo_layout;
	private LinearLayout cellphone_layout;
	private LinearLayout password_layout;
	private User mUser;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_account);

		btn_back = (Button) findViewById(R.id.btn_back);
		text_nickname = (TextView) findViewById(R.id.text_nickname);
		text_credit = (TextView) findViewById(R.id.text_credit);
		text_total_score = (TextView) findViewById(R.id.text_total_score);
		text_pu_coin = (TextView) findViewById(R.id.text_pu_coin);
		text_integrity = (TextView) findViewById(R.id.text_integrity);
		// btn_signout = (LinearLayout) findViewById(R.id.btn_signout);
		baseinfo_layout = (LinearLayout) findViewById(R.id.baseinfo_layout);
		cellphone_layout = (LinearLayout) findViewById(R.id.cellphone_layout);
		password_layout = (LinearLayout) findViewById(R.id.password_layout);
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

		baseinfo_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mUser == null) {
					return;
				}
				// 账号信息修改
				Intent intent = new Intent();
				intent.setClass(mActivity, AccountModifyActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.USER, mUser);
				mActivity.startActivity(intent);
			}
		});

		cellphone_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 手机绑定
				Intent intent = new Intent();
				intent.setClass(mActivity, PhoneBindingActivity.class);
				mActivity.startActivity(intent);
			}
		});

		password_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mUser == null) {
					return;
				}
				// 账号密码修改
				Intent intent = new Intent();
				intent.setClass(mActivity, PasswordModifyActivity.class);
				mActivity.startActivity(intent);
			}
		});

		// btn_signout.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // 注销账号
		//
		// new AlertDialog.Builder(mActivity).setTitle("确定要注销吗?")
		// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// PuApp.get().logout(mActivity);
		// }
		// }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// }
		// }).show();
		// }
		// });
	}

	@Override
	public void ensureUi() {

	}

	@Override
	protected void onResume() {
		super.onResume();

		showProgress();
		new Api(callback, mActivity).myinfo(PuApp.get().getToken());
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mUser = JSONUtils.fromJson(response, User.class);

			if (mUser != null) {
				text_nickname.setText("账号昵称：" + mUser.uname);
				text_credit.setText("实践学分：" + mUser.school_event_credit);
				text_total_score.setText("活动积分：" + mUser.school_event_score_used);
				text_pu_coin.setText("PU币：" + mUser.money);
				text_integrity.setText("诚信度：" + mUser.grad);
			}
		}
	};
}
