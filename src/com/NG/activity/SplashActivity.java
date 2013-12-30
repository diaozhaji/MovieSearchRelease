package com.NG.activity;


import com.NG.moviesearchbeta.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class SplashActivity extends Activity{
	private final int SPLASH_DISPLAY_LENGHT = 3000; // Splash display time in ms
	
	//private ImageView imageview;
	//private TextView tv;
	private int displayWidth;
	private int displayHeight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//取得屏幕分辨率，以在特效播放中使用
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidth=dm.widthPixels;
        displayHeight=dm.heightPixels;
        float density = dm.density;
        System.out.println(displayHeight);
        System.out.println(displayWidth);
        System.out.println(density);
        
        setContentView(R.layout.splash);
        
        checkInternet();
        
        new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent nextIntent = new Intent();
				
				nextIntent.setClass(SplashActivity.this, Home.class);
					
				SplashActivity.this.startActivity(nextIntent);
				SplashActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
        
        
        
        /*
		tv = (TextView)findViewById(R.id.splash_text);
        tv.setText("your screen height = "+displayHeight +"\n"
        			+"width = "+displayWidth+"\n"
        			+"density = "+density);
        */
        
        /*
        imageview = (ImageView)findViewById(R.id.splash_img);
		
		imageview.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
    			intent.setClass(SplashActivity.this,MainActivity.class);
    			//intent.setClass(SplashActivity.this,ViewPagerActivity.class);
				//intent.setClass(SplashActivity.this,detailTest.class);
    			startActivity(intent);          		
    			SplashActivity.this.finish();
			}
			
		});
		*/
				
	}

	public void checkInternet() {
		ConnectivityManager connMgr = (ConnectivityManager) 
					getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
            //new DownloadWebpageTask().execute(stringUrl);
			
        } else {
        	Toast.makeText(getApplicationContext(), "没有可用网络连接",
        		     Toast.LENGTH_SHORT).show();
        }

	}

}
