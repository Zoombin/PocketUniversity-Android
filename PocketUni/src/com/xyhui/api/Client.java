package com.xyhui.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mslibs.utils.VolleyLog;

public class Client {

	private static final int TIME_OUT_INTERVAL = 10000;

//	 public static final String BASE_URL = "http://xyhui.com/index.php";
	public static final String BASE_URL = "http://pocketuni.net/index.php";
	public static final String VOLUNTEER_UPDATE_URL = "http://www.dakaqi.cn/services/search-android-version.action";
	public static final String GAME_LIST_URL = "http://pu.websharp.com.cn/invoke/Game.ashx?method=listgame";

	public static final String UPLOAD_URL = "http://pocketuni.net/thumb.php?w=80&h=80&url=./data/uploads/";
	public static final String BANNER_URL_PREFIX = "http://pocketuni.net/thumb.php?w=480&h=270&url=";
	public static final String REAL_UPLOAD_URL = "http://pocketuni.net/data/uploads/";

	private static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * Important! This method execute the get action!
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @param responseHandler
	 */
	public static void post(String url, String method, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		params.put("app", "api");
		params.put("mod", url);
		params.put("act", method);

		client.setTimeout(TIME_OUT_INTERVAL);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);
		VolleyLog.d("%s ? %s", BASE_URL, params.toString());
		client.get(BASE_URL, params, responseHandler);
	}

	/**
	 * Important! This method execute the post action!
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @param responseHandler
	 */
	public static void get(String url, String method, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		RequestParams mods = new RequestParams();

		mods.put("app", "api");
		mods.put("mod", url);
		mods.put("act", method);

		client.setTimeout(TIME_OUT_INTERVAL);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);
		VolleyLog.d("%s ? %s & %s", BASE_URL, mods.toString(), params.toString());
		client.post(BASE_URL + "?" + mods.toString(), params, responseHandler);
	}

	/**
	 * 志愿者更新接口
	 * 
	 * @param responseHandler
	 */
	public static void getVolunteer(AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(TIME_OUT_INTERVAL);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);
		client.setUserAgent("Android_PU");
		client.get(VOLUNTEER_UPDATE_URL, null, responseHandler);
	}

	/**
	 * 获取游戏列表接口
	 * 
	 * @param responseHandler
	 */
	public static void getGame(AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(TIME_OUT_INTERVAL);
		client.getHttpClient().getParams()
				.setParameter("http.protocol.allow-circular-redirects", true);
		client.get(GAME_LIST_URL, null, responseHandler);
	}
}
