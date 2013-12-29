﻿package com.NG.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.NG.entity.SingleEntity;
import com.NG.utils.StringUtil;

/**
 * 
 * @author tianqiujie 依据关键字(搜索条件) 返回对应API信息 并存入相应实体
 * 
 */
public class SimpleInfoLoder {
	/**
	 * 
	 * anthor Abel 2010-09-02
	 * 
	 * @param key
	 *            搜索的关键字
	 * @return 已存入数据的泛型LIST
	 */
	public List<SingleEntity> findXml(String key) throws IOException {
		List<SingleEntity> result = new ArrayList<SingleEntity>();
		String ch = URLEncoder.encode(key, "utf-8");

		//String uString = "http://api.douban.com/v2/movie/search?q=" + ch;
		String uString = "http://192.158.31.250/search/search/?command="+ch+"&start=0&count=50";
		URL url = new URL(uString);

		StringBuilder builder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(url.openStream()));
		for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
				.readLine()) {
			builder.append(s);
		}
		
		System.out.println(builder.toString());

		JSONArray jsonArray = null;
		try {
			JSONObject jsonObject = new JSONObject(builder.toString());
			jsonArray = jsonObject.getJSONArray("subjects");
		} catch (JSONException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
			try {
				//JSONObject imagesObj = jsonObject.getJSONObject("images");
				
				SingleEntity movieBriefPojo = new SingleEntity();
				movieBriefPojo.setMovieName(jsonObject.getString("title"));
				movieBriefPojo.setAuthorName(jsonObject.getString("raw_directors"));
				movieBriefPojo.setFirstUrl(jsonObject.getString("subject_id"));
				movieBriefPojo.setImageUrl(jsonObject.getString("image_small"));
				String raw_adjs = jsonObject.getString("raw_adjs");
				raw_adjs = StringUtil.dealAdjString(raw_adjs);
				movieBriefPojo.setAdjs(raw_adjs);
				String year = jsonObject.getString("year");
				year = StringUtil.StringToYear(year);
				movieBriefPojo.setYear(year);
				movieBriefPojo.setRating_average(jsonObject.getString("rating_average"));
				movieBriefPojo.setCountries(jsonObject.getString("countries"));
				//暂时没用
				String user_tags = jsonObject.getString("raw_user_tags");
				user_tags = StringUtil.dealUserTagsString(user_tags);
				movieBriefPojo.setUser_tags(user_tags);
				
				
				
				result.add(movieBriefPojo);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}
	
}
