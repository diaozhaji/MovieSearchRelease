package com.NG.adapter;

import java.util.List;
import java.util.Random;

import com.NG.moviesearch.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SquareAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private List<String> sqList;

	private String[] colors = { "#2d83fc", "#11b3a6", "#fa5c2c", "#c524c2",
			"#fd4871", "#ff2424", "#003664", "#f49ac0", "#e2d83b", "#34d106",
			"#07aed2", "#fcaf2d",
			};

	// "#0055cd", "#ac0128", "#ef65ec", "#4ab22b","#55dfd0", "#06f67c",
	// "#e42490", "#0a8015"
	/*
	 * private String[] colors = { "#fe8081"
	 * ,"#7eff80","#00fe85","#7efffe","#007dfd","#ff82c2","#ff80ff",
	 * "#fe0000","#fefe00"
	 * ,"#80ff00","#00ff41","#00ffff","#027fc1","#7f7cbf","#f905fa",
	 * "#7f3d3f","#f77a37"
	 * ,"#00ff00","#0c807f","#003681","#807fff","#86003c","#ff0082",
	 * "#7c0000","#ff8001"
	 * ,"#008000","#067d43","#0001fd","#0000a2","#7c017e","#410577",
	 * "#420004","#7f4203"
	 * ,"#023d03","#023e3c","#04027f","#2b2c2e","#3c013b","#3f0085",
	 * "#000002","#817f04","#7f8044","#868688","#408082","#c0c0c2","#400039", };
	 */

	public SquareAdapter(Context context, List<String> list) {
		super();
		mContext = context;
		inflater = LayoutInflater.from(context);
		sqList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != sqList) {
			return sqList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sqList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SquareViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.squared_item, null);
			viewHolder = new SquareViewHolder();
			viewHolder.block = (RelativeLayout) convertView
					.findViewById(R.id.squared_block);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.squared_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (SquareViewHolder) convertView.getTag();
		}
		final String sq = sqList.get(position);
		viewHolder.title.setText(sq);
		// viewHolder.title.setBackgroundColor(Color.BLUE);
		Random rnd = new Random();
		int p = rnd.nextInt(colors.length);
		viewHolder.block.setBackgroundColor(Color.parseColor(colors[p]));
		return convertView;
	}
}

class SquareViewHolder {
	public RelativeLayout block;
	public TextView title;
}
