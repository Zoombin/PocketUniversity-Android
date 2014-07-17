package com.xyhui.utils;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class MediaPlayerUtils {

	MediaPlayer mediaPlayer;
	Context mContext;

	public MediaPlayerUtils(Context context) {
		mediaPlayer = new MediaPlayer();
		mContext = context;
	}

	public void play(String fileName, boolean loop) {
		if (null == mediaPlayer) {
			return;
		}
		AssetFileDescriptor descriptor = null;
		try {
			descriptor = mContext.getAssets().openFd(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		long start = descriptor.getStartOffset();
		long end = descriptor.getLength();

		try {
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
			mediaPlayer.setLooping(loop);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		if (null != mediaPlayer) {
			mediaPlayer.reset();
		}
	}

	public void onStop() {
		if (null != mediaPlayer) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
