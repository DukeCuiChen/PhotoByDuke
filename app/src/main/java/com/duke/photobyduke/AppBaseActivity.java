package com.duke.photobyduke;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;

import com.infrastructure.activity.BaseActivity;
import com.infrastructure.net.RequestCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class AppBaseActivity extends BaseActivity {
	protected boolean needCallback;

	protected SwipeRefreshLayout refreshLayout;

	public ImageLoader imageLoader = ImageLoader.getInstance();

	protected void onDestroy() {
		//回收该页面缓存在内存的图片
		imageLoader.clearMemoryCache();

		super.onDestroy();
	}

	
	public abstract class AbstractRequestCallback implements RequestCallback {

		public abstract void onSuccess(String content);

		public void onFail(String errorMessage) {
			refreshLayout.setRefreshing(false);

			new AlertDialog.Builder(AppBaseActivity.this).setTitle("出错啦")
					.setMessage(errorMessage).setPositiveButton("确定", null)
					.show();
		}

		public void onCookieExpired() {
			refreshLayout.setRefreshing(false);

			new AlertDialog.Builder(AppBaseActivity.this)
					.setTitle("出错啦")
					.setMessage("Cookie过期，请重新登录")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
//									Intent intent = new Intent(
//											AppBaseActivity.this,
//											LoginActivity.class);
//									intent.putExtra(AppConstants.NeedCallback,
//											true);
//									startActivity(intent);
								}
							}).show();
		}
	}		
}
