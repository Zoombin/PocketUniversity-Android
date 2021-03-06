package com.xyhui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class AccountBindingActivity extends FLActivity {
	private final int BINDING_TYPE_PHONE = 0;
	private final int BINDING_TYPE_EMAIL = 1;

	private int bindingType = BINDING_TYPE_PHONE;

	private Button btn_back;

	private Button btn_binding_phone;
	private Button btn_binding_email;

	private LinearLayout svPhone;
	private LinearLayout svEmail;

	private EditText edit_phone_num;
	private Button btn_get_msm_code;
	private TextView text_phone_sendtips;
	private EditText edit_msm_code;
	private Button btn_phone_bind;

	private EditText edit_email_addr;
	private Button btn_get_email_code;
	private TextView text_email_sendtips;
	private EditText edit_email_code;
	private Button btn_email_bind;

	private String phone;
	private String email;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_account_binding);

		btn_back = (Button) findViewById(R.id.btn_back);

		btn_binding_phone = (Button) findViewById(R.id.btn_binding_phone);
		btn_binding_email = (Button) findViewById(R.id.btn_binding_email);

		svPhone = (LinearLayout) findViewById(R.id.sv_phone_binding);
		svEmail = (LinearLayout) findViewById(R.id.sv_email_binding);

		edit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
		btn_get_msm_code = (Button) findViewById(R.id.btn_get_msm_code);
		text_phone_sendtips = (TextView) findViewById(R.id.text_phone_sendtips);
		edit_msm_code = (EditText) findViewById(R.id.edit_msm_code);
		btn_phone_bind = (Button) findViewById(R.id.btn_phone_bind);

		edit_email_addr = (EditText) findViewById(R.id.edit_email_addr);
		btn_get_email_code = (Button) findViewById(R.id.btn_get_email_code);
		text_email_sendtips = (TextView) findViewById(R.id.text_email_sendtips);
		edit_email_code = (EditText) findViewById(R.id.edit_email_code);
		btn_email_bind = (Button) findViewById(R.id.btn_email_bind);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AccountBindingActivity.this).setTitle("注意")
						.setMessage("为了您的账号安全,不验证手机号或者邮箱将无法使用本软件")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								PuApp.get().logout(mActivity);
							}
						}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

							}
						}).setCancelable(false).show();
			}
		});

		btn_binding_phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bindingType = BINDING_TYPE_PHONE;
				setBindingType();
			}
		});

		btn_binding_email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bindingType = BINDING_TYPE_EMAIL;
				setBindingType();
			}
		});

		btn_get_msm_code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取手机短信验证码
				phone = edit_phone_num.getText().toString().trim();

				if (TextUtils.isEmpty(phone)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写手机号");
					return;
				}

				btn_get_msm_code.setEnabled(false);
				text_phone_sendtips.setText("正在获取, 请稍候");

				closeKeyboard(edit_phone_num);

				new Api(getPhoneCodeCB, mActivity).mobileCode(PuApp.get().getToken(), phone);
			}
		});

		btn_phone_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String code = edit_msm_code.getText().toString().trim();

				if (TextUtils.isEmpty(code)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写验证码");
					return;
				}

				btn_phone_bind.setEnabled(false);
				text_phone_sendtips.setText("正在验证, 请稍候");

				closeKeyboard(edit_msm_code);

				new Api(phoneBindCB, mActivity)
						.userMobileBind(PuApp.get().getToken(), phone, code);
			}
		});

		btn_get_email_code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取邮箱验证码
				email = edit_email_addr.getText().toString().trim();

				if (TextUtils.isEmpty(email)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写邮箱地址");
					return;
				}

				btn_get_email_code.setEnabled(false);
				text_email_sendtips.setText("正在获取, 请稍候");

				closeKeyboard(edit_email_addr);

				new Api(getEmailCodeCB, mActivity).emailCode(PuApp.get().getToken(), email);
			}
		});

		btn_email_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String code = edit_email_code.getText().toString().trim();

				if (TextUtils.isEmpty(code)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写验证码");
					return;
				}

				btn_email_bind.setEnabled(false);
				text_email_sendtips.setText("正在验证, 请稍候");

				closeKeyboard(edit_email_code);

				new Api(emailBindCB, mActivity).emailBind(PuApp.get().getToken(), email, code);
			}
		});
	}

	@Override
	public void ensureUi() {
		setBindingType();
	}

	public void setBindingType() {
		if (bindingType == BINDING_TYPE_PHONE) {
			btn_binding_phone.setSelected(true);
			btn_binding_email.setSelected(false);

			svEmail.setVisibility(View.GONE);
			svPhone.setVisibility(View.VISIBLE);
		} else {
			btn_binding_phone.setSelected(false);
			btn_binding_email.setSelected(true);

			svPhone.setVisibility(View.GONE);
			svEmail.setVisibility(View.VISIBLE);
		}
	}

	CallBack getPhoneCodeCB = new CallBack() {

		@Override
		public void onSuccess(String response) {

			Response result = JSONUtils.fromJson(response, Response.class);

			if (result.status == 1) {
				text_phone_sendtips.setText("手机短信验证码已经发出，请耐心等待");
			} else if (!TextUtils.isEmpty(result.msg)) {
				text_phone_sendtips.setText(result.msg);
				btn_get_msm_code.setEnabled(true);
			} else {
				text_phone_sendtips.setText("发送短信失败");
				btn_get_msm_code.setEnabled(true);
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "发送短信失败");
			btn_get_msm_code.setEnabled(true);
		}
	};

	CallBack getEmailCodeCB = new CallBack() {

		@Override
		public void onSuccess(String response) {

			Response result = JSONUtils.fromJson(response, Response.class);

			if (result.status == 1) {
				text_email_sendtips.setText("邮箱验证码已经发出，请耐心等待");
			} else if (!TextUtils.isEmpty(result.msg)) {
				btn_get_email_code.setEnabled(true);
				text_email_sendtips.setText(result.msg);
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "发送邮箱验证码失败");
				btn_get_email_code.setEnabled(true);
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "发送邮箱验证码失败");
			btn_get_email_code.setEnabled(true);
		}
	};

	CallBack phoneBindCB = new CallBack() {

		@Override
		public void onSuccess(String response) {

			Response result = JSONUtils.fromJson(response, Response.class);

			if (result.status == 1) {
				new PrefUtil().setPreference(Params.LOCAL.MOBILE, phone);
				text_phone_sendtips.setText("成功绑定手机号：" + phone);

				final Intent intent = new Intent();

				if (!PuApp.get().getLocalDataMgr().isInited()) {
					intent.setClass(mActivity, UserInitActivity.class);
				} else {
					// intent.setClass(mActivity, NewFeatureActivity.class);
					intent.setClass(mActivity, MainActivity.class);
				}

				btn_binding_phone.postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivity(intent);
						finish();
					}
				}, 1000);
			} else if (!TextUtils.isEmpty(result.msg)) {
				text_phone_sendtips.setText(result.msg);
				btn_phone_bind.setEnabled(true);
			} else {
				text_phone_sendtips.setText("验证失败");
				btn_phone_bind.setEnabled(true);
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "验证失败");
			btn_phone_bind.setEnabled(true);
		}
	};

	CallBack emailBindCB = new CallBack() {

		@Override
		public void onSuccess(String response) {

			Response result = JSONUtils.fromJson(response, Response.class);

			if (result.status == 1) {
				new PrefUtil().setPreference(Params.LOCAL.EMAIL, email);
				text_email_sendtips.setText("成功绑定邮箱：" + email);
				final Intent intent = new Intent();
				if (!PuApp.get().getLocalDataMgr().isInited()) {
					intent.setClass(mActivity, UserInitActivity.class);
				} else {
					// intent.setClass(mActivity, NewFeatureActivity.class);
					intent.setClass(mActivity, MainActivity.class);
				}
				btn_binding_phone.postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivity(intent);
						finish();
					}
				}, 1000);

			} else if (!TextUtils.isEmpty(result.msg)) {
				text_email_sendtips.setText(result.msg);
				btn_email_bind.setEnabled(true);
			} else {
				text_email_sendtips.setText("验证失败");
				btn_email_bind.setEnabled(true);
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "验证失败");
			btn_email_bind.setEnabled(true);
		}
	};
}
