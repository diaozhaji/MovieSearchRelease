package com.NG.activity;

import com.NG.moviesearch.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class GuideActivity extends Activity {
	
	// 用来存储是否为第一次使用的Preference
	SharedPreferences settings;
	SharedPreferences.Editor editor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 若不为第一次使用，则跳过使用向导
		settings = getSharedPreferences("setting", MODE_PRIVATE);
		editor = settings.edit();
		Boolean isFirst = settings.getBoolean("isFirst", true);
		if (!isFirst) {
			changeActivity();
			return;
		}else{
			editor.putBoolean("isFirst", false);
			editor.commit();
		}

		setContentView(R.layout.guide_activity);
		Button startButton = (Button) findViewById(R.id.guide_btn);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 若为最后一张，则进入程序界面，否则顺序加载下一张图片
				
//					editor.putBoolean("isFirst", false);
//					editor.commit();
					changeActivity();
			}
		});
		
	}

	// 进入程序界面
	private void changeActivity() {
		Intent intent = new Intent(GuideActivity.this, Home.class);
		GuideActivity.this.startActivity(intent);
		GuideActivity.this.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		GuideActivity.this.finish();
	}
}
