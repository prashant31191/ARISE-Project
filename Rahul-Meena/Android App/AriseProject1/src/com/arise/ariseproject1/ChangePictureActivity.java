package com.arise.ariseproject1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.SessionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePictureActivity extends Activity {
	
	private static final int SELECT_PICTURE = 0;

	// UI elements
	private Button b_select,b_upload,b_cancel;
	private ImageView iv_image;

	
    // Session Manager Class
	private SessionManager session;
    
   	// Progress Dialog
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_picture);
 
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		session = new SessionManager(getApplicationContext());
		iv_image = (ImageView)findViewById(R.id.imageView_change_picture_activity_image);
		iv_image.setImageDrawable(iv_image.getDrawable());
		b_select = (Button) findViewById(R.id.button_change_picture_activity_select);
		b_upload = (Button) findViewById(R.id.button_change_picture_activity_upload);
		b_cancel = (Button) findViewById(R.id.button_change_picture_activity_cancel);
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				switch (v.getId()){
				
				case R.id.button_change_picture_activity_select:{
			    	Intent intent = new Intent();
			    	intent.setType("image/*");
			    	intent.setAction(Intent.ACTION_GET_CONTENT);
			    	startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
				}
				break;
				
				case R.id.button_change_picture_activity_upload:{
					uploadPhoto(iv_image.getDrawable());
				}
				break;
				
				case R.id.button_change_picture_activity_cancel:{
					finish();
				}
				break;
				}
				
			}
		};
		
		b_select.setOnClickListener(ocl);
		b_upload.setOnClickListener(ocl);
		b_cancel.setOnClickListener(ocl);
	}

    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == SELECT_PICTURE) {
    		Bitmap bitmap = getPath(data.getData());
    		iv_image.setImageBitmap(bitmap);
    	}
    }
        
        private Bitmap getPath(Uri uri) {
     
    		String[] projection = { MediaStore.Images.Media.DATA };
    		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
    		int column_index = cursor
    				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    		cursor.moveToFirst();
    		String filePath = cursor.getString(column_index);
    		cursor.close();
    		// Convert file path into bitmap image using below line.
    		return decodeFile(filePath);
    	}
        
        public Bitmap decodeFile(String filePath) {
         
                // Decode image size
         BitmapFactory.Options o = new BitmapFactory.Options();
         o.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, o);

         // The new size we want to scale to
         final int REQUIRED_SIZE = 240;

         // Find the correct scale value. It should be the power of 2.
         int width_tmp = o.outWidth, height_tmp = o.outHeight;
         int scale = 1;
         while (true) {
          if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                  break;
          width_tmp /= 2;
          height_tmp /= 2;
          scale *= 2;
         }

         // Decode with inSampleSize
         BitmapFactory.Options o2 = new BitmapFactory.Options();
         o2.inSampleSize = scale;
         return BitmapFactory.decodeFile(filePath, o2);
        }
        
        public void uploadPhoto(Drawable drawable) {
        	try {
    			UpdateImage mUpdateImage = new UpdateImage();
    			mUpdateImage.execute(drawable);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        
        public void executeMultipartPost() throws Exception {
     
    		try {
     
    		} catch (Exception e) {
     
    			// handle exception here
    			e.printStackTrace();
     
    			// Log.e(e.getClass().getName(), e.getMessage());
     
    		}
     
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_picture, menu);
		return true;
	}




	   /**
	    * Background Async Task to Load all product by making HTTP Request
	    * */
	   public class UpdateImage extends AsyncTask<Drawable, String, String> {

	       /**
	        * Before starting background thread Show Progress Dialog
	        * */
	       @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	           pDialog = new ProgressDialog(ChangePictureActivity.this);
	           pDialog.setMessage("Updating...");
	           pDialog.setIndeterminate(false);
	           pDialog.setCancelable(false);
	           pDialog.show();
	       }

	       /**
	        * getting All products from url
	     * @return 
	        * */
	       protected String doInBackground(Drawable... args) {
	    	     
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				
				BitmapDrawable drawable = (BitmapDrawable) args[0];
				
				Bitmap bitmap = drawable.getBitmap();

				bitmap.compress(CompressFormat.JPEG, 50, bos);

				byte[] data = bos.toByteArray();

				HttpClient httpClient = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(CommonUtilities.SERVER_UPLOAD_IMAGE_URL);

				String fileName = session.getUserID()+".jpg";
				ByteArrayBody bab = new ByteArrayBody(data, fileName);

				// File file= new File("/mnt/sdcard/forest.png");

				// FileBody bin = new FileBody(file);

				MultipartEntity reqEntity = new MultipartEntity(

				HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("file", bab);

				postRequest.setEntity(reqEntity);
				int timeoutConnection = 60000;
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				int timeoutSocket = 60000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				HttpConnectionParams.setTcpNoDelay(httpParameters, true);

				HttpResponse response;
				try {
					response = httpClient.execute(postRequest);

					BufferedReader reader = new BufferedReader(new InputStreamReader(

					response.getEntity().getContent(), "UTF-8"));

					String sResponse;

					StringBuilder s = new StringBuilder();

					while ((sResponse = reader.readLine()) != null) {

						s = s.append(sResponse);

					}

					System.out.println("Response: " + s);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	           return null;
	       }

	       /**
	        * After completing background task Dismiss the progress dialog
	        * **/
	       protected void onPostExecute(String result) {
	           // dismiss the dialog after getting all products
	           pDialog.dismiss();
	           ProfileFragment.isImageUpdated = true;
	           Toast.makeText(getApplicationContext(), "Picture successfully updated!", Toast.LENGTH_LONG).show();
	           finish();

	       }
	   }

}
