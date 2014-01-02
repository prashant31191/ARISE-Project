package com.arise.ariseproject1.adapter;

import java.util.ArrayList;
import java.util.List;

import com.arise.ariseproject1.R;
import com.arise.ariseproject1.classes.ChatUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatBarAdapter extends ArrayAdapter<ChatUser> {

	private static List<ChatUser> chatUsers = new ArrayList<ChatUser>();
	private ImageLoader imageLoader;
	private DisplayImageOptions dioptions;

	@Override
	public void add(ChatUser object) {
		chatUsers.add(object);
		super.add(object);
	}

	public ChatBarAdapter(Context context, int textViewResourceId, ImageLoader imageLoader, DisplayImageOptions diOptions) {
		super(context, textViewResourceId);
		this.imageLoader = imageLoader;
		this.dioptions = diOptions;
	}
	
	@Override
	public int getCount() {
		return this.chatUsers.size();
	}
	
	@Override
	public ChatUser getItem(int index) {
		return this.chatUsers.get(index);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.item_chat_tab, parent, false);
		}

		ImageView iv_image = (ImageView) row.findViewById(R.id.imageView_item_chat_tab_image);
		TextView tv_msgCount = (TextView) row.findViewById(R.id.textView_item_chat_tab_msg_count);

		ChatUser user = getItem(position);
		iv_image.setImageResource(R.drawable.tc);
		
		int noOfMsgs = user.getNoOfMsgs();
		if(noOfMsgs == 0){
			tv_msgCount.setVisibility(View.GONE);
		}
		else if(noOfMsgs > 0 && noOfMsgs <= 9){
			tv_msgCount.setVisibility(View.VISIBLE);
			tv_msgCount.setText(Integer.toString(noOfMsgs));
		}
		else if(noOfMsgs > 9){
			tv_msgCount.setVisibility(View.VISIBLE);
			tv_msgCount.setText("9+");
		}

		return row;
	}

}