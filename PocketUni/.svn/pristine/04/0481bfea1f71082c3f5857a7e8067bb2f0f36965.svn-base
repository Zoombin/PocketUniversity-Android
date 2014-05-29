package com.xyhui.activity.event;

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
import com.xyhui.types.EventNews;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;
import com.xyhui.widget.SpannedTextViewBlog;

public class EventNewsViewActivity extends FLActivity {
	private Button btn_back;

	private TextView text_title;
	private SpannedTextViewBlog text_content;

	private String mNewsId;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.EVENTNEWS_ID)) {
			mNewsId = getIntent().getStringExtra(Params.INTENT_EXTRA.EVENTNEWS_ID);
			VolleyLog.d("got announce id传入:%s", mNewsId);
		} else {
			VolleyLog.d("没有announce传入");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_news_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		text_title = (TextView) findViewById(R.id.text_title);
		text_content = (SpannedTextViewBlog) findViewById(R.id.text_content);
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
		new Api(callback, mActivity).getNews(PuApp.get().getToken(), mNewsId);
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			EventNews item = JSONUtils.fromJson(response, EventNews.class);

			text_title.setText(item.title);
			text_content.setData(item.content);
		}
	};
}
