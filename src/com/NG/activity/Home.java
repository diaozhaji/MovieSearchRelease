package com.NG.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.NG.adapter.SearchExampleAdapter;
import com.NG.adapter.SearhResultAdapter;
import com.NG.adapter.SquareAdapter;
import com.NG.entity.SingleEntity;
import com.NG.loader.SimpleInfoLoder;
import com.NG.moviesearchbeta.R;
import com.NG.utils.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class Home extends Activity {

	private ViewPager mViewPager;
	private ArrayList<View> mPageViews;

	// 该应用的主布局LinearLayout
	private ViewGroup mainViewGroup;
	private ViewGroup viewListView = null;
	private ViewGroup viewSquared = null;

	// 定义LayoutInflater
	LayoutInflater mInflater;

	// block
	private List<String> squaredList;
	private GridView gridView;
	private Context mContext;

	// search
	private ListView mlistView;// 存取搜索信息的列表控件
	private ImageView search_button;// 搜索按钮
	public EditText editText;// 搜索框

	private SearchExampleAdapter sAdapter; // 用于开始时的list
	private SearhResultAdapter mAdapater; // 用来加载BaseAdapater

	public List<SingleEntity> aList;// MovieBriefPojo 返回的泛型LIST
	private SingleEntity mbp;// 传递点击事件的 击点
	public List<String> sList;

	public Spinner sp;// 选择要搜索的类型

	private ProgressDialog proDialog;

	private String name;
	private Handler handler;
	String[] types = { "搜电影" };
	
	private static boolean flag = false; // 用于管理第一个List和结果List
	private static boolean isExit = false; // 用于管理是否退出应用

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		name = "戳泪点";

		initViewPager();

		initSquared();
		initView();
		initData();

	}

	private void initView() {

		mbp = new SingleEntity();
		initProDialog();
		mlistView = (ListView) viewListView
				.findViewById(R.id.search_result_list);

		search_button = (ImageView) mainViewGroup
				.findViewById(R.id.search_button);

		search_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				searchData();

				// mHandler.sendEmptyMessage(1);
			}
		});

		editText = (EditText) mainViewGroup.findViewById(R.id.edittext);
		editText.addTextChangedListener(new TextWatcher(){
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

                String editable = editText.getText().toString();
                String str = StringUtil.stringFilter(editable);
                if (!editable.equals(str)) {
                	editText.setText(str);
                }
                
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                
            }			
		});
		
		mlistView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				name = sList.get(arg2);
				editText.setText(name);
				searchData();
			}
			
		});

	}

	void initProDialog() {
		proDialog = new ProgressDialog(this);
		proDialog.setTitle(R.string.loading);
		proDialog.setMessage("请您耐心等待...");
	}

	public void searchData() {
		proDialog.show();
		name = editText.getText().toString();
		if (name.trim().length() < 1) {
			openOptionDialog("搜索条件");
			proDialog.dismiss();
			return;

		} else {
			new Thread(downloadRun).start();
		}

		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					// msg.obj是获取handler发送信息传来的数据
					List<SingleEntity> seList = (ArrayList<SingleEntity>) msg.obj;
					// 给ListView绑定数据
					try{
						showall(seList);
					}catch(Exception e){
						e.printStackTrace();
					}

				}
			}
		};
		// proDialog.dismiss();
	}

	Runnable downloadRun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				aList = new SimpleInfoLoder().findXml(name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendMessage(handler.obtainMessage(0, aList));
		}
	};

	/**
	 * @author tianqiujie 添加自定义布局器InfoAdapter
	 */

	public void showall(List<SingleEntity> list) {

		if (list == null) {
			// 没有结果情况
			Toast.makeText(getApplicationContext(), "没有返回结果",
					Toast.LENGTH_SHORT).show();
		} else {
			mAdapater = new SearhResultAdapter(this, list);

			mlistView.setAdapter(mAdapater);
			mlistView.setOnScrollListener(mScrollListener);
			flag = true;
		}
		mViewPager.setCurrentItem(0);
		proDialog.dismiss();
	}


	/**
	 * @author tianqiujie 当listView滚动停止以后才开始异步加载图片
	 */

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				mAdapater.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				mAdapater.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				mAdapater.setFlagBusy(false);
				break;
			default:
				break;
			}
			mAdapater.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};

	// 提示框方法
	public void openOptionDialog(String string) {
		new AlertDialog.Builder(this).setTitle("提示")
				.setMessage(string + "不能为空!")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	private void initData() {
		// TODO Auto-generated method stub
		sList = new ArrayList<String>();

		getStringList();

	}

	private void getStringList() {

		String[] a = { 
				"那些戳中泪点的电影",
				"我想看章子怡的电影",
				"张艺谋的电影",
				"宫崎骏有哪些优秀作品",
				"给爷找一些刺激的电影",
				"你能找到感人的电影么",
				"经典",
				"温暖午后轻松地赖在床上",
				"搜一下欢快的电影",
				"来一打惊悚的电影看看",
				"轻松的韩国电影",
				"成龙",
				"李连杰主演的电影",
				"斯皮尔伯格 战争",
				"美国 人性",
				"动画片"};
		for (int i = 0; i < a.length; i++) {
			sList.add(a[i]);
		}

		sAdapter = new SearchExampleAdapter(mContext, sList);
		mlistView.setAdapter(sAdapter);

	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		mInflater = getLayoutInflater();

		// 初始化ListView和GridView的内容
		mainViewGroup = (ViewGroup) mInflater.inflate(R.layout.home_activity,
				null);
		viewListView = (ViewGroup) mInflater.inflate(
				R.layout.search_result_page, null);
		viewSquared = (ViewGroup) mInflater.inflate(R.layout.squared_activity,
				null);

		// 初始化ViewPager的内容
		mPageViews = new ArrayList<View>();

		mPageViews.add(viewListView);
		mPageViews.add(viewSquared);

		mainViewGroup = (ViewGroup) mInflater.inflate(R.layout.home_activity,
				null);

		mViewPager = (ViewPager) mainViewGroup.findViewById(R.id.myviewpager);

		setContentView(mainViewGroup);

		mViewPager.setAdapter(new MyPagerAdapter());
		// 设置起始卡片
		// mViewPager.setCurrentItem(1);
	}

	private void initSquared() {
		// TODO Auto-generated method stub
		String[] a = { "章子怡", "王家卫", "搞笑的电影", "美国 人性", "还不错", "惊悚", "美国", "日本",
				"韩国", "奥斯卡", "动画", "冒险", "灾难", "爱情", "温暖 午后", "感人的", "悲伤",
				"2012", "科幻", "喜剧", "歌舞", "一米 阳光", "宫崎骏", "大卫林奇", "李连杰",
				"奥黛丽·赫本", "尼古拉斯·凯奇", "成龙", "世界和平", "李安", "汤姆·汉克斯", "玩具总动员",
				"斯皮尔伯格", "青春励志", "孤独", "文艺", "周星驰", "心情不好", "动作片", "僵尸", "美剧",
				"刘德华", "暴力", "战争", "经典", "浪漫的", "幽默", "1997", "好看的电影", "古装",
				"无聊", "纪念", "体育" };

		squaredList = new ArrayList<String>();

		for (int i = 0; i < 12; i++) {
			Random rnd = new Random();
			int p = rnd.nextInt(a.length);
			squaredList.add(a[p]);
		}

		gridView = (GridView) viewSquared.findViewById(R.id.squered_view);

		SquareAdapter sqadapter = new SquareAdapter(this, squaredList);
		sqadapter.getItem(0);
		gridView.setAdapter(sqadapter);
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				name = squaredList.get(arg2);
				editText.setText(name);
				searchData();
				mViewPager.setCurrentItem(0);
			}
			
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == 1){
					initSquared();
				}
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (flag == true) {

				mlistView.setAdapter(sAdapter);

				flag = false;
			} else {
				if (isExit == false) {
					isExit = true;
					Toast.makeText(this, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT)
							.show();

				} else {
					finish();
					System.exit(0);
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_DEL){
			System.out.println("KEYCODE_DEL");
		}
		
		return false;
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(mPageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(mPageViews.get(arg1));
			return mPageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}
}
