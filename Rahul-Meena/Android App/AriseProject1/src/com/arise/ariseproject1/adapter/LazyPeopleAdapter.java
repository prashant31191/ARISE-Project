package com.arise.ariseproject1.adapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.CommonUtilities;
import com.arise.ariseproject1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyPeopleAdapter extends BaseAdapter{

	private Context context;
	private JSONArray people;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions dioptions;
	
	public LazyPeopleAdapter(Context context, JSONArray people, LayoutInflater inflater,ImageLoader imageLoader,DisplayImageOptions dioptions){
		this.context = context;
		this.people = people;
		this.inflater = inflater;
		this.imageLoader = imageLoader;
		this.dioptions = dioptions;
	}

	@Override
	public int getCount() {
		return people.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	View imageLayout = new View(context); 
        if (convertView == null) {
            imageLayout = inflater.inflate(R.layout.item_lazy_people, parent, false);
        }
        else {
            imageLayout = (View) convertView;
        }
        
		try {
			JSONObject c = people.getJSONObject(position);
	        
	        ImageView iv_dp = (ImageView)imageLayout.findViewById(R.id.imageView_item_lazy_people_dp);
	        TextView tv_name = (TextView)imageLayout.findViewById(R.id.textView_item_lazy_people_name);
	        
	        String dpUri = c.getString(CommonUtilities.TAG_IMAGE);
	        String name = c.getString(CommonUtilities.TAG_NAME);
	        imageLoader.displayImage(dpUri, iv_dp,dioptions);
	        tv_name.setText(name);
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return imageLayout;
	}

}
