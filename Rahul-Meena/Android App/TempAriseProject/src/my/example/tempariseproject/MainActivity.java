package my.example.tempariseproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	Button b_register,b_uml,b_radar,b_allUsers;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		b_allUsers=(Button)findViewById(R.id.button_main_all_users);
		b_register=(Button)findViewById(R.id.button_main_register);
		b_uml=(Button)findViewById(R.id.button_main_update_my_location);
		b_radar=(Button)findViewById(R.id.button_main_radar);
		
		b_allUsers.setOnClickListener(this);
		b_register.setOnClickListener(this);
		b_uml.setOnClickListener(this);
		b_radar.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.button_main_all_users:{
			intent = new Intent(getApplicationContext(),AllUsersActivity.class);
			startActivity(intent);
		}
		break;
		
		case R.id.button_main_register:{
			intent = new Intent(getApplicationContext(),RegisterActivity.class);
			startActivity(intent);
		}
		break;
		
		case R.id.button_main_update_my_location:{
			intent = new Intent(getApplicationContext(),UMLActivity.class);
			startActivity(intent);
		}
		break;
		
		case R.id.button_main_radar:{
			intent = new Intent(getApplicationContext(),RadarActivity.class);
			startActivity(intent);
		}
		break;
		}
		
	}

}
