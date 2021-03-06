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
import com.xyhui.activity.WebViewActivity;
import com.xyhui.types.AdBanner;
import com.xyhui.utils.Params;

public class AdBannerPagerItem extends CPagerItem {
	private Object mData;

	private ImageView banner_img;

	public AdBannerPagerItem(Context context) {
		super(context);
	}

	public AdBannerPagerItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AdBannerPagerItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initItem(Object data) {
		mData = data;
		linkUiVar();
		bindListener();
		ensureUi();
	}

	private void linkUiVar() {
		setContentView(R.layout.ad_banner_item);
		banner_img = (ImageView) findViewById(R.id.banner_img);
	}

	private void bindListener() {
		banner_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AdBanner item = (AdBanner) mData;

				Intent intent = new Intent(getContext(), WebViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE, item.title);
				intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, item.url);
				getContext().startActivity(intent);
			}
		});
	}

	private void ensureUi() {
		AdBanner item = (AdBanner) mData;
		if (!TextUtils.isEmpty(item.cover)) {
			VolleyLog.d("img=%s", item.cover);
			UrlImageViewHelper.setUrlDrawable(banner_img, item.cover, R.drawable.img_default);
		} else {
			banner_img.setImageResource(R.drawable.none);
		}
	}
}
