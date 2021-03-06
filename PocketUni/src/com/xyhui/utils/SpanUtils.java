package com.xyhui.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import com.xyhui.activity.app.GameCenterActivity;
import com.xyhui.activity.weibo.UserHomePageActivity;
import com.xyhui.activity.weibo.WeiboTopicViewActivity;

public class SpanUtils {

	public static SpannableString txtToImg(final Context mActivity, String content) {
		content = content.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")
				.replace("&quot;", "\"");
		SpannableString ss = new SpannableString(content);

		try {
			// String[] emotions = new String[] { "tongue", "smile", "lol", "loveliness", "titter",
			// "biggrin", "shy", "sweat", "yun", "ku", "yiwen", "mad", "fendou", "funk",
			// "cry", "sad", "ha", "huffy", "pig", "kiss", "victory", "ok", "handshake",
			// "cake", "hug", "beer", "call", "time", "moon", "shocked", "yi" };
			String[] emotions = new String[] { "tongue", "smile", "lol", "loveliness", "titter",
					"biggrin", "shy", "sweat", "yun", "ku", "88", "mad", "fendou", "funk", "cry",
					"sad", "ha", "huffy", "pig", "guzhang", "victory", "ok", "tu", "cake", "hug",
					"beer", "call", "time", "moon", "hei", "shocked" };

			{
				int starts = -1;
				int end = -1;

				starts = content.indexOf("[", starts + 1);
				end = content.indexOf("]", end + 1);

				while (starts != -1 && end != -1 && starts < end) {

					String phrase = content.substring(starts, end + 1);
					String imageName = "";

					for (String emotion : emotions) {
						if (phrase.equalsIgnoreCase("[" + emotion + "]")) {
							imageName = emotion;
							break;
						}
					}

					try {

						if (!TextUtils.isEmpty(imageName)) {
							String uri = "face_" + imageName;
							int i = mActivity.getResources().getIdentifier(uri, "drawable",
									mActivity.getPackageName());
							if (i > 0) {
								Drawable drawable = mActivity.getResources().getDrawable(i);
								drawable.setBounds(0, 0, 32, 32);
								ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
								ss.setSpan(span, starts, end + 1,
										Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					starts = content.indexOf("[", starts + 1);
					end = content.indexOf("]", end + 1);
				}
			}

			{
				// 匹配一个或者多个的字母数字或者中文
				Pattern pattern = Pattern.compile("@([\\w\u4e00-\u9fa5])+");

				Matcher matcher = pattern.matcher(content);
				// 查找符合pattern的字符串
				while (matcher.find()) {
					final String matched = matcher.group();

					ClickableSpan click_span = new ClickableSpan() {
						@Override
						public void onClick(View widget) {
							// VolleyLog.d("Image Clicked :%s", matched);

							Intent intent = new Intent();
							intent.setClass(mActivity, UserHomePageActivity.class);
							intent.putExtra(Params.INTENT_EXTRA.USERNAME, matched);
							mActivity.startActivity(intent);
						}
					};
					ss.setSpan(click_span, matcher.start(), matcher.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}

			{
				// 匹配头尾是#，中间包含一个或者多个非#字符
				Pattern pattern = Pattern.compile("#[^#]+#");

				Matcher matcher = pattern.matcher(content);
				// 查找符合pattern的字符串
				while (matcher.find()) {
					// 获取专题ID
					final String matched = matcher.group().replace('#', '\0');

					ClickableSpan click_span = new ClickableSpan() {
						@Override
						public void onClick(View widget) {
							// VolleyLog.d("Image Clicked :%s", matched);

							Intent intent = new Intent();
							intent.setClass(mActivity, WeiboTopicViewActivity.class);
							intent.putExtra(Params.INTENT_EXTRA.WEIBO_TOPIC_ID, matched);
							mActivity.startActivity(intent);
						}
					};
					ss.setSpan(click_span, matcher.start(), matcher.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}

			// {
			// 匹配以“invitation=一个或多个数字-一个或多个数字”结尾的链接
			// Pattern invitePattern = Pattern
			// .compile("((http://|https://){1}[\\w\\.\\-/\\?=&:]+)invitation=\\d+-\\d+$");
			// final Matcher inviteMatcher = invitePattern.matcher(content);
			//
			// while (inviteMatcher.find()) {
			// final String inviteStr = inviteMatcher.group();
			// ClickableSpan click_span = new ClickableSpan() {
			// @Override
			// public void onClick(View widget) {
			//
			// int index = inviteStr.indexOf('-');
			// String fromUid = inviteStr.substring(11, index);
			// String toUid = inviteStr.substring(index + 1, inviteStr.length());
			//
			// VolleyLog.d("Local Uid: %s , toUid: %s",
			// new PrefUtil().getPreference(Params.LOCAL.UID), toUid);
			//
			// if (new PrefUtil().getPreference(Params.LOCAL.UID).equals(toUid)) {
			// FileUtil.writeContent("flag=" + fromUid, "invite.txt");
			// }
			//
			// VolleyLog.d("Clicked :%s", inviteStr);
			//
			// Uri uri = Uri.parse(inviteStr);
			// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			// mActivity.startActivity(intent);
			// }
			// };
			//
			// ss.setSpan(click_span, inviteMatcher.start(), inviteMatcher.end(),
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// }
			// }

			{
				// 匹配一个链接
				Pattern pattern = Pattern.compile("((http://|https://){1}[\\w\\.\\-/\\?=&:]+)");
				// 通过match（）创建Matcher实例
				Matcher matcher = pattern.matcher(content);
				while (matcher.find())// 查找符合pattern的字符串
				{

					final String matched = matcher.group();
					int start = matcher.start();
					int end = matcher.end();
					ClickableSpan click_span;

					if (matched.indexOf("type=gameinvite") >= 0) {
						click_span = new ClickableSpan() {
							@Override
							public void onClick(View widget) {
								// VolleyLog.d("游戏中心 :%s", matched);

								// 打开游戏中心
								Intent intent = new Intent();
								intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, matched);
								intent.setClass(mActivity, GameCenterActivity.class);
								mActivity.startActivity(intent);
							}
						};
						ss = new SpannableString(content.replace(matched, "接受邀请"));
						ss.setSpan(click_span, start, start + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else {
						click_span = new ClickableSpan() {
							@Override
							public void onClick(View widget) {
								// VolleyLog.d("一般网页 :%s", matched);

								Uri uri = Uri.parse(matched);
								Intent intent = new Intent(Intent.ACTION_VIEW, uri);
								mActivity.startActivity(intent);
							}
						};
						if (matched.indexOf("type=apply") >= 0) {
							StringBuilder sb = new StringBuilder(content);
							sb.delete(start - 9, start);
							sb.delete(end - 9, end + 3);
							ss = new SpannableString(sb.toString().replace(matched, "【点此】"));
							ss.setSpan(click_span, start - 9, start - 5,
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						} else {
							ss.setSpan(click_span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ss;
	}

	public static SpannableString txtToImg(Context mContext, URLImageParserSmall p, String content) {

		// String[] emotions = new String[] { "tongue", "smile", "lol", "loveliness", "titter",
		// "biggrin", "shy", "sweat", "yun", "ku", "yiwen", "mad", "fendou", "funk", "cry",
		// "sad", "ha", "huffy", "pig", "kiss", "victory", "ok", "handshake", "cake", "hug",
		// "beer", "call", "time", "moon", "shocked", "yi" };

		String[] emotions = new String[] { "tongue", "smile", "lol", "loveliness", "titter",
				"biggrin", "shy", "sweat", "yun", "ku", "88", "mad", "fendou", "funk", "cry",
				"sad", "ha", "huffy", "pig", "guzhang", "victory", "ok", "tu", "cake", "hug",
				"beer", "call", "time", "moon", "hei", "shocked" };

		content = htmlspecialchars_decode_ENT_NOQUOTES(content);

		Spanned s = Html.fromHtml(content, p, null);

		SpannableString ss = new SpannableString(s);
		int starts = -1;
		int end = -1;

		starts = content.indexOf("[", starts + 1);
		end = content.indexOf("]", end + 1);

		while (starts != -1 && end != -1 && starts < end) {
			// VolleyLog.d("got starts:%d", starts);
			String phrase = content.substring(starts, end + 1);
			String imageName = "";

			for (String emotion : emotions) {
				// VolleyLog.d("got emotion: %s  phrase: %s", emotion, phrase);
				if (phrase.equalsIgnoreCase("[" + emotion + "]")) {
					imageName = emotion;
					break;
				}
			}

			try {
				if (!TextUtils.isEmpty(imageName)) {
					String uri = "face_" + imageName;
					int i = mContext.getResources().getIdentifier(uri, "drawable",
							mContext.getPackageName());
					if (i > 0) {
						Drawable drawable = mContext.getResources().getDrawable(i);
						drawable.setBounds(0, 0, 32, 32);
						ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
						ss.setSpan(span, starts, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			starts = content.indexOf("[", starts + 1);
			end = content.indexOf("]", end + 1);
		}
		return ss;
	}

	static Hashtable<String, String> html_specialchars_table = new Hashtable<String, String>();
	static {
		html_specialchars_table.put("&#091;", "[");
		html_specialchars_table.put("&#093;", "]");
	}

	static String htmlspecialchars_decode_ENT_NOQUOTES(String s) {
		Enumeration<String> en = html_specialchars_table.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String val = (String) html_specialchars_table.get(key);
			s = s.replaceAll(key, val);
		}
		return s;
	}

	public static String getTimeString(String createdAt) {
		try {
			long time = Long.parseLong(createdAt);
			Date date = new Date(time * 1000);
			String timeString = "";
			timeString = new SimpleDateFormat("yyyy-M-d H:m").format(date);
			return timeString;
		} catch (Exception e) {
			return "";
		}
	}

	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static int IndexOfAny(String test, String[] values, int index) {
		int first = -1;
		for (String item : values) {
			int i = test.indexOf(item, index);
			if (i >= 0) {
				if (first > 0) {
					if (i < first) {
						first = i;
					}
				} else {
					first = i;
				}
			}
		}
		return first;
	}
}
