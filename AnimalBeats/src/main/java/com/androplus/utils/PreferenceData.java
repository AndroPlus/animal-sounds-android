package com.androplus.utils;

import android.app.Activity;
import android.content.SharedPreferences;


public class PreferenceData {
    Activity ctx;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public PreferenceData(Activity ctx){
        this.ctx =ctx;
        preferences = ctx.getSharedPreferences("pref", 0);
    }
    //save the data

    public void put(int ver){
        editor = preferences.edit();
        editor.putInt("ver", ver);
        editor.commit();
    }

    public int get(){
        return  preferences.getInt("ver",0);
    }

    public void setisTNHD_Paper_Updated(boolean ver){
        editor = preferences.edit();
        editor.putBoolean("is_tnhd_paper", ver);
        editor.commit();
    }

    public boolean isTNHD_paperUpdated(){
        return  preferences.getBoolean("is_tnhd_paper",false);
    }

    public void setTNHD_Versioncode(int vercode){
        editor = preferences.edit();
        editor.putInt("tnhd_ver_code", vercode);
        editor.commit();
    }

    public int getTNHD_Versioncode(){
        return  preferences.getInt("tnhd_ver_code",0);
    }

    public void setNightMode(int mode){
        editor = preferences.edit();
        editor.putInt("mode", mode);
        editor.commit();
    }

    public int getNightMode(){
        return  preferences.getInt("mode",0);
    }
    public void setOverlayHelp(boolean mode){
        editor = preferences.edit();
        editor.putBoolean("help", mode);
        editor.commit();
    }

    public boolean getOverlayHelp(){
        return  preferences.getBoolean("help",false);
    }

    public void setCPVersion(int ver){
        editor = preferences.edit();
        editor.putInt("cp_ver", ver);
        editor.commit();
    }

    public int getCPVersion(){
        return  preferences.getInt("cp_ver",0);
    }

    public void setIntersDuration(int duration){
        editor = preferences.edit();
        editor.putInt("duration", duration);
        editor.commit();
    }

    public int getIntersDuration(){
        return  preferences.getInt("duration",60000);
    }

    private void setPD(String key, int type, String data){
        editor = preferences.edit();
        switch(type){

            case 1:
                editor.putInt(key, Integer.parseInt(data));
                break;

            case 2:
                editor.putString(key, data);
                break;

        }

        editor.commit();
    }

    private Object getPD(String key, int type){
        switch(type){

            case 1:

                return preferences.getInt(key, 0);


            case 2:
                return preferences.getString(key, "");

        }
        return null;
    }


    /**FRESH PF DATA***/

    public int getLastSelectedCategoryId() {
        return (Integer) getPD("selected_category_id", 1);
    }

    public void setSelectedCategoryId(int selected_category_id) {
        setPD("selected_category_id", 1, selected_category_id+"");
    }

    public int getLastItemSelectedId() {
        return (Integer) getPD("item_id", 1);
    }

    public void setSelectedId(int item_id) {
        setPD("item_id", 1, item_id+"");
    }

    public int getIsSplitted() {
        return (Integer) getPD("is_split", 1);
    }

    public void setSplitted(int is_split) {
        setPD("is_split", 1, is_split+"");
    }

    public int getResid() {
        return (Integer) getPD("resid", 1);
    }

    public void setResid(int resid) {
        setPD("resid", 1, resid+"");
    }


//	String serverXML;

    public String getServerXML() {
        return (String) getPD("serverXML", 2);
    }

    public void setServerXML(String serverXML) {
        setPD("serverXML", 2, serverXML+"");
    }

    //String image_name;

    public String getImage_name() {
        return (String) getPD("image_name", 2);
    }

    public void setImage_name(String image_name) {
        setPD("image_name", 2, image_name+"");
    }

    //String image_name;

    public String getTitle() {
        return (String) getPD("news_title", 2);
    }

    public void setTitle(String news_title) {
        setPD("news_title", 2, news_title+"");
    }

    // Typeface newsPaperFont;

    public  String getNewsPaperFont() {
        return (String) getPD("my_font", 2);
    }

    public  void setNewsPaperFont(String newsPaperFont) {
        setPD("my_font", 2, newsPaperFont+"");
    }

    //  String lang;

    public  String getLang() {
        return (String) getPD("my_lang", 2);
    }

    public  void setLang(String my_lang) {
        setPD("my_lang", 2, my_lang+"");
    }


    // String appName;

    //String appPackageName;

    public  String getAppName() {
        return (String) getPD("appName", 2);
    }

    public  void setAppName(String my_appName) {
        setPD("appName", 2, my_appName+"");
    }

    public String getAppPackageName() {
        return (String) getPD("appPackageName", 2);
    }

    public void setAppPackageName(String appPackageName) {
        setPD("appPackageName", 2, appPackageName+"");
    }

    public String getCategories() {
        return (String)getPD("categories", 2);
    }

    public void setCategories(String categories) {
        setPD("categories", 2, categories+"");
    }

    public String getNPCode() {
        return (String)getPD("np_code", 2);

    }

    public void setNPCode(String np_code) {
        setPD("np_code", 2, np_code+"");
    }

    // Rewarded Videos Preference

    public int getRewardDuration(){
        return  preferences.getInt("duration",900000);	//60000x5 = 5 mins
    }
    public long getRewardShowTime()	{
        return  preferences.getLong("reward_showtime" ,0);
    }
    public void setRewardShowtime(long reward_time)	{
        editor = preferences.edit();
        editor.putLong("reward_showtime", reward_time);
        editor.commit();
    }


}


