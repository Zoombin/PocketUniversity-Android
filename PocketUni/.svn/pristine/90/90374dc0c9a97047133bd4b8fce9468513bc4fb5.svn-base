package com.xyhui.activity.app;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Announce;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;
import com.xyhui.widget.SpannedTextViewNotice;

public class NoticeViewActivity extends FLActivity {
	private Button btn_back;
	private TextView text_title;
	private TextView text_params;
	private SpannedTextViewNotice text_content;

	private String mAnnounceId;
	private int mAnounceType;

	private PrefUtil mPrefUtil;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.ANNOUNCE_ID)) {
			mAnnounceId = getIntent().getStringExtra(Params.INTENT_EXTRA.ANNOUNCE_ID);
			mAnounceType = getIntent().getIntExtra(Params.INTENT_EXTRA.ANNOUNCE_TYPE, 0);
		} else {
			VolleyLog.d("没有announce传入");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_notice_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		text_title = (TextView) findViewById(R.id.text_title);
		text_params = (TextView) findViewById(R.id.text_params);
		text_content = (SpannedTextViewNotice) findViewById(R.id.text_content);

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
		mPrefUtil = new PrefUtil();

		showProgress();
		if (NoticeList.NOTICE_OFFICIAL == mAnounceType) {
			new Api(callback, mActivity).getNotice(PuApp.get().getToken(), mAnnounceId);
		} else {
			new Api(callback, mActivity).getAnnounce(PuApp.get().getToken(), mAnnounceId, "1");
		}
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			Announce item = JSONUtils.fromJson(response, Announce.class);

			if (null != item) {
				text_title.setText(item.title);
				text_params.setText("发布时间：" + item.time);
				text_content.setData(item.content);

				if (null == mPrefUtil.getPreference(mAnnounceId)) {
					mPrefUtil.setPreference(mAnnounceId, response);
				}
			}
		}

		public void onFailure(String message) {
			onSuccess(mPrefUtil.getPreference(mAnnounceId));
		}
	};
}
