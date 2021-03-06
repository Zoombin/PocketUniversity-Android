package com.xyhui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.types.UserData;
import com.xyhui.widget.FLActivity;

public class UserInitActivity extends FLActivity {

	private Button btn_back;
	private Button btn_submit;

	private EditText edit_new_password;
	private EditText edit_repeat_password;
	private EditText edit_password_email;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_userinit);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_submit = (Button) findViewById(R.id.btn_submit);

		edit_new_password = (EditText) findViewById(R.id.edit_new_password);
		edit_repeat_password = (EditText) findViewById(R.id.edit_repeat_password);
		edit_password_email = (EditText) findViewById(R.id.edit_password_email);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(UserInitActivity.this).setTitle("注意")
						.setMessage("为了您的账号安全,不初始化将无法使用本软件")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								PuApp.get().logout(mActivity);
							}
						}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).setCancelable(false).show();
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 提交修改信息
				String password = edit_new_password.getText().toString();
				password = password.trim();
				String repassword = edit_repeat_password.getText().toString();
				repassword = repassword.trim();
				String email = edit_password_email.getText().toString();
				email = email.trim();

				if (password.length() < 6 || password.length() > 15) {
					NotificationsUtil.ToastBottomMsg(mActivity, "新密码格式有误, 合法的为6-15位字符");
					return;
				}

				if (!password.equals(repassword)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "两次密码不同");
					return;
				}

				if (TextUtils.isEmpty(email)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请输入密保邮箱");
					return;
				}

				new Api(callback, mActivity).initUser(PuApp.get().getToken(), email, repassword);
			}
		});
	}

	CallBack callback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			dismissProgress();

			Response r = JSONUtils.fromJson(response, Response.class);

			if (null != r) {
				if (1 == r.status) {
					NotificationsUtil.ToastBottomMsg(mActivity, "密码修改成功，已添加密保邮箱，可通过该邮箱找回密码");
					// final Intent intent = new Intent(UserInitActivity.this,
					// NewFeatureActivity.class);
					final Intent intent = new Intent(UserInitActivity.this, MainActivity.class);
					btn_submit.postDelayed(new Runnable() {
						@Override
						public void run() {
							startActivity(intent);
							finish();
						}
					}, 1000);
				} else if (!TextUtils.isEmpty(r.msg)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.msg);
				}
			}

		}
	};

	CallBack passwordEmailCB = new CallBack() {

		@Override
		public void onSuccess(String response) {
			dismissProgress();

			UserData result = JSONUtils.fromJson(response, UserData.class);

			if (result.status.equals("1")) {
				edit_password_email.setText(result.email);
			}
		}
	};

	@Override
	public void ensureUi() {
		new Api(passwordEmailCB, mActivity).passwordEmail(PuApp.get().getToken());
	}
}
