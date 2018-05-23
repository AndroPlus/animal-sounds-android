package com.androplus.AnimalBeats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.androplus.appirater.Appirater;
import com.androplus.crosspromote.HouseApps;
import com.androplus.fragment.HomeFragment;
import com.androplus.materialnavigationdrawer.MaterialNavigationDrawer;
/*import com.androplus.updatechecker.UpdateChecker;
import com.androplus.updatechecker.notice.Notice;*/


public class MainActivity extends MaterialNavigationDrawer /*implements BaseSliderView.OnSliderClickListener*/{
	private JSONArray subCategory;
	JSONObject jsonObject;
	JSONObject obj;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	public JSONArray jsonArray;

	//boolean isPlaying=false;
	@Override
	public void init(Bundle savedInstanceState) {
		((ApplicationManager) getApplication()).sendGA(t, "Home");
		 // set cover background
		//initMopubRewardVideo();
		preferences = getSharedPreferences(AppConstants.SETTINGS, 0);
		editor = preferences.edit();

		if ( preferences != null && !preferences.getBoolean(AppConstants.IS_FIRST_LAUNCH, false) ){
			editor.putBoolean(AppConstants.IS_FIRST_LAUNCH, true);
			AppConstants.isFirstLaunch = true;
			editor.apply();
		}else{
			AppConstants.isFirstLaunch = false;
		}
		this.setDrawerBackground(this.getResources().getDrawable(R.drawable.bamboo));
        // set user photo and data
       this.setUserPhoto(this.getResources().getDrawable(R.drawable.lion));
       this.setSecondAccountPhoto(this.getResources().getDrawable(R.drawable.wolf));
       this.setThirdAccountPhoto(this.getResources().getDrawable(R.drawable.tiger));
     //  this.setUsername("AnimalSounds for Kids");
       this.setUserEmail("Created by Andro+");
        try {
			jsonArray = new JSONArray(getFileFromAsset());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.get(i);
				//+" ("+obj.getJSONArray("SubCategory").length()+")"
				((ApplicationManager) getApplication()).sendGA(t, obj.getString("Title") +"Selected");
		        this.addSection(this.newSection(i, obj.getString("Title"), new HomeFragment()));
			}
		//	Appirater.appLaunched(this);
			/*
			UpdateChecker checker = new UpdateChecker(this);
			checker.setSuccessfulChecksRequired(5);
			checker.setNotice(Notice.NOTIFICATION);
			checker.start();*/
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	
	private String getFileFromAsset()	{
		BufferedReader reader = null;
		StringBuffer strBuf = new StringBuffer();
		try {
		    reader = new BufferedReader(
		        new InputStreamReader(getAssets().open(getString(R.string.animal_json_file)), "UTF-8")); 

		    // do reading, usually loop until end of file reading 
		    String mLine = reader.readLine();		    
		    while (mLine != null) {
		       //process line
		    	 if(mLine!=null) strBuf.append(mLine); 
		    	mLine = reader.readLine();
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (reader != null) {
		         try {
		             reader.close();
		         } catch (IOException e) {
		        	 e.printStackTrace();
		         }
		    }
		}
		return strBuf.toString();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.home_menu, menu);
		   return super.onCreateOptionsMenu(menu);	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int itemId = menuItem.getItemId();
		if (itemId == R.id.action_volume) {
			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);

			return true;
		}else if (itemId == R.id.action_animal_list) {
			showAnimalListPopup();
			return true;
		}else if (itemId == R.id.action_crosspromote) {
			((ApplicationManager) getApplication()).sendGA(t, "CrossPrmote");
			Intent intent = new Intent(this, HouseApps.class);
			startActivity(intent);
			return true;
		}else  if(menuItem.getItemId() == android.R.id.home){
			 finish();
		 }else  if(menuItem.getItemId() == R.id.removeAdsBt){
			Toast.makeText(MainActivity.this,"Ad remove clicked", Toast.LENGTH_SHORT).show();
			onBuyButtonClicked(AppConstants.getAdFreeSKU(MainActivity.this), "");
		}else  if(menuItem.getItemId() == R.id.privacyPolBt){
			String url = "http://techramesh.com/PrivacyPolicy.php?app=AB";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}
		return super.onOptionsItemSelected(menuItem);
	}

	public void showAnimalListPopup() {
		((ApplicationManager) getApplication()).sendGA(t, "Animal List");
	    View menuItemView = findViewById(R.id.action_animal_list);
	    PopupMenu popup = new PopupMenu(this, menuItemView);
	    try {
	    obj = (JSONObject) jsonArray.get(position);
	    for(int i = 0; i < obj.getJSONArray("SubCategory").length(); i++){
				popup.getMenu().add(Menu.NONE, i, Menu.NONE, obj.getJSONArray("SubCategory").getJSONObject(i).getString("Title"));
	    }
	    } catch (JSONException e) {
			e.printStackTrace();
		}
	    
	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				
				try {

						HomeFragment fragment = (HomeFragment) getCurrentSection().getTargetFragment();
						fragment.mDemoSlider.moveToSlider(arg0.getItemId());
						fragment.mDemoSlider.stopAutoCycle();

					if(fragment.player.isPlaying()){
						fragment.player.pause();
					}else {
						fragment.playAudio(obj.getJSONArray("SubCategory").getJSONObject(arg0.getItemId()).getString("Title").replace(" ", "").toLowerCase());
					}
					//isPlaying = !isPlaying;
          		} catch (JSONException e) {
          			e.printStackTrace();
          		}
				
				return false;
			}
		});
	    popup.show();
	}
}
