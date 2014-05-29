package com.xyhui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRcodeUtils {

	private final static int QRCODE_IMAGE_WIDTH = 150;
	private final static int QRCODE_IMAGE_HEIGHT = 150;
	private final static String QRCODE_IMAGE_NAME = "xyhui_qrcode.png";

	// 二维码图片背景颜色，默认透明
	private final static int QRCODE_IMAGE_BACKGROUND = 0x00000000;
	// 二维码图片前景色，可以指定其他颜色，让二维码变成彩色效果
	private final static int QRCODE_IMAGE_FOREGROUND = 0xFF000000;

	public static void setupQRcode(String content, ImageView imageview) {

		if (TextUtils.isEmpty(content)) {
			return;
		}

		FileOutputStream fos = null;
		Bitmap bitmap = null;
		try {
			// 生成二维码图像
			bitmap = encodeAsBitmap(content, BarcodeFormat.QR_CODE, QRCODE_IMAGE_WIDTH,
					QRCODE_IMAGE_HEIGHT);
			if (null != bitmap) {
				// 将二维码图像保存到文件
				File file = new File(Environment.getExternalStorageDirectory(), QRCODE_IMAGE_NAME);
				fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
		// 显示QRCode
		if (null != bitmap) {
			imageview.setImageBitmap(bitmap);
			imageview.setVisibility(View.VISIBLE);
		}
	}

	private static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth,
			int desiredHeight) throws WriterException {

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		BitMatrix matrix;
		Writer writer = new QRCodeWriter();

		matrix = writer.encode(contents, com.google.zxing.BarcodeFormat.QR_CODE, desiredWidth,
				desiredHeight, hints);

		int width = matrix.getWidth();
		int height = matrix.getHeight();

		int[] pixels = new int[width * height];

		// All are transparent, by default, the background color
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = QRCODE_IMAGE_BACKGROUND;
		}

		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = matrix.get(x, y) ? QRCODE_IMAGE_FOREGROUND
						: QRCODE_IMAGE_BACKGROUND;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
