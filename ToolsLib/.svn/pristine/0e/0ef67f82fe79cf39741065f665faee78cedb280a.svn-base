package com.mslibs.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

public class ImageUtils {

	private ImageUtils() {
	}

	public static Boolean isLoaded(ImageView imageView) {
		Boolean loaded = (Boolean) imageView.getTag(imageView.getId());
		if (loaded != null && loaded)
			return true;
		return false;

	}

	public static void clear(ImageView imageView) {
		imageView.setImageBitmap(null);
		imageView.setTag(imageView.getId(), false);
	}

	public static Bitmap readBitmapFromNetwork(URL url) {
		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bmp = null;
		try {
			URLConnection conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bmp = BitmapFactory.decodeStream(bis);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (is != null)
					is.close();
				if (bis != null)
					bis.close();
			} catch (IOException e) {
			}
		}
		return bmp;
	}

	public static void resampleImageAndSaveToNewLocation(String pathInput, String pathOutput)
			throws Exception {
		Bitmap bmp = resampleImage(pathInput, 500);

		OutputStream out = new FileOutputStream(pathOutput);
		bmp.compress(Bitmap.CompressFormat.JPEG, 75, out);
	}

	public static Bitmap resampleImage(String path, int maxDim) throws Exception {

		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bfo);

		BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
		optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);

		Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);

		Matrix m = new Matrix();

		if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
			BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(), bmpt.getHeight(),
					maxDim);
			m.postScale((float) optsScale.outWidth / (float) bmpt.getWidth(),
					(float) optsScale.outHeight / (float) bmpt.getHeight());
		}

		return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt.getHeight(), m, true);
	}

	private static BitmapFactory.Options getResampling(int cx, int cy, int max) {
		float scaleVal = 1.0f;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		if (cx > cy) {
			scaleVal = (float) max / (float) cx;
		} else if (cy > cx) {
			scaleVal = (float) max / (float) cy;
		} else {
			scaleVal = (float) max / (float) cx;
		}
		bfo.outWidth = (int) (cx * scaleVal + 0.5f);
		bfo.outHeight = (int) (cy * scaleVal + 0.5f);
		return bfo;
	}

	private static int getClosestResampleSize(int cx, int cy, int maxDim) {
		int max = Math.max(cx, cy);

		int resample = 1;
		for (resample = 1; resample < Integer.MAX_VALUE; resample++) {
			if (resample * maxDim > max) {
				resample--;
				break;
			}
		}

		if (resample > 0) {
			return resample;
		}
		return 1;
	}

	public static BitmapFactory.Options getBitmapDims(String path) throws Exception {
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bfo);
		return bfo;
	}

	public static byte[] getBytes(InputStream is) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int i = 0; (i = is.read(buf)) > 0;) {
				os.write(buf, 0, i);
			}
			os.close();
			return os.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}