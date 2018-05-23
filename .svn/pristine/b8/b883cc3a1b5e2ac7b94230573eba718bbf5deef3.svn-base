package com.androplus.utils;

import android.util.Log;

import com.androplus.AnimalBeats.IAPBaseActivity;

import java.util.Date;

public class RewardVideoUtil {
    static Date now = null;
    public static boolean isReadyToShowAd(IAPBaseActivity ctx){
        now = new Date();

        Log.e("isReadyToShowAd",now.getTime()+"--"+ (ctx.pd.getRewardShowTime() + ctx.pd.getRewardDuration())+"");

        if (now.getTime() > (ctx.pd.getRewardShowTime() + ctx.pd.getRewardDuration())) {
            return  true;
        }else return false;
    }

    public static void setShowAd(IAPBaseActivity ctx){
        now = new Date();
        if (ctx.pd.getRewardShowTime() == 0 || now.getTime() > (ctx.pd.getRewardShowTime() + ctx.pd.getRewardDuration())) {
            ctx.pd.setRewardShowtime(new Date().getTime());
            Log.e("setShowAd", ctx.pd.getRewardShowTime()+"");
        }
    }

}
