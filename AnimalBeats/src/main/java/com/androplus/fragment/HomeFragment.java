package com.androplus.fragment;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.androplus.AnimalBeats.AnimalListAdapter;
import com.androplus.AnimalBeats.ChildAnimationExample;
import com.androplus.AnimalBeats.MainActivity;
import com.androplus.AnimalBeats.R;
import com.androplus.materialnavigationdrawer.MaterialSection;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by neokree on 24/11/14.
 */
public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, OnCompletionListener{
	public SliderLayout mDemoSlider;
	private JSONArray mJsonArray;
	public MediaPlayer player;
	MainActivity activity;
	ImageView playPauseBt = null;
	//boolean isPlaying=false;
	MaterialSection section = null;
	public HomeFragment(){
		super();
	}

	/*@SuppressLint("ValidFragment")
	public HomeFragment(JSONArray jsonArray) {
		mJsonArray = jsonArray;
	}*/
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View root = (ViewGroup) inflater.inflate(R.layout.activity_main, null);
		section = activity.getCurrentSection();
    	initUI(root);
        return root;
    }

	@Override
	public void onAttach(Context context) {
		activity = (MainActivity) context;
		super.onAttach(context);
	}
	private void initUI(View view){
		playPauseBt = (ImageView) view.findViewById(R.id.playPauseBt);

    	  mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);
    	  if (player == null) {
  			player = new MediaPlayer();
  		}

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					mJsonArray = ((JSONObject) activity.jsonArray.get(section.subCatId)).getJSONArray("SubCategory");

					for(int i = 0; i < mJsonArray.length(); i++){

						JSONObject obj = mJsonArray.getJSONObject(i);

						TextSliderView textSliderView = new TextSliderView(getActivity());

						textSliderView
								.description(obj.getString("Title"))
								.image("file:///android_asset/" + obj.getString("Icon"))
								.setScaleType(BaseSliderView.ScaleType.FitCenterCrop).descFont(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.kids_font)))
								.setOnSliderClickListener(HomeFragment.this);
						//add your extra information
						textSliderView.getBundle()
								.putInt("Index",i);

						mDemoSlider.addSlider(textSliderView);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});


			
          mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
          mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
          //mDemoSlider.setCustomIndicator((PagerIndicator) view.findViewById(R.id.custom_indicator));
          mDemoSlider.setCustomAnimation(new ChildAnimationExample());
          mDemoSlider.setDuration(4000);
          mDemoSlider.stopAutoCycle();
          mDemoSlider.setOnSliderChange(new SliderLayout.SliderChangeListener(){

			@Override
			public void onSlide(int position) {

				/*if(position>=0) {
					BaseSliderView baseSliderView = null;
					try {
						baseSliderView = mDemoSlider.mSliderAdapter.getSliderView(position);
						baseSliderView.image("file:///android_asset/" + mJsonArray.getJSONObject(position).getString("Icon"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					View temp = baseSliderView.getView();
					ImageView target = (ImageView) temp.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
					baseSliderView.bindEventAndShow(temp, target);


					playPauseBt.setVisibility(View.VISIBLE);
					//Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT).show();

				}*/
				StopAudio();
			}
          });

		activity.adContainer = (RelativeLayout) view.findViewById(R.id.adContainer);
          ListView l = (ListView)view.findViewById(R.id.transformers);
          l.setAdapter(new AnimalListAdapter(getActivity(), mJsonArray));
          l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	  try {
					  playPauseBt.setVisibility(View.GONE);
            		mDemoSlider.moveToSlider(position);
            		 mDemoSlider.stopAutoCycle();
					  playAudio(mJsonArray.getJSONObject(position).getString("Title").replace(" ", "").toLowerCase());
				  } catch (JSONException e) {
          			e.printStackTrace();
          		}
              }
          });
          activity.showAd(true, activity.adContainer);
    }
	public void pauseAudio(){
		if(player!=null&&player.isPlaying()) {
			player.stop();
		}

	}
    
    public void playAudio(String filename) {

		try {

			if (player == null) {
				player = new MediaPlayer();
			}

			AssetFileDescriptor descriptor;
			try {

				descriptor = getActivity().getAssets().openFd(filename + ".mp3");
				player.reset();
				player.setDataSource( descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
				//player.setDataSource(this, Uri.parse("android.resource://com.androplus.AnimalBeats/raw/"+ mAnimalArray[i]));
				player.setLooping(false); // Set looping
				player.setVolume(100, 100);
				//descriptor.close();
				player.prepare();

				player.setOnCompletionListener(this);

				player.start();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				activity.showAd(false, null);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    @Override
    public void onDestroy() {
    	
    	if (player != null && player.isPlaying() == true) {
			player.reset();
			player = null;
		}
    	
    	super.onDestroy();
    	
    }
	public void StopAudio() {
		if (player != null && player.isPlaying() == true) {
			player.reset();
		}
	}
	
	public void onPause() {
		super.onPause();
		//StopAudio();
	}
	
	
    @Override
    public void onResume() {
    	super.onResume();
    }

	@Override
	public void onSliderClick(BaseSliderView slider) {
		try {

			playPauseBt.setVisibility(View.GONE);
			playAudio(mJsonArray.getJSONObject(slider.getBundle().getInt("Index")).getString("Title").replace(" ", "").toLowerCase());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//Toast.makeText(getActivity(), slider.getBundle().getInt("Index") + "",Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		playPauseBt.setVisibility(View.VISIBLE);
		mDemoSlider.nextSlider();
	}
    
}
