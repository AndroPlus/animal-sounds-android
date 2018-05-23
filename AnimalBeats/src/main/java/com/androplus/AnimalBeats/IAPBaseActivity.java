package com.androplus.AnimalBeats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amazon.device.iap.model.UserDataResponse;
import com.androplus.ads.AdManager;
import com.androplus.ads.AdsKey;
import com.androplus.ads.InterstitialAdListener;
import com.androplus.ads.enums.AdProvider;
import com.androplus.ads.mopub.DeviceType;
import com.androplus.iap.AppStoreMode;
import com.androplus.iap.AppStoreType;
import com.androplus.iap.CommonIAPListener;
import com.androplus.iap.IAPManager;
import com.androplus.iap.Operation;
import com.androplus.iap.Response;
import com.androplus.iap.play.util.IabResult;
import com.androplus.iap.vo.AvailableItem;
import com.androplus.iap.vo.PurchaseItem;
import com.androplus.iap.vo.PurchasedItem;
import com.androplus.utils.PreferenceData;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.inmobi.sdk.InMobiSdk;
import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class IAPBaseActivity extends AppCompatActivity {
    public Tracker t;
    protected ActionBar actionBar;
    public MoPubView moPubView;
    //protected StartAppAd startAppAd = null;
    //private int mTitleRes;
    protected ListFragment mFrag;

    boolean fromHomeActivity = false;
    // public Fragment mContent;
		/*public BaseActivity(int titleRes) {
			mTitleRes = titleRes;
		}*/

    String TAG = IAPBaseActivity.class.getSimpleName();
    public PreferenceData pd;
    private IAPManager iapManager;
    //protected InterstitialAd intAds;
    protected boolean showAdmobFullScreenAd = false;
    MoPubInterstitial ints = null;

    SharedPreferences sharedPre = null;
    SharedPreferences.Editor edit = null;
    public RelativeLayout adContainer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();

        AppLovinSdk.initializeSdk(this);
        InMobiSdk.init(this, getString(R.string.inmobi_account_id));
        //startAppAd = StartAppAds.initInterstitialAd(this);

        ApplicationManager.setCurrentContext(this);
        pd = new PreferenceData(this);

        sharedPre = getSharedPreferences(AppConstants.SETTINGS_PREF, 0);
        edit = sharedPre.edit();

        AppConstants.isPurchased = sharedPre.getBoolean(AppConstants.APP_PURCHASED, false);

        iapManager = IAPManager.getInstance();
        if (!AppConstants.isPaidVersion) {

            AppStoreType appStoreType = AppStoreType.PLAY_STORE;
            switch (AppConstants.platform) {
                case AMAZON:
                    appStoreType = AppStoreType.AMAZON_STORE;
                    break;

                case GOOGLE_PLAY:
                    appStoreType = AppStoreType.PLAY_STORE;
                    break;
            }

            AppStoreMode storeMode = AppConstants.DEV_MODE ? AppStoreMode.TEST_SUCCESS : AppStoreMode.LIVE;
            iapManager.initStore(this, appStoreType, AppConstants.getBase64EncodeKey(ApplicationManager.getCurrentContext()), storeMode, new IAPHandler());

            iapManager.setDebugMode(AppConstants.DEV_MODE);
        }
        setup();
    }

    private void setup(){
        t = ((ApplicationManager) getApplication()).getTracker(ApplicationManager.TrackerName.APP_TRACKER);
        AdsKey adskey = new AdsKey();
        adskey.setMoPubMobileBannerKey(getString(R.string.mopub_mobile_banner));
        adskey.setMoPubMobileInterstialKey(getString(R.string.mopub_mobile_fullscreen));

        adskey.setMoPubTabletBannerKey(getString(R.string.mopub_tablet_banner));
        adskey.setMoPubTabletInterstialKey(getString(R.string.mopub_tablet_fullscreen));

        adskey.setGoogleAdKey(getString(R.string.admob_banner_id));
        adskey.setGoogleInterstitialAdKey(getString(R.string.admob_fullscreen_id));

        adskey.setInMobiAdKey(getString(R.string.inmobi_account_id));
        adskey.setInMobiBannerAdKey(getString(R.string.inmobi_banner_id));
        adskey.setInMobiInterstitialAdKey(getString(R.string.inmobi_fullscreen_id));
        adskey.setInMobiNativeAdKey(getString(R.string.inmobi_native_id));

        //adskey.setMMBannerAdKey(getString(R.string.mm_banner));
        //adskey.setMMInterstitialAdKey(getString(R.string.mm_fullscreen));

        AdManager.setAdProvider(AdProvider.MO_PUB);
        AdManager.init("GooglePlay", adskey, false);

        if(!AppConstants.isPurchased)	loadFullScreenAds();

    }

    private boolean showAlert;
    private class IAPHandler implements CommonIAPListener {

        //-----------------------------------------------------------------------------------
        // GET USER DETAILS SUCCESS (AMAZON)
        //-----------------------------------------------------------------------------------



        @Override
        public void onUserDetailsFound(UserDataResponse user) {
            Log.d(TAG, "test");
        }

        //-----------------------------------------------------------------------------------
        // IAP setup finished (GOOGLE PLAY and BLACK BERRY)
        //-----------------------------------------------------------------------------------
        @Override
        public void OnIabSetupFinished(IabResult result) {
            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                alert("Problem setting up in-app billing: " + result, "Error");
                return;
            }

            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            showAlert = false;
            iapManager.getPurchasedItems();
        }

        //-----------------------------------------------------------------------------------
        // GET AVAILABLE ITEM LIST SUCCESS
        //-----------------------------------------------------------------------------------

        @Override
        public void onIAPListPresent(Response response, AvailableItem item) {
            try {
                Log.d(TAG, "Success");
                if (AppConstants.platform == Platform.AMAZON) {
                    Log.d(TAG, item.response.getRequestStatus().toString());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        //-----------------------------------------------------------------------------------
        // PURCHASE SUCCESS
        //-----------------------------------------------------------------------------------

        @Override
        public void onPurchaseSuccess(Response response, PurchaseItem item) {
            try {
                String requestedSKU = iapManager.getRequestedSKU();
                if (response == Response.SUCCESSFUL) {
                    String purchasedItemSKU = item.getPurchasedItemSKU();
                    if (purchasedItemSKU.equals(AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext()))) {
                        Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");

                        //UILApplication.getGaTracker().set(Fields.SCREEN_NAME, "IAP Success - Remove Ads");
                        AppConstants.isPurchased = true;
                        makeAdFreeVersion(true);

                        Log.d(TAG, "Success");
                        ((ApplicationManager) getApplication()).sendGA(t, "IAPAdFree/Success");
                        alert(getResources().getString(R.string.iap_success_message), getResources().getString(R.string.iap_success_title));
                    }  else {
                        if (requestedSKU.equals(AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext()))) {
                            //((FBApplication) getApplication()).sendGA(t, "IAPAdFree/Failure");
                        }
                        alert(getResources().getString(R.string.iap_failure_message), getResources().getString(R.string.iap_failure_title));
                    }
                } else if (response == Response.ALREADY_ENTITLED) {
                    if (requestedSKU.equals(AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext()))) {
                        AppConstants.isPurchased = true;
                        makeAdFreeVersion(true);

                        Log.d(TAG, "already purchased");
                        alert(getResources().getString(R.string.iap_restore_success_message), getResources().getString(R.string.iap_restore_success_title));
                    }				}
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        //-----------------------------------------------------------------------------------
        // PURCHASE CANCELLED
        //-----------------------------------------------------------------------------------

        @Override
        public void onPurchaseCanceled(String message) {
            String requestedSKU = iapManager.getRequestedSKU();
            try {
                if (requestedSKU.equals(AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext()))) {
                    //((ApplicationManager) getApplication()).sendGA(t, "IAPAdFree/Cancel");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        //-----------------------------------------------------------------------------------
        // GET PURCHASED ITEM LIST SUCCESS
        //-----------------------------------------------------------------------------------

        @Override
        public void onParchsedListPresent(Response response, PurchasedItem item) {
            try {
                Log.d(TAG, "Query inventory was successful.");

                AppConstants.isPurchased = item.hasPurchased(AppConstants.platform == Platform.SAMSUNG ?
                        AppConstants.AD_FREE_ITEM : AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext()));

                if (AppConstants.isPurchased) {
                    makeAdFreeVersion(false);
                    //UILApplication.getGaTracker().set(Fields.SCREEN_NAME, "IAP Already Purchased");
					/*alert(getResources().getString(R.string.iap_restore_success_message),
							getResources().getString(R.string.iap_restore_success_title));*/
                }

                if (!AppConstants.isPurchased && !AppConstants.isProVersion && showAlert) {
                    alert(getResources().getString(R.string.iap_restore_no_purchase_message),
                            getResources().getString(R.string.iap_restore_no_purchase_title));
                }
                Log.d(TAG, "Initial inventory query finished; enabling main UI.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        //-----------------------------------------------------------------------------------
        // COMMON ERROR HANDLER
        //-----------------------------------------------------------------------------------

        @Override
        public void onError(Operation operation, Response response, String message) {
            try {
                switch (operation) {
                    case GET_AVAILABLE_ITEM:
                        // on getAvalableItem failed
                        if (response == Response.SUCCESSFUL_WITH_UNAVAILABLE_SKUS) {
                            Log.d(TAG, "Success with unaviable Sku");
                        } else {
                            Log.d(TAG, "failed");
                        }
                        break;

                    case PURCHASE:
                        // on purchase failed
                        if (response == Response.INVALID_SKU) {
                            Log.d(TAG, "invalid sku");
                            if (AppConstants.platform != Platform.AMAZON) {
                                alert("Invalid Item", "Error!");
                            }
                        } else {
                            Log.d(TAG, "failed");
                            if (AppConstants.platform != Platform.AMAZON) {
                                //alert(getResources().getString(R.string.iap_failure_message), getResources().getString(R.string.iap_failure_title));
							/*alert(getResources().getString(R.string.iap_restore_success_message), getResources().getString(R.string.iap_restore_success_title));*/
                            }
                        }

                        String requestedSKU = iapManager.getRequestedSKU();
                        if (requestedSKU.equals(AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext())) || requestedSKU.equals(AppConstants.AD_FREE_ITEM)) {
                            //((FBApplication) getApplication()).sendGA(t, "IAPAdFree/Failure");
                        }
                        break;

                    case GET_PURCHASED_ITEM:
                        // on getPurchasedItem failed
                        Log.d(TAG, "failed ");

                        if (showAlert && AppConstants.platform != Platform.AMAZON) {
                            //alert(getResources().getString(R.string.iap_restore_failure_message), getResources().getString(R.string.iap_restore_failure_title));
                        }
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // User clicked the "Upgrade to Premium" button.
    public void onBuyButtonClicked(String sku, String item) {
        String payload = "";
        iapManager.purchase(sku, payload, "", "");
    }

    // User clicked the restore button.
    private void onRestorePurchaseButtonClicked() {
        //iapManager.getPurchasedItems(AppConstants.GROUP_ID);
    }




    /**
     * Show in app purchase
     */
    public void showIapDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationManager.getCurrentContext())
                .setTitle(R.string.iap_title)
                .setMessage(R.string.iap_msg)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((ApplicationManager) getApplication()).sendGA(t, "IAPAdFree/Cancel");
                    }
                })

                .setPositiveButton(R.string.iap_restore, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onRestorePurchaseButtonClicked();
                    }
                })

                .setNegativeButton(R.string.iap_upgrade, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBuyButtonClicked(AppConstants.getAdFreeSKU(ApplicationManager.getCurrentContext()), AppConstants.AD_FREE_ITEM);
                    }
                });

        builder.create().show();
        ((ApplicationManager) getApplication()).sendGA(t, "IAPAdFree/Display");
    }


    void alert(final String message, final String title) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder bld = new AlertDialog.Builder(ApplicationManager.getCurrentContext());
                    if ( title.length() > 0 ){
                        bld.setTitle(title);
                    }

                    bld.setMessage(message);
                    bld.setNeutralButton("OK", null);
                    Log.d(TAG, "Showing alert dialog: " + message);
                    bld.create().show();
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void makeAdFreeVersion(Boolean from_purchaseSuccess) {
        if ( AppConstants.isPurchased ){
            edit.putBoolean(AppConstants.APP_PURCHASED, true);
            edit.commit();

            Context ctx = ApplicationManager.getCurrentContext();

             if ( ctx instanceof MainActivity){
                ((MainActivity)ctx).removeAdContainer();
            }
            try {
                supportInvalidateOptionsMenu();
            }catch (Exception e){
                e.printStackTrace();
            }
			/*if ( AppConstants.platform == Platform.AMAZON ){
				AmazonAds.destroyAds();
			}*/
        }
    }

    // track app event for GOOGLE ANALYTICS
    public void trackEvent(String event) {
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        iapManager.handleActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        ApplicationManager.setCurrentContext(this);
        if (AppConstants.platform == Platform.AMAZON ){
            //AmazonAds.isActivityPaused =  false;
        }
        super.onResume();
        //startAppAd.onResume();
        MoPub.onResume(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //if(startAppAd!=null) startAppAd.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //if(startAppAd!=null)  startAppAd.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //startAppAd.onPause();
        if ( AppConstants.platform == Platform.AMAZON ){
            //AmazonAds.isActivityPaused =  true;
            MoPub.onPause(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iapManager.isInitialized()) {
            iapManager.dispose();
        }
        try {
            if ( AppConstants.isPurchased ){
                return;
        }

            if ( AppConstants.platform == Platform.GOOGLE_PLAY){
                // destroy app lovin ads
                //	if(isHome) {
                if (moPubView != null) {
                    moPubView.setBannerAdListener(null);
                    moPubView.destroy();
                    moPubView = null;
                    Log.d(TAG, "Banner Adview destroyed!");
                }
                AdManager.onPause();
            }
            else if ( AppConstants.platform == Platform.AMAZON ){
                //	AmazonAds.destroyAds();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    public void removeAdContainer() {
        Log.e("REMOVE ADS", "REMOVE");
        if(adContainer!=null) {
            adContainer.setVisibility(View.GONE);
            adContainer.removeAllViews();
        }
    }


    private void loadFullScreenAds() {
        AdManager.loadMoPubInterstitialAd(this, new InterstitialAdListener() {

            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                ints = interstitial;
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                super.onInterstitialFailed(interstitial, errorCode);
            }
        });
    }

    public void showAd(boolean isBannerAd, ViewGroup adContainer){
        try {

            if ( AppConstants.isPurchased){
                removeAdContainer();
                return;
            }

            //App lovin Ad
            if ( AppConstants.platform == Platform.GOOGLE_PLAY) {
                if (isBannerAd){
                    //For banner ad
                    moPubView = new MoPubView(this);
                    AdManager.showAd(this, adContainer,moPubView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                }else{
                    //For AppLovin InterstitialAd
                    if ( !AppConstants.isPurchased){
                        Date now = new Date();

                        if (AppConstants.lastShowedTime == 0){
                            AppConstants.lastShowedTime = new Date().getTime();
                        }else if ( now.getTime() > (AppConstants.lastShowedTime + pd.getIntersDuration())){
                            AppConstants.lastShowedTime = new Date().getTime();
                            Calendar cal = Calendar.getInstance(Locale.getDefault());
                            cal.setTimeInMillis(AppConstants.lastShowedTime);

                            if(ints!=null) {
                                AdManager.showMoPubInterstitialAd(this);
                                ints = null;
                                loadFullScreenAds();
                            }
                        }else{
                            Log.e("fullscreenads","time not reached!!!");
                        }

                        now = null;
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
