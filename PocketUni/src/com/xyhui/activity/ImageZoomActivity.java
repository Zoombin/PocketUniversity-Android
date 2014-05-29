package com.xyhui.activity;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.DisplayType;
import it.sephiroth.android.library.imagezoom.test.utils.DecodeUtils;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Window;

import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class ImageZoomActivity extends FLActivity {

	private ImageViewTouch mImage;
	private Matrix imageMatrix;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mImage = (ImageViewTouch) findViewById(R.id.image);

		// set the default image display type
		mImage.setDisplayType(DisplayType.FIT_IF_BIGGER);
	}

	@Override
	public void linkUiVar() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_imageview_zoom);
	}

	@Override
	public void ensureUi() {
		String url = getIntent().getStringExtra(Params.INTENT_EXTRA.WEBVIEW_URL);
		showProgress();
		new LoadImageTask().execute(url);
	}

	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Uri imageUri = Uri.parse(params[0]);

			final int size = -1; // use the original image size
			Bitmap bitmap = DecodeUtils.decode(mActivity, imageUri, size, size);
			return bitmap;
		}

		@Override
		protected void onPostExecute(final Bitmap bitmap) {
			dismissProgress();
			if (null != bitmap) {
				if (null == imageMatrix) {
					imageMatrix = new Matrix();
				}

				mImage.setImageBitmap(bitmap, imageMatrix.isIdentity() ? null : imageMatrix,
						ImageViewTouchBase.ZOOM_INVALID, ImageViewTouchBase.ZOOM_INVALID);
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, "加载图片失败");
			}
		}
	}
}
