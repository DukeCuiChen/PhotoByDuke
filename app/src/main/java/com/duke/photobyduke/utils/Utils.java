package com.duke.photobyduke.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.DisplayMetrics;

import com.infrastructure.activity.BaseActivity;
import com.infrastructure.utils.BaseUtils;

public class Utils extends BaseUtils {
	/**
	 * 
	 * @Title: convertToInt
	 * @Description: 对象转化为整数数字类型
	 * @param value
	 * @param defaultValue
	 * @return integer
	 * @throws
	 */
	public final static int convertToInt(Object value, int defaultValue) {
		if (value == null || "".equals(value.toString().trim())) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value.toString());
		} catch (Exception e) {
			try {
				return Double.valueOf(value.toString()).intValue();
			} catch (Exception e1) {
				return defaultValue;
			}
		}
	}
	
	/**
	 * 
	 * @Title: createProgressDialog
	 * @Description: 创建ProgressDialog
	 * @param activity
	 * @param msg
	 * @return ProgressDialog
	 */
    public static ProgressDialog createProgressDialog(final BaseActivity activity, final String msg)
    {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

	public static int getHeight(Context context) {
		if (context instanceof Activity) {
			DisplayMetrics displaysMetrics = new DisplayMetrics();
			((Activity) context).getWindowManager()
					.getDefaultDisplay()
					.getMetrics(displaysMetrics);
			//得到屏幕高
			return displaysMetrics.heightPixels;
		}
		return 0;
	}

	public static int getWidth(Context context) {
		if (context instanceof Activity) {
			DisplayMetrics displaysMetrics = new DisplayMetrics();
			((Activity) context).getWindowManager()
					.getDefaultDisplay()
					.getMetrics(displaysMetrics);

			//得到屏幕宽
			return displaysMetrics.widthPixels;
		}
		return 0;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
