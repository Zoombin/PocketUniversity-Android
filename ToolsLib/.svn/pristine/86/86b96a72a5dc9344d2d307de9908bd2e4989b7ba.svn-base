package com.mslibs.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CRelativeLayout extends RelativeLayout {
	private int mResourceID;

	public CRelativeLayout(Context context) {
		super(context);
	}

	public CRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setContentView(int id) {
		mResourceID = id;
	}

	public void addContentView(int id) {
		addView(((Activity) getContext()).getLayoutInflater().inflate(id, null));
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (mResourceID != 0) {
			((Activity) getContext()).getLayoutInflater().inflate(mResourceID, this);
		}
	}
}
