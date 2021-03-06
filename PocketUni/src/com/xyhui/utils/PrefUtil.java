package com.xyhui.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.xyhui.activity.PuApp;

public class PrefUtil {

	private SharedPreferences mPrefs;

	public PrefUtil() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(PuApp.get());
	}

	public void setPreference(String PrefId, String value) {
		if (TextUtils.isEmpty(value)) {
			mPrefs.edit().remove(PrefId).commit();
		} else {
			mPrefs.edit().putString(PrefId, value).commit();
		}
	}

	public void setPreference(String PrefId, long value) {
		if (value == 0) {
			mPrefs.edit().remove(PrefId).commit();
		} else {
			mPrefs.edit().putLong(PrefId, value).commit();
		}
	}

	public void setPreference(String PrefId, int value) {
		if (value == 0) {
			mPrefs.edit().remove(PrefId).commit();
		} else {
			mPrefs.edit().putInt(PrefId, value).commit();
		}
	}

	public void setPreference(String PrefId, boolean value) {
		mPrefs.edit().putBoolean(PrefId, value).commit();
	}

	public String getPreference(String PrefId) {
		String value = mPrefs.getString(PrefId, null);
		if (TextUtils.isEmpty(value)) {
			return null;
		}
		return value;
	}

	public int getIntPreference(String PrefId) {
		return getIntPreference(PrefId, 0);
	}

	public int getIntPreference(String PrefId, int defaultvalue) {
		int value = mPrefs.getInt(PrefId, defaultvalue);
		return value;
	}

	public long getLongPreference(String PrefId) {
		long value = mPrefs.getLong(PrefId, 0);
		return value;
	}

	public boolean getBoolPreference(String PrefId) {
		boolean value = mPrefs.getBoolean(PrefId, true);
		return value;
	}
}
