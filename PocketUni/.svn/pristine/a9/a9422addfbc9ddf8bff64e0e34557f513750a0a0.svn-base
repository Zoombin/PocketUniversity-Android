package com.xyhui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.widget.CSpannedTextViewBase;
import com.xyhui.R;
import com.xyhui.activity.ImageZoomActivity;
import com.xyhui.activity.weibo.UserHomePageActivity;
import com.xyhui.types.Weibo;
import com.xyhui.types.WeiboTypeData;
import com.xyhui.utils.Params;
import com.xyhui.utils.SpanUtils;

public class SpannedTextViewForward extends CSpannedTextViewBase {

	public SpannedTextViewForward(Context context) {
		super(context);
	}

	public SpannedTextViewForward(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpannedTextViewForward(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.widget_spanned_weibo_forward, this);
	}

	@Override
	public void setData(Object obj) {
		// VolleyLog.d("SpannedTextViewForward inflate");
		if (obj instanceof Weibo) {
			final Weibo weibo = (Weibo) obj;
			// VolleyLog.d("inflate weibo Forward");

			TextView text_nickname = (TextView) findViewById(R.id.text_nickname);

			text_nickname.setText(weibo.uname);

			text_nickname.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getContext(), UserHomePageActivity.class);
					intent.putExtra(Params.INTENT_EXTRA.USER_ID, weibo.uid);
					getContext().startActivity(intent);
				}
			});

			TextView spanned_textview_forward = (TextView) findViewById(R.id.spanned_textview_forward);
			ImageView img_weibo = (ImageView) findViewById(R.id.img_weibo);

			SpannableString s = SpanUtils.txtToImg(getContext(), weibo.content);
			spanned_textview_forward.setText(s);

			spanned_textview_forward.setMovementMethod(LinkMovementMethod.getInstance());

			final WeiboTypeData img = weibo.type_data;
			if (img == null) {
				img_weibo.setVisibility(View.GONE);
			} else {
				UrlImageViewHelper.setUrlDrawable(img_weibo, img.thumburl);
				img_weibo.setVisibility(View.VISIBLE);

				img_weibo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Intent intent = new Intent();
						// intent.setClass(getContext(), WebViewActivity.class);
						// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE, "图片浏览");
						// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, img.thumbmiddleurl);
						// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE,
						// WebViewActivity.TYPE_IMAGE);
						// getContext().startActivity(intent);

						Intent intent = new Intent(getContext(), ImageZoomActivity.class);
						intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, img.thumbmiddleurl);
						getContext().startActivity(intent);
					}
				});
			}
		}
	}
}
