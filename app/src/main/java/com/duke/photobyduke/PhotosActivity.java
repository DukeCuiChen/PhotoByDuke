package com.duke.photobyduke;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import com.duke.photobyduke.adapter.RecyclerAdapter;
import com.duke.photobyduke.entity.CinemaBean;
import com.duke.photobyduke.entity.URLgetRecentInfo;
import com.duke.photobyduke.net.ApiList;
import com.duke.photobyduke.net.UrlConfig;
import com.infrastructure.net.DefaultThreadPool;
import com.infrastructure.net.HttpRequest;
import com.infrastructure.net.RequestParameter;
import com.infrastructure.net.URLData;
import com.infrastructure.net.UrlConfigManager;
import com.infrastructure.utils.LogWrapper;

import java.io.InputStream;
import java.util.ArrayList;

public class PhotosActivity extends AppBaseActivity{
	private RecyclerView mrecycleLayout;
	
	private AbstractRequestCallback photoCallback;
	private ArrayList<URLgetRecentInfo> urlList;

	private boolean isRefresh;
	@Override
	protected void initVariables() {

	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_scrolling);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		mrecycleLayout = (RecyclerView) findViewById(R.id.recycler);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mrecycleLayout.setLayoutManager(mLayoutManager);
		mrecycleLayout.setItemAnimator(new DefaultItemAnimator());

		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
		refreshLayout.setColorSchemeResources(R.color.colorPrimary);
		refreshLayout.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				LogWrapper.logD("onRefresh");
				isRefresh = true;
				loadData();

			}
		});
		
		
	}

	@Override
	protected void loadData() {
		if (!isRefresh){
			refreshLayout.setRefreshing(true);
		}

		photoCallback = new AbstractRequestCallback() {
			
			@Override
			public void onSuccess(String content) {
			}

			@Override
			public void onSuccess(InputStream in) {
				LogWrapper.logD("duke3");
				urlList = UrlConfig.getURLList(in);
				runOnUiThread(new Runnable() {
					public void run() {
						refreshLayout.setRefreshing(false);
//						CinemaAdapter adapter = new CinemaAdapter(getArrayCinameBean(urlList), PhotosActivity.this);
						RecyclerAdapter adapter = new RecyclerAdapter(getArrayCinameBean(urlList), PhotosActivity.this);
						mrecycleLayout.setAdapter(adapter);
					}
				});

			}
		};
		
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		RequestParameter rp1 = new RequestParameter("method", "flickr.photos.getRecent");
		RequestParameter rp2 = new RequestParameter("api_key", "842283d9ea2b11eb980609b618bdda66");
		params.add(rp1);
		params.add(rp2);

		final URLData urlData = UrlConfigManager.findURL(this, "FlickerAPI");
		if(isRefresh){
			urlData.setExpires(0);
			isRefresh = false;
		}
		HttpRequest request = getRequestManager().createRequest(
				urlData, params, photoCallback);
		DefaultThreadPool.getInstance().execute(request);
		
	}
	
	private ArrayList<CinemaBean> getArrayCinameBean(ArrayList<URLgetRecentInfo> list){
		ArrayList<CinemaBean> cinemaBeanList = new ArrayList<CinemaBean>();
//		CinemaBean mCb = new CinemaBean();
//		mCb.setCinemaTitle("test by duke");
////		mCb.setCinemaPhotoUrl(null);
//		cinemaBeanList.add(mCb);
		for(URLgetRecentInfo urlinfo : list){
			CinemaBean mcinemabean = new CinemaBean();
			mcinemabean.setCinemaTitle(urlinfo.getTitle());
			mcinemabean.setCinemaPhotoUrl(String.format(ApiList.photoApi, urlinfo.getFarm(),
					urlinfo.getServer(), urlinfo.getId(), urlinfo.getSecret()));
//			LogWrapper.logD("photoUrl:" + String.format(ApiList.photoApi, urlinfo.getFarm(),
//					urlinfo.getServer(), urlinfo.getId(), urlinfo.getSecret()));
			cinemaBeanList.add(mcinemabean);
		}
		return cinemaBeanList;
		
	}
	
}
