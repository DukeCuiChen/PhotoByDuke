package com.duke.photobyduke.utils;

import android.view.View;
import android.widget.FrameLayout;
public class MarginProxyAnimator {

    private View mView;

    public MarginProxyAnimator(View view) {
        this.mView = view;
    }

    public int getHeight() {
        FrameLayout.LayoutParams  lp = (FrameLayout.LayoutParams) mView.getLayoutParams();
        return lp.height;
    }

    public void setHeight(int height) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mView.getLayoutParams();
        lp.height = height;
        mView.setLayoutParams(lp);

    }

    public int getWidth(){
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mView.getLayoutParams();
        return lp.width;
    }

    public void setWidth(int width){
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mView.getLayoutParams();
        lp.width = width;
        mView.setLayoutParams(lp);
    }

}
