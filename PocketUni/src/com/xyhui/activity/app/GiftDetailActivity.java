package com.xyhui.activity.app;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Gift;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GiftDetailActivity extends FLActivity {
	private Button btn_back;
	private TextView tv_giftdetail;
	private TextView tv_content_html;
	private Gift gift;

	private String giftid;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GIFTID)) {
			giftid = getIntent().getStringExtra(Params.INTENT_EXTRA.GIFTID);
		} else {
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_gift_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		
		tv_giftdetail = (TextView) findViewById(R.id.tv_giftdetail); 
		tv_content_html = (TextView) findViewById(R.id.tv_content_html);
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
		showProgress();
		new Api(callback, mActivity).getGift(PuApp.get().getToken(), giftid);
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			gift = JSONUtils.fromJson(response, Gift.class);

			if (gift != null) {
				tv_giftdetail.setText(gift.getGiftDetail());
				if (!TextUtils.isEmpty(gift.content)) {
					tv_content_html.setVisibility(View.VISIBLE);
					tv_content_html.setText(Html.fromHtml(gift.content));
				}
			}
		}
	};
}