package com.chatapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.chatapp.Callbacks.ResponseCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageUtils {

    private ImageUtils() {

    }

    /**
     * Crops image into a circle that fits within the ImageView.
     */
    public static void displayRoundImageFromUrl(final Context context, final String url, final ImageView imageView) {
        if (context != null && url != null && imageView != null) {
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop()
                    .dontAnimate();

            Glide.with(context)
                    .asBitmap()
                    .apply(myOptions)
                    .load(url)
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    public static void displayImageFromUrl(final Context context, final String url,
                                           final ImageView imageView, Drawable placeholderDrawable) {
        displayImageFromUrl(context, url, imageView, placeholderDrawable, null);
    }

    /**
     * Displays an image from a URL in an ImageView.
     */
    public static void displayImageFromUrl(final Context context, final String url,
                                           final ImageView imageView, Drawable placeholderDrawable, RequestListener listener) {
        if (context != null && url != null && imageView != null && placeholderDrawable != null && listener != null) {
            RequestOptions myOptions = new RequestOptions()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(placeholderDrawable);

            if (listener != null) {
                Glide.with(context)
                        .load(Uri.parse(url))
                        .apply(myOptions)
                        .listener(listener)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(myOptions)
                        .listener(listener)
                        .into(imageView);
            }
        }
    }

    public static void displayRoundImageFromUrlWithoutCache(final Context context,
                                                            final String url,
                                                            final ImageView imageView) {
        displayRoundImageFromUrlWithoutCache(context, url, imageView, null);
    }

    public static void displayRoundImageFromUrlWithoutCache(final Context context,
                                                            final String url,
                                                            final ImageView imageView, RequestListener listener) {
        if (context != null && url != null && imageView != null && listener != null) {
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);

            if (listener != null) {
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .apply(myOptions)
                        .listener(listener)
                        .into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .apply(myOptions)
                        .into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
        }
    }

    /**
     * Displays an image from a URL in an ImageView.
     * If the image is loading or nonexistent, displays the specified placeholder image instead.
     */
    public static void displayImageFromUrl(final Context context,
                                           final String url,
                                           final ImageView imageView) {
        if (context != null && url != null && imageView != null) {
            RequestOptions myOptions = new RequestOptions()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

            Glide.with(context)
                    .load(url)
                    .apply(myOptions)
                    .into(imageView);
        }
    }

    /**
     * Displays an image from a URL in an ImageView.
     */
    public static void displayGifImageFromUrl(Context context, String url, ImageView
            imageView, Drawable placeholderDrawable, RequestListener listener) {
        if (context != null && url != null && imageView != null && listener != null && placeholderDrawable != null) {
            RequestOptions myOptions = new RequestOptions()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(placeholderDrawable);

            if (listener != null) {
                Glide.with(context)
                        .asGif()
                        .load(url)
                        .apply(myOptions)
                        .listener(listener)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .asGif()
                        .load(url)
                        .apply(myOptions)
                        .into(imageView);
            }
        }
    }

    /**
     * Displays an GIF image from a URL in an ImageView.
     */
    public static void displayGifImageFromUrl(Context context, String url, ImageView
            imageView, String thumbnailUrl, Drawable placeholderDrawable) {
        if (context != null && url != null && imageView != null && thumbnailUrl != null && placeholderDrawable != null) {
            RequestOptions myOptions = new RequestOptions()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(placeholderDrawable);

            if (thumbnailUrl != null) {
                Glide.with(context)
                        .asGif()
                        .load(url)
                        .apply(myOptions)
                        .thumbnail(Glide.with(context).asGif().load(thumbnailUrl))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .asGif()
                        .load(url)
                        .apply(myOptions)
                        .into(imageView);
            }
        }
    }

    public static void displayImagefromUri(Context context, Uri uri, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .dontAnimate()
                .override(Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    public static void displayThumbnailimages(Context context, String url, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .dontAnimate()
                .override(40)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(context)
                .load(url)
                .thumbnail(0.2f)
                .apply(myOptions)
                .into(imageView);
    }

    public static void saveBitmapToFile(final File file, final ResponseCallback<File> responseCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // BitmapFactory options to downsize the image
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    o.inSampleSize = 6;
                    // factor of downsizing the image

                    FileInputStream inputStream = new FileInputStream(file);
                    //Bitmap selectedBitmap = null;
                    BitmapFactory.decodeStream(inputStream, null, o);
                    inputStream.close();

                    // The new size we want to scale to
                    final int REQUIRED_SIZE = 40;

                    // Find the correct scale value. It should be the power of 2.
                    int scale = 1;
                    while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                            o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                        scale *= 2;
                    }

                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    inputStream = new FileInputStream(file);

                    Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
                    inputStream.close();

                    // here i override the original image file
                    file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);

                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    responseCallback.responseCallback(file);

                } catch (Exception e) {
                    responseCallback.responseCallback(null);
                }
            }
        }).start();

    }
}
