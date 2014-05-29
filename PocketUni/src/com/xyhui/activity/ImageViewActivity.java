package com.xyhui.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class ImageViewActivity extends FLActivity {

	private ImageView img_imageview;

	private String mUrl;
	private RelativeLayout layout_bg;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.URL)) {
			mUrl = getIntent().getStringExtra(Params.INTENT_EXTRA.URL);
		} else {
			finish();
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_imageview);

		img_imageview = (ImageView) findViewById(R.id.img_imageview);
		layout_bg = (RelativeLayout) findViewById(R.id.layout_bg);
	}

	@Override
	public void bindListener() {

		layout_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		img_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void ensureUi() {
		UrlImageViewHelper.setUrlDrawable(img_imageview, mUrl, R.drawable.img_default,
				UrlImageViewHelper.CACHE_DURATION_THREE_DAYS, callback);
	}

	UrlImageViewCallback callback = new UrlImageViewCallback() {
		@Override
		public void onLoaded(ImageView arg0, Bitmap arg1, String arg2, boolean arg3) {

		}

	};
}
