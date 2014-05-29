package com.xyhui.widget;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mslibs.widget.CSpannedTextViewBase;
import com.xyhui.R;
import com.xyhui.utils.SpanUtils;
import com.xyhui.utils.URLImageParserSmall;

public class SpannedTextViewBlog extends CSpannedTextViewBase {
	public SpannedTextViewBlog(Context context) {
		super(context);
	}

	public SpannedTextViewBlog(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpannedTextViewBlog(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.widget_spanned_weibo_content, this);
	}

	@Override
	public void setData(Object obj) {
		// VolleyLog.d("Spanned TextView Blog:inflate");
		if (obj instanceof String) {
			String content = (String) obj;
			// VolleyLog.d("inflate weibo content:%s", content);

			TextView spanned_textview_content = (TextView) findViewById(R.id.spanned_textview_content);

			URLImageParserSmall p = new URLImageParserSmall(spanned_textview_content, getContext());
			SpannableString s = SpanUtils.txtToImg(getContext(), p, content);

			spanned_textview_content.setText(s);
		}
	}

}
