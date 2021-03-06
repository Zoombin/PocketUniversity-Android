package com.xyhui.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.widget.CPagerItem;
import com.xyhui.R;
import com.xyhui.activity.event.EventViewActivity;
import com.xyhui.api.Client;
import com.xyhui.types.EventBanner;
import com.xyhui.utils.Params;

public class EventBannerPagerItem extends CPagerItem {
	private Object mData;

	private ImageView banner_img;
	private TextView banner_title;

	public EventBannerPagerItem(Context context) {
		super(context);
	}

	public EventBannerPagerItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EventBannerPagerItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initItem(Object data) {
		mData = data;
		linkUiVar();
		bindListener();
		ensureUi();
	}

	private void linkUiVar() {
		setContentView(R.layout.widget_banner_item);
		banner_img = (ImageView) findViewById(R.id.banner_img);
		banner_title = (TextView) findViewById(R.id.banner_title);
	}

	private void bindListener() {
		banner_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBanner item = (EventBanner) mData;

				Intent intent = new Intent();
				intent.setClass(getContext(), EventViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.EVENTID, item.id);
				getContext().startActivity(intent);
			}
		});
	}

	private void ensureUi() {
		EventBanner item = (EventBanner) mData;
		if (!TextUtils.isEmpty(item.banner)) {
			String img = Client.BANNER_URL_PREFIX + item.banner;
			// VolleyLog.d("img=%s", img);
			UrlImageViewHelper.setUrlDrawable(banner_img, img, R.drawable.img_default);
		} else {
			banner_img.setImageResource(R.drawable.none);
		}

		banner_title.setText(item.title);
	}
}
