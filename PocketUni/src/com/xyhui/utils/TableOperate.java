package com.xyhui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.xyhui.R;

public abstract class TableOperate {

	private static void setTabIndicator(TabSpec spec, String title, Drawable drawable, View view) {
		int sdk = new Integer(Build.VERSION.SDK_INT).intValue();
		if (sdk < 4) {
			spec.setIndicator(title);
		} else {
			spec.setIndicator(view);
		}
	}

	public static void addTab(TabHost host, String title, int drawable, String Tag, int layout) {
		TabHost.TabSpec spec = host.newTabSpec(Tag);
		spec.setContent(layout);
		View view = prepareTabView(host.getContext(), title, drawable);
		TableOperate.setTabIndicator(spec, title,
				host.getContext().getResources().getDrawable(drawable), view);
		host.addTab(spec);
	}

	public static void addTab(TabHost host, String title, int drawable, String Tag, Intent intent) {
		TabHost.TabSpec spec = host.newTabSpec(Tag);
		spec.setContent(intent);
		View view = prepareTabView(host.getContext(), title, drawable);
		TableOperate.setTabIndicator(spec, title,
				host.getContext().getResources().getDrawable(drawable), view);
		host.addTab(spec);
	}

	private static View prepareTabView(Context context, String text, int drawable) {
		View view;
		if (TextUtils.isEmpty(text)) {
			view = LayoutInflater.from(context).inflate(R.layout.tab_main_app, null);
		} else {
			view = LayoutInflater.from(context).inflate(R.layout.tab_main_nav, null);
			TextView tv = (TextView) view.findViewById(R.id.tab_main_nav_tvTitle);
			tv.setText(text);
			ImageView iv = (ImageView) view.findViewById(R.id.tab_main_nav_ivIcon);
			iv.setImageResource(drawable);
		}

		return view;
	}

	public static void OnTabChanged(String tabId, TabWidget mTabWidget, Resources mResources) {

	}
}
