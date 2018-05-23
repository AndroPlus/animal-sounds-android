package com.androplus.AnimalBeats;

import android.content.Context;

public class AppConstants {

	public static final Platform platform = Platform.GOOGLE_PLAY;

	public static final Boolean DEV_MODE = false;

	public static Boolean isFirstLaunch = false;

	public static Boolean isPaidVersion = false;

	public static Boolean isPurchased = false;

	public static Boolean isProVersion = false;

	public static long lastShowedTime = 0;

	// Android app key
	// public static final String BASE64_ENCODED_PUBLICKEY = null;

	public static String getBase64EncodeKey(Context ctx) {
		return ctx.getString(R.string.base64_encoded_publickey);
	}

	// private static final String SKU = null;

	public static String getAdFreeSKU(Context ctx) {
		return ctx.getString(R.string.adfree_sku);
	}

	// Amazon Ad Key
	private static final String AMAZON_AD_TEST_KEY = "923dcbf1fe2d4433ba205f971857691d";
	private static final String AMAZON_AD_LIVE_KEY = "923dcbf1fe2d4433ba205f971857691d";

	public static final String AD_FREE_ITEM = "";

	public static String getAmazonAdKey() {
		return DEV_MODE ? AMAZON_AD_TEST_KEY : AMAZON_AD_LIVE_KEY;
	}

	// Shared Preference
	public static final String SETTINGS = "settings";
	public static final String APP_PURCHASED = "app_purchased";

	// To show InterstitialAd
	public static final String IS_FIRST_LAUNCH = "is_first_lauch";
	public static final String CHECK_FIRST_SHOW_COUNT = "check_first_show_count";

	public static final String SETTINGS_PREF = "myPref";

}
