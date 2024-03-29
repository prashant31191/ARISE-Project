package com.arise.ariseproject1;

import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;
import org.lucasr.twowayview.TwoWayView.ChoiceMode;

import com.arise.ariseproject1.adapter.ChatBarAdapter;
import com.arise.ariseproject1.adapter.ChatPagerAdapter;
import com.arise.ariseproject1.adapter.LazyPWLUCSAdapter;
import com.arise.ariseproject1.classes.ChatUser;
import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.PWLUCS;
import com.arise.ariseproject1.classes.SessionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends FragmentActivity implements OnClickListener{

	private Button b_add, b_send;
	private EditText et_myMsg;
	private TwoWayView chatBar;
	private static SessionManager session;
	private static ChatBarAdapter chatBarAdapter;
    private static ViewPager viewPager;
    private static ChatPagerAdapter cpAdapter;
	private DisplayImageOptions dioptions;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);


		dioptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_image)
				.showImageForEmptyUri(R.drawable.default_image)
				.showImageOnFail(R.drawable.default_image)
				.imageScaleType(ImageScaleType.NONE)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc(true).build();
		
		session = new SessionManager(getApplicationContext());
		
		int chatCase = getIntent().getExtras().getInt(CommonUtilities.TAG_CHAT_CASE);

		//long cpid = getIntent().getExtras().getLong(CommonUtilities.TAG_MESSAGE_CPID);
		
		b_add = (Button)findViewById(R.id.button_chat_activity_add);
		b_send = (Button)findViewById(R.id.button_chat_activity_send);
		et_myMsg = (EditText)findViewById(R.id.editText_chat_activity_message);
		chatBar = (TwoWayView)findViewById(R.id.twoWayView_chat_activity_chatBar);
		viewPager = (ViewPager)findViewById(R.id.viewPager_chat_activity);
 
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		
        // Initialization
        cpAdapter = new ChatPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(100); // seemingly infinite
        viewPager.setAdapter(cpAdapter);
        chatBarAdapter = new ChatBarAdapter(getApplicationContext(), R.layout.item_chat_tab,imageLoader,dioptions);
        initializeChatBarWithPWLUCS();
        chatBar.setAdapter(chatBarAdapter);
        chatBar.setChoiceMode(ChoiceMode.SINGLE);
        
		if(chatCase == 1){

			long uid = getIntent().getExtras().getInt(CommonUtilities.TAG_UID);
			openTheSelectedUsersWindow(uid);
		}
        
        /**
         * on clicking the chatbar tab make respective viewpager chat selected
         * */
        chatBar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				
				viewPager.setCurrentItem(position);
				resetNewMsgCount(position);
				
			}
		});
		

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
            	selectTab(position);}
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        b_add.setOnClickListener(this);
        b_send.setOnClickListener(this);

	}

	private void selectTab(int position) {
        chatBar.setSelection(position);
        chatBar.performItemClick(chatBar.getSelectedView(), position, chatBar.getItemIdAtPosition(position));	
	}

	private void openTheSelectedUsersWindow(long uid) {
		
		for(int i=0;i<chatBarAdapter.getCount();i++){
			if(uid == chatBarAdapter.getItem(i).getUid()){
				selectTab(i);
			}
		}
		
	}

	private void initializeChatBarWithPWLUCS() {

		for(int i=0;i<session.getPWLUCS().size();i++){
			chatBarAdapter.add(new ChatUser(session.getPWLUCS().get(i).getName(), session.getPWLUCS().get(i).getImage(), session.getPWLUCS().get(i).getUid(), 0, session.getPWLUCS().get(i).getGcmRegId()));
			chatBarAdapter.notifyDataSetChanged();
			cpAdapter.addNewListFragment();
			cpAdapter.notifyDataSetChanged();
		}
		
	}

	private static void resetNewMsgCount(int position) {
		
        chatBarAdapter.getItem(position).setNoOfMsgs(0);
        chatBarAdapter.notifyDataSetChanged();
		
	}

	private static void setNewMsgCount(int position) {

		chatBarAdapter.getItem(position).setNoOfMsgs((chatBarAdapter.getItem(position).getNoOfMsgs()+1));
        chatBarAdapter.notifyDataSetChanged();
		
	}
	
	public static void newMessageReceived(String message) throws JSONException{
    	
		JSONObject jObject = new JSONObject(message);
		long from_uid = Long.valueOf(jObject.getString(CommonUtilities.TAG_MESSAGE_SENDER_UID));
		//String from_image = jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_IMAGE);
		//String from_name = jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_NAME);
		//long cpid = Long.valueOf(jObject.getString(CommonUtilities.TAG_MESSAGE_CPID));
		String content = jObject.getString(CommonUtilities.TAG_MESSAGE_CONTENT);
		String time = jObject.getString(CommonUtilities.TAG_MESSAGE_TIME);
		//String time = jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_UID);
		
		for(int i=0; i<chatBarAdapter.getCount();i++){
			if(from_uid == chatBarAdapter.getChatUserList().get(i).getUid()){
				setNewMsgCount(i);
				cpAdapter.getItem(i).receive(content,time);
				break;
			}
			if(i == (chatBarAdapter.getCount()-1)){
				for(int j=0;j<session.getPWCSUL().size();j++){
					if(from_uid == session.getPWCSUL().get(j).getUid()){
						chatBarAdapter.add(new ChatUser(session.getPWCSUL().get(j).getName(), session.getPWCSUL().get(j).getImage(), session.getPWCSUL().get(j).getUid(), 1, session.getPWLUCS().get(i).getGcmRegId()));
						chatBarAdapter.notifyDataSetChanged();
						cpAdapter.addNewListFragment();
						cpAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}
		
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		/*
		case R.id.button_chat_activity_add:{
			showChatUserListDialog();
		}
		break;
		*/
		case R.id.button_chat_activity_send:{
			
			cpAdapter.getItem(viewPager.getCurrentItem()).send(et_myMsg.getText().toString(),chatBarAdapter.getItem(chatBar.getSelectedItemPosition()).getUid(),chatBarAdapter.getItem(chatBar.getSelectedItemPosition()).getGcmRegId());
			et_myMsg.setText("");
		}
		break;
		
		}
		
	}       
 
    @SuppressWarnings("unused")
	private void showChatUserListDialog() {
		 
		// custom dialog
		final Dialog dialog = new Dialog(getApplicationContext());
		dialog.setContentView(R.layout.dialog_chat_user_list);
		dialog.setTitle("Select");
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		ListView lv_chat_users = (ListView)dialog.findViewById(R.id.listView_dialog_chat_user_list);
        final LazyPWLUCSAdapter peopleAdapter = new LazyPWLUCSAdapter(getApplicationContext(), 0, imageLoader, dioptions);
        int pwlucs_list_size = session.getPWLUCS().size();
        int chat_users_list_size = chatBarAdapter.getCount();
        if(pwlucs_list_size == 0){
        	dialog.cancel();
        } 
        if(pwlucs_list_size != chat_users_list_size){
        	
        	if(pwlucs_list_size != 0 && chat_users_list_size == 0){
        		peopleAdapter.addAll(session.getPWLUCS());
        	} else{

        		for(int i=0;i<pwlucs_list_size;i++){
        			for(int j=0;j<chat_users_list_size;j++){
        				
        				if(session.getPWLUCS().get(i).getUid() == chatBarAdapter.getItem(j).getUid()){
        					break;
        				} else if(j == (chat_users_list_size-1)){
        					peopleAdapter.add(session.getPWLUCS().get(i));
        				}
        			}
        		}
        	}
        lv_chat_users.setAdapter(peopleAdapter);
        
        lv_chat_users.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				    PWLUCS person = peopleAdapter.getItem(position);
				    String name = person.getName();
				    String image = person.getImage();
				    long uid = person.getUid();
				    String gcm_regid = person.getGcmRegId();
				    //long cpid = person.getCpid();
					chatBarAdapter.add(new ChatUser(name, image, uid, 0, gcm_regid));
					int size = chatBarAdapter.getCount();
					size--;
					chatBarAdapter.notifyDataSetChanged();
			        cpAdapter.addNewListFragment();
			        cpAdapter.notifyDataSetChanged();
	                chatBar.setSelection(size);
			        chatBar.performItemClick(chatBar.getSelectedView(), chatBar.getSelectedItemPosition(),  chatBar.getItemIdAtPosition(position));
			        viewPager.setCurrentItem((size));
			        dialog.cancel();
				
			}
		});
        
        } else{
        	dialog.setTitle("No user left to chat with!");
        }
        

		dialog.show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
	}

}
