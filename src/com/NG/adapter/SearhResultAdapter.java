﻿package com.NG.adapter;


import java.util.ArrayList;
import java.util.List;

import com.NG.activity.Home;
import com.NG.cache.ImageLoader;
import com.NG.entity.SingleEntity;
import com.NG.moviesearchbeta.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearhResultAdapter extends BaseAdapter{
	
	private static final String TAG = "SearhResultAdapter";
	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}
	
	private Context mContext;
	private List<SingleEntity> aList;	
	private ImageLoader mImageLoader;
	
	
	public SearhResultAdapter(Context context, List<SingleEntity> seList) {
		this.mContext = context;
		aList = seList;
		mImageLoader = new ImageLoader(context);
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return aList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return aList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.search_result_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.all_title);
			viewHolder.contentTextView = (TextView)convertView
					.findViewById(R.id.all_content);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.allimageview);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}		
		final SingleEntity movieBriefPojo = aList.get(position);
		
		viewHolder.mTextView.setText(movieBriefPojo.getMovieName());
		/*
		String content = "";
		if(!movieBriefPojo.getAuthorName().equals(null)){
			content += "导演：" + movieBriefPojo.getAuthorName()+"\n";
		}
		content += "地区："+movieBriefPojo.getCountries()
				+"\n"+"时间："+movieBriefPojo.getYear()
				+"\t\t"+"评分："+movieBriefPojo.getRating_average();
		if(movieBriefPojo.getAdjs()!=""){
			content += "\n"+"情感："+movieBriefPojo.getAdjs();
		}
		viewHolder.contentTextView.setText(content);*/
		
		viewHolder.contentTextView.setText(
				"导演：" + movieBriefPojo.getAuthorName()
				+"\n"+"地区："+movieBriefPojo.getCountries()
				+"\n"+"时间："+movieBriefPojo.getYear()+"\t\t"+"评分："+movieBriefPojo.getRating_average()
				+"\n"+"情感："+movieBriefPojo.getAdjs()
				);	
		viewHolder.mImageView.setBackgroundResource(R.drawable.rc_item_bg);
		
		String url = movieBriefPojo.getImageUrl();
		Log.d(TAG, url);
		if (!mBusy) {
			mImageLoader.DisplayImage(url, viewHolder.mImageView, false);
		} else {
			mImageLoader.DisplayImage(url, viewHolder.mImageView, false);
		}
		
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("点点点点点点点点");
				Activity activity = (Activity) mContext;
				Intent intent = new Intent(mContext, com.NG.activity.MovieDetailPage.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", movieBriefPojo.getFirstUrl().toString());
				bundle.putString("imageurl", movieBriefPojo.getImageUrl().toString());
				// bundle.putString("type", "电影");
				intent.putExtras(bundle);
				
				activity.startActivity(intent);
				
			}
			
		});
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView mTextView;
		TextView contentTextView;
		ImageView mImageView;
		
	}

}
