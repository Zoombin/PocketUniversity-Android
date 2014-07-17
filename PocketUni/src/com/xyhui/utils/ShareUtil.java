package com.xyhui.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

public class ShareUtil {

	public static void startShareText(Context context, String subject, String text) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		context.startActivity(intent);
	}

	/**
	 * start share activity
	 * 
	 * @param context
	 * @param bitmap
	 * @param subject
	 * @param text
	 */
	public static void startShareBitmap(Context context, Bitmap bitmap, String subject, String text) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		try {
			File myCaptureFile = saveBitmap(bitmap);
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myCaptureFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

		context.startActivity(intent);
	}

	/**
	 * save and return the bitmap file
	 * 
	 * @param bitmap
	 * @return bitmap file
	 * 
	 */
	public static File saveBitmap(Bitmap bitmap) {
		File qrImgDir = null;
		File qrImgFile = null;
		try {
			String filePath = Environment.getExternalStorageDirectory().getPath();

			qrImgDir = new File(filePath, "pu");

			if (!qrImgDir.exists()) {
				qrImgDir.mkdirs();
			}

			qrImgFile = new File(qrImgDir, "qr.jpg");
			FileOutputStream fos = new FileOutputStream(qrImgFile);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return qrImgFile;
	}
}
