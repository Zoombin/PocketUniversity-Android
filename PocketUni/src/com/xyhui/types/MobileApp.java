package com.xyhui.types;

public class MobileApp {
	public MobileApp(String appName, String iconUrl, int iconId, String downloadUrl, String pkgName) {
		mAppName = appName;
		mIconUrl = iconUrl;
		mIconId = iconId;
		mDownloadUrl = downloadUrl;
		mPkgName = pkgName;
	}

	/**
	 * 应用名字
	 */
	public String mAppName;

	/**
	 * 应用图标地址
	 */
	public String mIconUrl;

	/**
	 * 应用图标id
	 */
	public int mIconId;

	/**
	 * 应用apk文件下载地址
	 */
	public String mDownloadUrl;

	/**
	 * 应用包名
	 */
	public String mPkgName;
}
