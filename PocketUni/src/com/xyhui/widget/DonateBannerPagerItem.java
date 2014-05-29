package com.xyhui.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CPagerItem;
import com.xyhui.R;
import com.xyhui.activity.ImageZoomActivity;
import com.xyhui.utils.Params;

public class DonateBannerPagerItem extends CPagerItem {
	private Object mData;

	private ImageView banner_img;

	public DonateBannerPagerItem(Context context) {
		super(context);
	}

	public DonateBannerPagerItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DonateBannerPagerItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initItem(Object data) {
		mData = data;
		linkUiVar();
		ensureUi();
	}

	private void linkUiVar() {
		setContentView(R.layout.donate_banner_item);
		banner_img = (ImageView) findViewById(R.id.banner_img);

		banner_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ImageZoomActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, (String) mData);
				getContext().startActivity(intent);
			}
		});
	}

	private void ensureUi() {
		String item = (String) mData;
		if (!TextUtils.isEmpty(item)) {
			VolleyLog.d("img=%s", item);
			UrlImageViewHelper.setUrlDrawable(banner_img, item, R.drawable.img_default);
		} else {
			banner_img.setImageResource(R.drawable.none);
		}
	}
}
