package com.xyhui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mslibs.utils.VolleyLog;

public class URLImageParserSmall implements ImageGetter {
	Context c;
	View container;

	int max_width = 456;
	int max_height = 456;

	public URLImageParserSmall(View t, Context c) {
		this.c = c;
		this.container = t;
	}

	public Drawable getDrawable(String source) {

		final WindowManager w = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		w.getDefaultDisplay().getMetrics(metrics);
		int imgsize = 304;
		if (source.indexOf("wangwang/smiley") > 0 || source.indexOf("emotions") > 0) {
			imgsize = 30;
		}

		max_width = max_height = (int) Math.round(imgsize * metrics.density);

		VolleyLog.d("max_width=%d", max_width);

		URLDrawable urlDrawable = new URLDrawable();

		// get the actual source
		ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

		urlDrawable.setBounds(0, 0, max_width, 30);

		String url = source;
		asyncTask.execute(url);
		VolleyLog.d("%d : %s", imgsize, url);

		return urlDrawable;

	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable d) {
			this.urlDrawable = d;
		}

		@Override
		protected Drawable doInBackground(String... params) {
			String source = params[0];
			return fetchDrawable(source);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// set the correct bound according to the result from HTTP call
			if (result != null) {

				int height = result.getIntrinsicHeight();
				int width = result.getIntrinsicWidth();
				height = (URLImageParserSmall.this.max_width) * height / width;
				width = URLImageParserSmall.this.max_width;

				urlDrawable.setBounds(0, 0, width, height);

				// change the reference of the current drawable to the result
				// from the HTTP call
				urlDrawable.drawable = result;

				// redraw the image by invalidating the container
				URLImageParserSmall.this.container.invalidate();

				TextView t = (TextView) URLImageParserSmall.this.container;
				t.setText(t.getText());

			}
		}

		/***
		 * Get the Drawable from URL
		 * 
		 * @param urlString
		 * @return
		 */
		public Drawable fetchDrawable(String urlString) {
			try {
				InputStream is = fetch(urlString);
				Drawable drawable = Drawable.createFromStream(is, "src");

				int height = drawable.getIntrinsicHeight();
				int width = drawable.getIntrinsicWidth();
				height = (URLImageParserSmall.this.max_width) * height / width;
				width = URLImageParserSmall.this.max_width;

				drawable.setBounds(0, 0, width, height);

				return drawable;
			} catch (Exception e) {
				return null;
			}
		}

		private InputStream fetch(String urlString) throws MalformedURLException, IOException {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(urlString);
			HttpResponse response = httpClient.execute(request);
			return response.getEntity().getContent();
		}
	}
}