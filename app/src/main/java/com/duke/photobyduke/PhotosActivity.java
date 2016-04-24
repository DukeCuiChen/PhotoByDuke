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
import com.duke.photobyduke.entity.URLGetRecentInfo;
import com.duke.photobyduke.net.ApiList;
import com.duke.photobyduke.net.UrlConfig;
import com.infrastructure.net.DefaultThreadPool;
import com.infrastructure.net.OkHttpRequestTest;
import com.infrastructure.net.RequestParameter;
import com.infrastructure.net.URLData;
import com.infrastructure.net.UrlConfigManager;
import com.infrastructure.utils.LogWrapper;

import java.io.InputStream;
import java.util.ArrayList;

public class PhotosActivity extends AppBaseActivity{
	private RecyclerView mRecycleView;
//	private ListView mListView;
	
	private AbstractRequestCallback photoCallback;
	private ArrayList<URLGetRecentInfo> urlList;

	private boolean isRefresh;
	@Override
	protected void initVariables() {

	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_scrolling);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		mRecycleView = (RecyclerView) findViewById(R.id.recycler);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		LogWrapper.logD("animator or not :" +  mLayoutManager.supportsPredictiveItemAnimations());
		mRecycleView.setLayoutManager(mLayoutManager);
		mRecycleView.setItemAnimator(new DefaultItemAnimator());

//		mListView = (ListView) findViewById(R.id.list);
//		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_list);

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
				urlList = UrlConfig.getURLList(in, isRefresh);
				runOnUiThread(new Runnable() {
					public void run() {
						isRefresh = false;
						refreshLayout.setRefreshing(false);
						RecyclerAdapter adapter = new RecyclerAdapter(getArrayCinemaBean(urlList), PhotosActivity.this);
						mRecycleView.setAdapter(adapter);
//						ListAdapter listAdapter = new ListAdapter(getArrayCinemaBean(urlList), PhotosActivity.this);
//						mListView.setAdapter(listAdapter);
					}
				});

			}
		};
		
		ArrayList<RequestParameter> params = new ArrayList<>();
		RequestParameter rp1 = new RequestParameter("method", "flickr.photos.getRecent");
		RequestParameter rp2 = new RequestParameter("api_key", "842283d9ea2b11eb980609b618bdda66");
		params.add(rp1);
		params.add(rp2);

		final URLData urlData = UrlConfigManager.findURL(this, "FlickerAPI");
		if(isRefresh){
			urlData.setIsRefresh(isRefresh);
//			isRefresh = false;
		}
//		HttpRequest request = getRequestManager().createRequest(
//				urlData, params, photoCallback);
		OkHttpRequestTest request = new OkHttpRequestTest(urlData, params, photoCallback);
		DefaultThreadPool.getInstance().execute(request);
		
	}
	
	private ArrayList<CinemaBean> getArrayCinemaBean(ArrayList<URLGetRecentInfo> list){
		ArrayList<CinemaBean> cinemaBeanList = new ArrayList<>();

		for(URLGetRecentInfo urlInfo : list){
			CinemaBean mCinemaBean = new CinemaBean();
			mCinemaBean.setCinemaTitle(urlInfo.getTitle());
			mCinemaBean.setCinemaPhotoUrl(String.format(ApiList.photoApi, urlInfo.getFarm(),
					urlInfo.getServer(), urlInfo.getId(), urlInfo.getSecret()));
			cinemaBeanList.add(mCinemaBean);
		}
		return cinemaBeanList;
		
	}
	
}
