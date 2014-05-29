package com.xyhui.activity.more;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class PhoneBindingActivity extends FLActivity {
	private Button btn_back;

	private TextView text_bound_tips;
	private EditText edit_phone_num;
	private EditText edit_password;
	private Button btn_bind;

	private String mPhone;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_account_cellphone);

		btn_back = (Button) findViewById(R.id.btn_back);

		text_bound_tips = (TextView) findViewById(R.id.text_bound_tips);
		edit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
		edit_password = (EditText) findViewById(R.id.edit_password);
		btn_bind = (Button) findViewById(R.id.btn_bind);
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

		btn_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPhone = edit_phone_num.getText().toString().trim();

				if (TextUtils.isEmpty(mPhone)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写手机号");
					return;
				}

				String pass = edit_password.getText().toString().trim();

				if (TextUtils.isEmpty(pass)) {
					NotificationsUtil.ToastBottomMsg(mActivity, "请填写密码");
					return;
				}

				btn_bind.setEnabled(false);

				closeKeyboard(edit_phone_num);

				new Api(bindcallback, mActivity).mobileBind(PuApp.get().getToken(), mPhone, pass);
			}
		});
	}

	@Override
	public void ensureUi() {
		setPhoneNum();
	}

	public void setPhoneNum() {
		String phoneNum = new PrefUtil().getPreference(Params.LOCAL.MOBILE);

		if (!TextUtils.isEmpty(phoneNum)) {
			text_bound_tips.setText("当前绑定手机号：" + phoneNum);
			edit_phone_num.setHint("输入变更绑定的手机号码");
		} else {
			text_bound_tips.setText("当前绑定手机号：无");
		}

	}

	CallBack bindcallback = new CallBack() {

		@Override
		public void onSuccess(String response) {

			Response result = JSONUtils.fromJson(response, Response.class);

			if (result.status == 1) {
				new PrefUtil().setPreference(Params.LOCAL.MOBILE, mPhone);
				text_bound_tips.setText("当前绑定手机号：" + mPhone);
				NotificationsUtil.ToastBottomMsg(mActivity, "成功绑定手机号：" + mPhone);
			} else if (!TextUtils.isEmpty(result.msg)) {
				NotificationsUtil.ToastBottomMsg(mActivity, result.msg);
				btn_bind.setEnabled(true);
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "绑定失败");
				btn_bind.setEnabled(true);
			}
		}

		@Override
		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "绑定失败");
			btn_bind.setEnabled(true);
		}
	};
}
