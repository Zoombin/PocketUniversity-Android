/**
 * 
 */
package com.xyhui.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;

import com.mslibs.utils.VolleyLog;

/**
 * @author wuss
 * @since 2012/4/27
 */
public class Utils {

	public static final String GWAP = "3gwap";

	public static final String CMWAP = "cmwap";

	public static final String CTWAP = "ctwap";

	public static final String UNIWAP = "uniwap";

	public static final String WAP_PROXY_HOST = "10.0.0.172";

	public static final String CTWAP_PROXY_HOST = "10.0.0.200";

	/**
	 * Checks whether the url is secure or not.
	 * 
	 * @param url
	 *            The url to check.
	 */
	public static void checkUrlIsSecure(String url) {
		try {
			URL parsed = new URL(url);
			if (!parsed.getProtocol().toLowerCase(Locale.US).equals("https")
					&& !parsed.getHost().toLowerCase(Locale.US).endsWith("corp.google.com")) {
				throw new RuntimeException(new StringBuilder().append("Insecure URL: ")
						.append(url).toString());
			}
		} catch (MalformedURLException e) {
			VolleyLog.d(new StringBuilder().append("Cannot parse URL: ").append(url).toString(),
					new Object[0]);
		}
	}

	/**
	 * Make sure the method cannot be called from UI thread.
	 */
	public static void ensureNotOnMainThread() {
		// Prevent the HttpRequest from being sent on the main thread
		if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
			throw new IllegalStateException("This method cannot be called from the UI thread.");
		}
	}

	/**
	 * Make sure the method must be called from UI thread.
	 */
	public static void ensureOnMainThread() {
		if (Looper.myLooper() != null && Looper.myLooper() != Looper.getMainLooper()) {
			throw new IllegalStateException("This method must be called from the UI thread.");
		}
	}

	/**
	 * To check whether WIFI connection is available or not.
	 * 
	 * @param context
	 *            The application context.
	 * @return true WIFI is available, false otherwise.
	 */
	public static boolean isWifiAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiInfo == null) {
			return false;
		}
		return wifiInfo.isConnected();
	}

	/**
	 * Checks whether the current network is available or not.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		if (netInfo == null) {
			return false;
		}
		return netInfo.isConnected();
	}

	/**
	 * To check whether MOBILE connection is available or not.
	 * 
	 * @param context
	 *            The application context.
	 * @return true MOBILE connection is available,false otherwise.
	 */
	public static boolean isMobileAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileInfo == null) {
			return false;
		}
		return mobileInfo.isConnected();
	}

	/**
	 * To check whether MOBILE WAP connection is available or not.
	 * 
	 * @param context
	 *            The application context.
	 * @return true MOBILE WAP connection is available,false otherwise.
	 */
	public static boolean isMobileWapAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileInfo == null) {
			return false;
		}
		if (mobileInfo.isConnected()) {
			String extraInfo = mobileInfo.getExtraInfo();
			if (extraInfo != null
					&& (extraInfo.equals(CMWAP) || extraInfo.equals(CTWAP)
							|| extraInfo.equals(UNIWAP) || extraInfo.equals(GWAP))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	public static void setMobileApnproxy(Context context, HttpClient httpClient) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileInfo == null) {
			return;
		}
		if (mobileInfo.isConnected()) {
			String extraInfo = mobileInfo.getExtraInfo();
			if (extraInfo == null) {
				return;
			}
			HttpHost proxy = null;
			if (extraInfo.toLowerCase().equals(CMWAP) || extraInfo.toLowerCase().equals(UNIWAP)
					|| extraInfo.toLowerCase().equals(GWAP)) {
				proxy = new HttpHost(WAP_PROXY_HOST, 80);
			} else if (extraInfo.toLowerCase().equals(CTWAP)) {
				proxy = new HttpHost(CTWAP_PROXY_HOST, 80);
			}
			if (proxy != null) {
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
				VolleyLog.d("Use Proxy %s", new Object[] { proxy.toHostString() });
			}
		}
	}

	/**
	 * Checks whether the network type is mobile or not.
	 * 
	 * @param networkType
	 * @return true if the network type is mobile, false otherwise.
	 */
	public static boolean isNetworkTypeMobile(int networkType) {
		switch (networkType) {
		case ConnectivityManager.TYPE_MOBILE:
		case ConnectivityManager.TYPE_MOBILE_MMS:
		case ConnectivityManager.TYPE_MOBILE_SUPL:
		case ConnectivityManager.TYPE_MOBILE_DUN:
		case ConnectivityManager.TYPE_MOBILE_HIPRI:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Checks whether the network type is wifi or not.
	 * 
	 * @param networkType
	 * @return true if the network type is wifi, false otherwise.
	 */
	public static boolean isNetworkTypeWIFI(int networkType) {
		switch (networkType) {
		case ConnectivityManager.TYPE_WIFI:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Copy the content of the input stream into the output stream, using a temporary byte array
	 * buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
	 * 
	 * @param in
	 *            The input stream to copy from.
	 * @param out
	 *            The output stream to copy to.
	 * 
	 * @throws java.io.IOException
	 *             If any error occurs during the copy.
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[8 * 1024];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	/**
	 * Closes the specified stream.
	 * 
	 * @param stream
	 *            The stream to close.
	 */
	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				Log.e("closeStream", "Could not close stream", e);
			}
		}
	}

}
