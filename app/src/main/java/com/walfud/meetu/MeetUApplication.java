package com.walfud.meetu;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.walfud.common.WallE;
import com.walfud.libpuller.Puller;
import com.walfud.meetu.util.UniversalImageLoaderOption;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = this;

        if (BuildConfig.DEBUG) {
            MobclickAgent.setDebugMode(true);
            Bugtags.start("b27243a172339c9df358ab036868ec05", this, Bugtags.BTGInvocationEventBubble);
        }

        Puller.getInstance().initialize(this);

        WallE.initialize();

        initUniversalImageLoader();
    }

    private static Context sApplicationContext;
    public static Context getContext() {
        return sApplicationContext;
    }

    // Internal
    private void initUniversalImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                .discCacheExtraOptions(480, 800, null)
//                .taskExecutor(...)
//                .taskExecutorForCachedImages(...)
//                .threadPoolSize(3) // default
//                .threadPriority(Thread.NORM_PRIORITY - 2) // default
//                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
//                .memoryCacheSizePercentage(13) // default
//                .discCache(new UnlimitedDiscCache(cacheDir)) // default
//                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileCount(100)
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(context)) // default
//                .imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(UniversalImageLoaderOption.getOption()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }
}
