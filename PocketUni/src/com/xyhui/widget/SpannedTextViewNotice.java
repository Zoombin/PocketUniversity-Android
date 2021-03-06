package com.xyhui.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mslibs.widget.CSpannedTextViewBase;
import com.xyhui.R;
import com.xyhui.utils.URLImageParser;

public class SpannedTextViewNotice extends CSpannedTextViewBase {
	public SpannedTextViewNotice(Context context) {
		super(context);
	}

	public SpannedTextViewNotice(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpannedTextViewNotice(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater()
				.inflate(R.layout.widget_spanned_notice, this);
	}

	@Override
	public void setData(Object obj) {
		// VolleyLog.d("SpannedTextViewNotice inflate");
		if (obj instanceof String) {
			String content = (String) obj;
			String formated = content.replace("&lt;", "<").replace("&gt;", ">")
					.replace("&amp;", "&").replace("&quot;", "\"");

			// String content = "";
			// try {
			// InputStream in = getResources().getAssets().open("index.html");
			// // 获取文件的字节数
			// int lenght = in.available();
			// // 创建byte数组
			// byte[] buffer = new byte[lenght];
			// // 将文件中的数据读到byte数组中
			// in.read(buffer);
			// content = EncodingUtils.getString(buffer, ENCODING);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			// VolleyLog.d("inflate weibo content: %s", formated);

			TextView spanned_textview_content = (TextView) findViewById(R.id.spanned_textview_content);

			URLImageParser p = new URLImageParser(spanned_textview_content, getContext());
			Spanned htmlSpan = Html.fromHtml(formated, p, null);
			// 使TextView中的链接可点击
			spanned_textview_content.setClickable(true);
			spanned_textview_content.setMovementMethod(LinkMovementMethod.getInstance());
			spanned_textview_content.setText(htmlSpan);
		}
	}
}
