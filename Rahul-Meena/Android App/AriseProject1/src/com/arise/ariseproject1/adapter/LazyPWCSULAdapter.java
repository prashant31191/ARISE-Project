package com.arise.ariseproject1.adapter;


import java.util.Collection;
import java.util.List;

import com.arise.ariseproject1.classes.PWCSUL;
import com.arise.ariseproject1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyPWCSULAdapter extends ArrayAdapter<PWCSUL>{

	private Context context;
	private List<PWCSUL> people;
	private ImageLoader imageLoader;
	private DisplayImageOptions dioptions;

	@Override
	public void add(PWCSUL object) {
		people.add(object);
		super.add(object);
	}

	@Override
	public void addAll(Collection<? extends PWCSUL> collection) {
		people.add((PWCSUL) collection);
		super.addAll(collection);
	}

	public LazyPWCSULAdapter(Context context,int id,ImageLoader imageLoader,DisplayImageOptions dioptions) {
		super(context, id);
		this.imageLoader = imageLoader;
		this.dioptions = dioptions;
	}

	@Override
	public int getCount() {
		return people.size();
	}

	@Override
	public PWCSUL getItem(int arg0) {
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
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLayout = inflater.inflate(R.layout.item_lazy_people, parent, false);
        }
        else {
            imageLayout = (View) convertView;
        }
        
			PWCSUL c = people.get(position);
	        
	        ImageView iv_dp = (ImageView)imageLayout.findViewById(R.id.imageView_item_lazy_people_dp);
	        TextView tv_name = (TextView)imageLayout.findViewById(R.id.textView_item_lazy_people_name);
	        
	        String dpUri = c.getName();
	        String name = c.getImage();
	        imageLoader.displayImage(dpUri, iv_dp,dioptions);
	        tv_name.setText(name);
	     
        
		return imageLayout;
	}
	
	public void removeItem(int position){
		people.remove(position);
	}

}
