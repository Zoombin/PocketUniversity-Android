package com.xyhui.widget;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mslibs.widget.CSpannedTextViewBase;
import com.xyhui.R;
import com.xyhui.utils.SpanUtils;

public class SpannedTextViewContent extends CSpannedTextViewBase {
	public SpannedTextViewContent(Context context) {
		super(context);
	}

	public SpannedTextViewContent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpannedTextViewContent(Context context, AttributeSet attrs, int defStyle) {
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
		// VolleyLog.d("SpannedTextViewContent inflate");
		if (obj instanceof String) {
			String content = (String) obj;
			// VolleyLog.d("inflate weibo content:%s", content);

			SpannableString s = SpanUtils.txtToImg(getContext(), content);
			TextView spanned_textview_content = (TextView) findViewById(R.id.spanned_textview_content);
			spanned_textview_content.setText(s);
			spanned_textview_content.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}
}
