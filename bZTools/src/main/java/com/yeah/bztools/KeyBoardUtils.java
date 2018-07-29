package com.yeah.bztools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardUtils {
	/**
	 * 隐藏软键盘
	 * @param context
	 * @param view
	 */
	public static void hideSoftKeyBoard(Context context,View view) {
		// 1.得到InputMethodManager对象
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 获取状态信息
		boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
		if (isOpen) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
		}
	}
	/**
	 * 显示软件盘
	 * @param context
	 * @param view
	 */
	public static void showSoftKeyBoard(Context context,View view) {
		// 1.得到InputMethodManager对象
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 获取状态信息
		boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
		if (!isOpen) {
			// 调用showSoftInput方法显示软键盘，其中view为聚焦的view组件
			 imm.showSoftInput(view,InputMethodManager.SHOW_FORCED); // 显示藏键盘
		}
	}
}
