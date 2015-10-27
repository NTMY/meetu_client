package com.walfud.meetu.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.walfud.common.DensityTransformer;
import com.walfud.meetu.MeetUApplication;

/**
 * Created by walfud on 2015/10/27.
 */
public class UniversalImageLoaderOption {

    private static DisplayImageOptions sOption;
    private static DisplayImageOptions sCircleOption;
    private static DisplayImageOptions sRoundedOption;

    public static DisplayImageOptions getOption() {
        if (sOption == null) {
            sOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
//                .resetViewBeforeLoading(false)  // default
//                .delayBeforeLoading(1000)
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
//                .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//                .decodingOptions(...)
//                .displayer(new SimpleBitmapDisplayer()) // default
//                .handler(new Handler()) // default
                    .build();
        }

        return sOption;
    }

    public static DisplayImageOptions getCircleOption() {
        if (sCircleOption == null) {
            sCircleOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
//                .resetViewBeforeLoading(false)  // default
//                .delayBeforeLoading(1000)
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
//                .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//                .decodingOptions(...)
                    .displayer(new RoundedBitmapDisplayer(Integer.MAX_VALUE)) // default
//                .handler(new Handler()) // default
                    .build();
        }

        return sCircleOption;
    }

    public static DisplayImageOptions getRoundedOption() {
        if (sRoundedOption == null) {
            sRoundedOption = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
//                .resetViewBeforeLoading(false)  // default
//                .delayBeforeLoading(1000)
                    .cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
//                .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//                .decodingOptions(...)
                    .displayer(new RoundedBitmapDisplayer(DensityTransformer.dp2px(MeetUApplication.getContext(), 10))) // default
//                .handler(new Handler()) // default
                    .build();
        }

        return sRoundedOption;
    }
}
