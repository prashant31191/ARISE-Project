package com.arise.ariseproject1;

import java.util.Random;

import com.arise.ariseproject1.adapter.ChatSessionArrayAdapter;
import com.arise.ariseproject1.classes.OneComment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatListFragment extends ListFragment {

	ChatSessionArrayAdapter adapter;
	private static Random random;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		adapter = new ChatSessionArrayAdapter(getActivity().getApplicationContext(), R.layout.listitem_discuss);
		setListAdapter(adapter);
		random = new Random();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void send(String msg){

		adapter.add(new OneComment(false, msg));
		addItems();
		getListView().setSelection(getListView().getChildCount()-1);
	}

	private void addItems() {
		adapter.add(new OneComment(true, "Hello bubbles!"));

		for (int i = 0; i < 4; i++) {
			boolean left = getRandomInteger(0, 1) == 0 ? true : false;
			String words = "adsadasdas";

			adapter.add(new OneComment(left, words));
		}
	}

	private static int getRandomInteger(int aStart, int aEnd) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * random.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}
}
