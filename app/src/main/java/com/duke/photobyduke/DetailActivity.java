package com.duke.photobyduke;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.duke.photobyduke.utils.MarginProxyAnimator;
import com.duke.photobyduke.utils.PageArgs;
import com.duke.photobyduke.utils.Utils;
import com.infrastructure.utils.LogWrapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by duke on 2016/3/25.
 */
public class DetailActivity extends AppBaseActivity {
    private final int MARGIN_TOP = 60;
    private PageArgs args;
    private CardView mLayout;
    private ImageView imageView;
    private PhotoViewAttacher mAttacher;
    private String imageUri;
    private String text;

    private Handler mHandler;
    private DisplayImageOptions options;
    private FrameLayout frameLayout;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        args = intent.getParcelableExtra("args");
        imageUri = intent.getBundleExtra("com.duke.photobyduke.item").getString("imageUri");
        text = intent.getBundleExtra("com.duke.photobyduke.item").getString("text");
        mHandler = new Handler();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        //设置左滑关闭的Activity的功能
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        mLayout = (CardView) findViewById(R.id.detail_layout);
        imageView = (ImageView) findViewById(R.id.detail_image);
        frameLayout = (FrameLayout) findViewById(R.id.detail_frame);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setLayout(DetailActivity.this, mLayout, args.getX(), args.getY(), dip2px(DetailActivity.this, 95));
                mLayout.setVisibility(View.VISIBLE);
                setLayout(DetailActivity.this, imageView, dip2px(DetailActivity.this, 5), dip2px(DetailActivity.this, 5), dip2px(DetailActivity.this, 85));
                imageView.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(imageUri, imageView, options);


                int translationY = args.getY() - dip2px(DetailActivity.this, MARGIN_TOP);
                ObjectAnimator layoutAnim = ObjectAnimator.ofInt(new MarginProxyAnimator(mLayout), "height", dip2px(DetailActivity.this, 95), Utils.getHeight(DetailActivity.this)- dip2px(DetailActivity.this, MARGIN_TOP));
                ObjectAnimator layoutAnimY = ObjectAnimator.ofFloat(mLayout, View.TRANSLATION_Y, 0, -translationY);
                ObjectAnimator layoutBgAnim = ObjectAnimator.ofObject(mLayout, "cardBackgroundColor", new ArgbEvaluator(), 0x00FFFFFF, 0xFFFFFFFF);
                layoutBgAnim.setInterpolator(new AccelerateInterpolator());     //动画播放的速率，有几种默认的方式
                layoutBgAnim.setDuration(400);                                  //设置动画播放持续的时间，单位是毫秒
                layoutBgAnim.start();

                int imageViewTranslationY = (Utils.getHeight(DetailActivity.this)/2 - dip2px(DetailActivity.this, 350)/2) - MARGIN_TOP;
//                ObjectAnimator imageViewAnimX = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0, Utils.getWidth(DetailActivity.this)/2-dip2px(DetailActivity.this, 115));
                LogWrapper.logD("position height:" + (Utils.getHeight(DetailActivity.this) / 2 - dip2px(DetailActivity.this, 350) / 2));
                LogWrapper.logD("imageViewTranslationY:" + imageViewTranslationY);
                ObjectAnimator imageViewAnimY = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, 0, imageViewTranslationY);
                ObjectAnimator imageViewAnimHeight = ObjectAnimator.ofInt(new MarginProxyAnimator(imageView), "height", dip2px(DetailActivity.this, 85), dip2px(DetailActivity.this, 350));
                ObjectAnimator imageViewAnimWidth = ObjectAnimator.ofInt(new MarginProxyAnimator(imageView), "width", dip2px(DetailActivity.this, 85),
                        Utils.getWidth(DetailActivity.this)-dip2px(DetailActivity.this, 20));

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSet.setStartDelay(300);
                animatorSet.setDuration(800);
                animatorSet.play(layoutAnim);
                animatorSet.play(layoutAnimY);
    //            animatorSet.play(layoutBgAnim);

//                animatorSet.play(imageViewAnimX);
                animatorSet.play(imageViewAnimY);
                animatorSet.play(imageViewAnimHeight);
                animatorSet.play(imageViewAnimWidth);
                animatorSet.start();
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        LogWrapper.logD("onAnimationEnd width:" + imageView.getWidth() + ", height:" + imageView.getHeight());
                        ImageLoader.getInstance().displayImage(imageUri, imageView, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                //设置图片可以进行手势的缩放
                                mAttacher = new PhotoViewAttacher(imageView);
                                mAttacher.update();
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }, 250);

    }

    @Override
    protected void loadData() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Snackbar.make(frameLayout, text, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 设置相对于父组件的位置
     * @param context 上下文对象
     * @param view 所要被设置的View的布局
     * @param x 距离父组件左边的距离
     * @param y 距离父组件的上边的距离
     * @param height 该View的高度
     */
    private void setLayout(Activity context, View view, int x, int y, int height) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
//        Log.d("duke", "left:" + x );
//        Log.d("duke", "top:" + y );
//        Log.d("duke", "right:" + x);
//        Log.d("duke", "bottom:" + (y + height));
        margin.height = height;
        margin.setMargins(x, y, x, y + margin.height);          //距离左上右下的距离，而不是直接的坐标
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    //测量单位的转化，dp值转像素
    public static int dip2px(Context context, float dpValue) {
        //dip->px
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.getResources().getDisplayMetrics());
    }
}
