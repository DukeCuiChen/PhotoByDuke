package com.duke.photobyduke.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

/**
 * Created by duke on 2016/4/23.
 */
public class ListAdapter extends BaseAdapter{

    private final ArrayList<CinemaBean> cinemaList;
    private final AppBaseActivity context;

    public ListAdapter(ArrayList<CinemaBean> cinemaList,
                       AppBaseActivity context) {
        this.cinemaList = cinemaList;
        this.context = context;
    }

    @Override
    public int getCount() {
        LogWrapper.logD("getCount");
        return cinemaList.size();
    }

    @Override
    public Object getItem(int position) {
        LogWrapper.logD("getItem:" + position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        LogWrapper.logD("getItemId:" + position);
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LogWrapper.logD("getView:" + position);
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cinemalist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView;
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_imgPhoto);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(R.id.item_imgPhoto, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.id.item_imgPhoto);
        }
        final CinemaBean cinemaBean = cinemaList.get(position);
        viewHolder.textView.setText( "" + position + "," + cinemaBean.getCinemaTitle());
        context.imageLoader.displayImage(cinemaBean.getCinemaPhotoUrl(), viewHolder.imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                LogWrapper.logD("" + position + "onLoadingComplete view:" + view + ",imageview:" + viewHolder.imageView);
                Bundle bundle = new Bundle();
                bundle.putString("text", cinemaBean.getCinemaTitle());
                bundle.putString("imageUri", imageUri);
                viewHolder.itemView.setTag(bundle);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int[] location = new int[2];
                viewHolder.itemView.getLocationOnScreen(viewHolder.location);

                PageArgs args = new PageArgs();
                args.setX(viewHolder.location[0]);
                args.setY(viewHolder.location[1] - Utils.getStatusBarHeight(context));
                Bundle bundle =(Bundle) v.getTag();
                if(bundle != null){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("args", args);
                    intent.putExtra("com.duke.photobyduke.item", bundle);
                    context.startActivity(intent);
                } else {
                    Snackbar.make(v, "Wait a moment", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        View itemView;
        ImageView imageView;
        TextView textView;
        int[] location = new int[2];//放在这个里面可以重用，更加经济
    }
}
