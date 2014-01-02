package com.arise.ariseproject1.adapter;

import java.util.ArrayList;
import java.util.List;

import com.arise.ariseproject1.ChatListFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChatPagerAdapter extends FragmentPagerAdapter {

	private List<ChatListFragment> chatFragments = new ArrayList<ChatListFragment>();
	
	public ChatPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	public void addNewListFragment() {
		chatFragments.add(new ChatListFragment());
	}

	@Override
    public ChatListFragment getItem(int index) {
		return chatFragments.get(index);
    }
 
    @Override
    public int getCount() {
        return chatFragments.size();
    }
 
    
}
