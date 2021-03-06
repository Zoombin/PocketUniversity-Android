package com.xyhui.activity.group;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupNoticeEditActivity extends FLActivity {
	private Button btn_back;
	private Button btn_submit;
	private EditText edit_group_notice;

	private String group_id;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			group_id = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);

			VolleyLog.d("got groupid:%s", group_id);
		}

		if (TextUtils.isEmpty(group_id)) {
			VolleyLog.d("no groupid");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_notice_edit);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		edit_group_notice = (EditText) findViewById(R.id.edit_group_notice);
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
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 提交创建部落
				showProgress("正在提交", ",请稍候...");
				String notice = edit_group_notice.getText().toString();
				new Api(callback, mActivity).groupannounce(PuApp.get().getToken(), group_id,
						notice);
			}
		});

	}

	@Override
	public void ensureUi() {

	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			Response r = JSONUtils.fromJson(response, Response.class);

			if (null != r) {
				if (!TextUtils.isEmpty(r.response)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.response);
					finish();
				} else if (!TextUtils.isEmpty(r.message)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.message);
				}
			}

		}
	};
}
