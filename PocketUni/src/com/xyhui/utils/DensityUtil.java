package com.xyhui.utils;

import android.content.Context;

public class DensityUtil {
	public static int dip2px(Context paramContext, float paramFloat) {
		float f = paramContext.getResources().getDisplayMetrics().density;
		return (int) (paramFloat * f + 0.5F);
	}

	public static int px2dip(Context paramContext, float paramFloat) {
		float f = paramContext.getResources().getDisplayMetrics().density;
		return (int) (paramFloat / f + 0.5F);
	}

	public static int sp2px(Context paramContext, float paramFloat) {
		float f = paramContext.getResources().getDisplayMetrics().scaledDensity;
		return (int) (paramFloat * f + 0.5F);
	}

	public static int px2sp(Context paramContext, float paramFloat) {
		float f = paramContext.getResources().getDisplayMetrics().scaledDensity;
		return (int) (paramFloat / f + 0.5F);
	}
}