package com.mslibs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ms.R;

public class PagerIndicator extends CLinearLayout {

	private int mPagerCount;
	private LinearLayout mCurrentPoint;

	public PagerIndicator(Context context) {
		super(context);
	}

	public PagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setContentView(R.layout.widget_pager_indicator);
	}

	public PagerIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setPagerCount(int count) {
		mPagerCount = count;
		if (getChildCount() > 0) {
			removeAllViews();
		}

		for (int i = 0; i < mPagerCount; i++) {
			LinearLayout item = new LinearLayout(getContext());
			LayoutParams params = new LayoutParams(14, 14);
			params.setMargins(4, 0, 4, 0);
			item.setLayoutParams(params);
			if (i == 0) {
				item.setBackgroundResource(R.drawable.rounded8_o);
				mCurrentPoint = item;
			} else {
				item.setBackgroundResource(R.drawable.rounded8_n);
			}
			addView(item);
		}
	}

	public void setPagerIndex(int index) {
		if (index < getChildCount()) {
			LinearLayout item = (LinearLayout) getChildAt(index);
			item.setBackgroundResource(R.drawable.rounded8_o);
			// VolleyLog.d("setPagerIndex: %d item: %s", index, item.toString());
			if (mCurrentPoint != null) {
				mCurrentPoint.setBackgroundResource(R.drawable.rounded8_n);
				// VolleyLog.d("currentPoint: %s", mCurrentPoint.toString());
			}
			mCurrentPoint = item;
		}
	}
}
