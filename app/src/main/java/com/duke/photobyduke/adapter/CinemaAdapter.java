package com.duke.photobyduke.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duke.photobyduke.AppBaseActivity;
import com.duke.photobyduke.R;
import com.duke.photobyduke.entity.CinemaBean;
import com.infrastructure.utils.LogWrapper;

import java.util.ArrayList;

public class CinemaAdapter extends BaseAdapter {
	private final ArrayList<CinemaBean> cinemaList;
	private final AppBaseActivity context;
	private int count=0;

	public CinemaAdapter(ArrayList<CinemaBean> cinemaList,
			AppBaseActivity context) {
		this.cinemaList = cinemaList;
		this.context = context;
		
	}

	public int getCount() {
		return cinemaList.size();
	}

	public CinemaBean getItem(final int position) {
		return cinemaList.get(position);
	}

	public long getItemId(final int position) {

		return position;
	}

	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		LogWrapper.logD("count:" + count++);
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = context.getLayoutInflater().inflate(
					R.layout.item_cinemalist, null);
			holder.tvCinemaTitle= (TextView) convertView
					.findViewById(R.id.item_title);
			holder.imgPhoto = (ImageView) convertView
					.findViewById(R.id.item_imgPhoto);
			holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		CinemaBean cinema = cinemaList.get(position);
		holder.tvCinemaTitle.setText(cinema.getCinemaTitle());

		context.imageLoader.displayImage(cinemaList.get(position)
				.getCinemaPhotoUrl(), holder.imgPhoto);

		return convertView;
	}

	class Holder {
		TextView tvCinemaTitle;
		ImageView imgPhoto;
	}
}
