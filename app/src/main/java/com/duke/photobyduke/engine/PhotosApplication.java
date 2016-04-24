package com.duke.photobyduke.engine;

import android.app.Application;

import com.duke.photobyduke.R;
import com.infrastructure.cache.CacheManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class PhotosApplication extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate();

		CacheManager.getInstance().initCacheDir(this);

		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory()
				.cacheOnDisc()
				.build();
		
		ImageLoaderConfiguration config = 
				new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheExtraOptions(480, 480)
				.memoryCacheSize(2 * 1024 * 1024)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new WeakMemoryCache())
//				.writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(config);
	}

}
