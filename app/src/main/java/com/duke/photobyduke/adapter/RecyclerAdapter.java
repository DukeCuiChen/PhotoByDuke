package com.duke.photobyduke.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duke.photobyduke.AppBaseActivity;
import com.duke.photobyduke.DetailActivity;
import com.duke.photobyduke.R;
import com.duke.photobyduke.entity.CinemaBean;
import com.duke.photobyduke.utils.PageArgs;
import com.duke.photobyduke.utils.Utils;
import com.infrastructure.utils.LogWrapper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ITEM = 0;

    private final ArrayList<CinemaBean> cinemaList;
    private final AppBaseActivity context;

    private ItemViewHolder holder;

    public RecyclerAdapter(ArrayList<CinemaBean> cinemaList,
                           AppBaseActivity context) {
        this.cinemaList = cinemaList;
        this.context = context;

    }

    //创建item的时候调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogWrapper.logD("onCreateViewHolder");
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cinemalist, parent, false);
        holder = new ItemViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] location = new int[2];
                itemView.getLocationOnScreen(location);

                PageArgs args = new PageArgs();
                args.setX(location[0]);
                args.setY(location[1] - Utils.getStatusBarHeight(context));
                Bundle bundle =(Bundle) v.getTag();
                if(bundle != null){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("args", args);
                    intent.putExtra("com.duke.photobyduke.item", bundle);
//                    intent.putExtra("bitmap", itemBitmap);
                    LogWrapper.logD("???????????????");
                    context.startActivity(intent);
                } else {
                    Snackbar.make(v, "Wait a moment", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        return holder;
    }

    //显示Item的时候调用
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogWrapper.logD("onBindViewHolder");
        if(holder instanceof  ItemViewHolder) {
            final CinemaBean cinemaBean = cinemaList.get(position);
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tvCinemaTitle.setText(cinemaBean.getCinemaTitle());
//            context.imageLoader.displayImage(cinemaBean.getCinemaPhotoUrl(), viewHolder.imgPhoto, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    LogWrapper.logD("onLoadingComplete:" + loadedImage);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("bitmap", loadedImage);
//                    bundle.putString("imageUri", imageUri);
//                    viewHolder.itemView.setTag(bundle);
//
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//
//                }
//            });

            context.imageLoader.loadImage(cinemaBean.getCinemaPhotoUrl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    LogWrapper.logD("onLoadingStarted view:" + view);
                    viewHolder.imgPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    LogWrapper.logD("onLoadingComplete width:" + loadedImage.getWidth() + ", height:" + loadedImage.getHeight());
                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("bitmap", loadedImage);
                    bundle.putString("imageUri", imageUri);
                    bundle.putString("text", cinemaBean.getCinemaTitle());
                    viewHolder.itemView.setTag(bundle);
                    viewHolder.imgPhoto.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return cinemaList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;

    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvCinemaTitle;
        ImageView imgPhoto;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCinemaTitle = (TextView) itemView.findViewById(R.id.item_title);
            imgPhoto = (ImageView) itemView.findViewById(R.id.item_imgPhoto);
        }
    }



}
