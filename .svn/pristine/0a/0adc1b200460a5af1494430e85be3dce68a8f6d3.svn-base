package com.androplus.materialnavigationdrawer;
import java.util.LinkedList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.androplus.AnimalBeats.AppConstants;
import com.androplus.AnimalBeats.ApplicationManager;
import com.androplus.AnimalBeats.IAPBaseActivity;
import com.androplus.AnimalBeats.R;
import com.androplus.components.CustomTypefaceSpan;
import com.androplus.utils.RewardVideoUtil;
import com.mopub.common.MoPub;

/**
 * Activity that implements ActionBarActivity with a Navigation Drawer with Material Design style
 *
 * @author created by neokree
 */
@SuppressLint("InflateParams")
public abstract class MaterialNavigationDrawer extends IAPBaseActivity implements MaterialSectionListener{

    public static final int BOTTOM_SECTION_START = 100;
    public static final int SECTION_START = 0;
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private RelativeLayout drawer;
    private ImageView userphoto;
    private ImageView userSecondPhoto;
    private ImageView userThirdPhoto;
    private ImageView usercover;
    private TextView username;
    private TextView usermail;
    private LinearLayout sections;
    private LinearLayout bottomSections;
    public int position  =0;
    private List<MaterialSection> sectionList;
    private List<MaterialSection> bottomSectionList;
    private MaterialSection currentSection;

    // private CharSequence title;
    private static int indexFragment = 0;
    private float density;
    private int primaryColor;
    private String selected_title = "";
    boolean isFirstLoad = false;
    @Override
    /**
     * Do not Override this method!!! <br>
     * Use init() instead
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_navigation_drawer);
        // init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // init drawer components
        drawer = (RelativeLayout) this.findViewById(R.id.drawer);
        username = (TextView) this.findViewById(R.id.user_nome);
        usermail = (TextView) this.findViewById(R.id.user_email);
        userphoto = (ImageView) this.findViewById(R.id.user_photo);
        userSecondPhoto = (ImageView) this.findViewById(R.id.user_photo_2);
        userThirdPhoto = (ImageView) this.findViewById(R.id.user_photo_3);
        usercover = (ImageView) this.findViewById(R.id.user_cover);
        sections = (LinearLayout) this.findViewById(R.id.sections);
        bottomSections = (LinearLayout) this.findViewById(R.id.bottom_sections);

        // init lists
        sectionList = new LinkedList<MaterialSection>();
        bottomSectionList = new LinkedList<MaterialSection>();

        //get density
        density = this.getResources().getDisplayMetrics().density;

        // get primary color
        Resources.Theme theme = this.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;
        init(savedInstanceState);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        // INIT ACTION BAR

        // Si preleva il titolo dell'activity
        // selected_title = sectionList.get(indexFragment).getTitle();
        // si collega il DrawerLayout al codice e gli si setta l'ombra all'apertura

        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.app_name_kids, R.string.nothing);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
        //drawerToggle.syncState();
        MaterialSection section = sectionList.get(0);
        currentSection = section;
        section.select();
        isFirstLoad = true;
        selected_title = section.getTitle();
        setFragment(section.getTargetFragment(),getString(R.string.app_name_kids));
    }
    public Toolbar getToolbar() {
        return toolbar;
    }
    // Gestione dei Menu -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }
    /* Chiamata dopo l'invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se dal drawer si seleziona un oggetto
        if(item.getItemId() == android.R.id.home){
            if(drawerLayout.isDrawerOpen(drawer)){
                drawerLayout.closeDrawer(drawer);
                setTitle(selected_title);
            }else{
                drawerLayout.openDrawer(drawer);
                setTitle(getString(R.string.app_name_kids));
            }
        }else if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {// al cambio di orientamento dello schermo
        super.onConfigurationChanged(newConfig);

        // Passa tutte le configurazioni al drawer
        drawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public void setTitle(CharSequence title) {
        // this.title = title;
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.kids_font));
        SpannableStringBuilder SS = new SpannableStringBuilder(title);
        SS.setSpan (new CustomTypefaceSpan("", font), 0, title.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        this.getSupportActionBar().setTitle(SS);
    }

    private void setFragment(Fragment fragment,String title) {
        // si fa
        // il trasferimento del fragment sullo schermo
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        // si setta il titolo e si chiude il drawer
        //lista.setItemChecked(i, true);
        if(isFirstLoad){
            isFirstLoad = false;
        }else{
            selected_title = title;
        }
        setTitle(title);
        if(drawerLayout.isDrawerOpen(drawer)){
            drawerLayout.closeDrawer(drawer);
        }else{
            drawerLayout.openDrawer(drawer);
        }
    }
    @Override
    public void onClick(final MaterialSection section) {
        try {
           /* if (!AppConstants.isFirstLaunch && RewardVideoUtil.isReadyToShowAd(MaterialNavigationDrawer.this) && MoPub.hasRewardedVideo(mopub_reward_id)) {
                new MaterialDialog.Builder(MaterialNavigationDrawer.this)
                        .title("")
                        .content(R.string.reward_main_txt)
                        .positiveText(R.string.reward_watch_txt)
                        .negativeText(R.string.reward_cancel_txt)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                userClickedToWatchAd(new OnRewardVideo() {
                                    @Override
                                    public void onRewardVideoCompleted() {
                                        loadRewardedVideo();
                                        RewardVideoUtil.setShowAd(MaterialNavigationDrawer.this);
                                        removeAdContainer();
                                        setSection(section);
                                        ((ApplicationManager) getApplication()).sendGA(t, "RewardVideo/Closed");
                                    }
                                });
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                materialDialog.dismiss();
                                setSection(section);
                                ((ApplicationManager) getApplication()).sendGA(t, "RewardDialogCancel");
                            }
                        })
                        .show();
            } else*/  setSection(section);
        }catch (Exception e){
            e.printStackTrace();
           // ((ApplicationManager) getApplication()).sendGA(t, "RewardDialog-AppCrash-Error");
            setSection(section);
        }
    }

    public void  setSection(MaterialSection section){
        currentSection = section;
        if(section.getTarget() == MaterialSection.TARGET_FRAGMENT)
        {
            setFragment(section.getTargetFragment(),section.getTitle());
            // setting toolbar color if is setted
            if(section.hasSectionColor())
                this.getToolbar().setBackgroundColor(section.getSectionColor());
            else
                this.getToolbar().setBackgroundColor(primaryColor);
        }
        else {
            this.startActivity(section.getTargetIntent());
        }

        position = section.getPosition();

        for(MaterialSection mySection : sectionList) {
            if(position != mySection.getPosition())
                mySection.unSelect();
        }
        for(MaterialSection mySection : bottomSectionList) {
            if(position != mySection.getPosition())
                mySection.unSelect();
        }

    }

    private Bitmap convertToBitmap(Drawable drawable) {

        Bitmap mutableBitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable.draw(canvas);
        return mutableBitmap;
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    // Method used for customize layout

    public void setUserEmail(String email) {
        this.usermail.setText(email);
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setUserPhoto(Bitmap photo) {
        userphoto.setImageBitmap(getCroppedBitmap(photo));
    }

    public void setUserPhoto(Drawable photo) {
        userphoto.setImageBitmap(getCroppedBitmap(convertToBitmap(photo)));
    }

    public void setSecondAccountPhoto(Drawable photo) {
        userSecondPhoto.setImageBitmap(getCroppedBitmap(convertToBitmap(photo)));
    }

    public void setSecondAccountPhoto(Bitmap photo) {
        userSecondPhoto.setImageBitmap(getCroppedBitmap(photo));
    }

    public void setThirdAccountPhoto(Drawable photo) {
        userThirdPhoto.setImageBitmap(getCroppedBitmap(convertToBitmap(photo)));
    }

    public void setThirdAccountPhoto(Bitmap photo) {
        userThirdPhoto.setImageBitmap(getCroppedBitmap(photo));
    }

    public void setDrawerBackground(Bitmap background) {
        usercover.setImageBitmap(background);

        //setPalette();
    }

    public void setDrawerBackground(Drawable background) {
        usercover.setImageDrawable(background);

        //setPalette();
    }

    public void addSection(MaterialSection section) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(48 * density));
        sectionList.add(section);
        sections.addView(section.getView(),params);
    }

    public void addBottomSection(MaterialSection section) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(48 * density));
        bottomSectionList.add(section);
        bottomSections.addView(section.getView(),params);
    }

    public void addDivisor() {
        View view = new View(this);
        view.setBackgroundColor(Color.parseColor("#e0e0e0"));
        // height 1 px
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
        params.setMargins(0,(int) (8 * density), 0 , (int) (8 * density));

        sections.addView(view, params);
    }

    // create sections

    public MaterialSection newSection(String title, Drawable icon, Fragment target) {
        MaterialSection section = new MaterialSection(this,sectionList.size(),true,MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Drawable icon, Intent target) {
        MaterialSection section = new MaterialSection(this,sectionList.size(),true,MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Bitmap icon,Fragment target) {
        MaterialSection section = new MaterialSection(this,sectionList.size(),true,MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Bitmap icon,Intent target) {
        MaterialSection section = new MaterialSection(this,sectionList.size(),true,MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newSection(int subCatId, String title,Fragment target) {
        MaterialSection section = new MaterialSection(this,sectionList.size(),false,MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);
        section.subCatId = subCatId;

        return section;
    }

    public MaterialSection newSection(String title,Intent target) {
        MaterialSection section = new MaterialSection(this,sectionList.size(),false,MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newBottomSection(String title, Drawable icon,Fragment target) {
        MaterialSection section = new MaterialSection(this,BOTTOM_SECTION_START + bottomSectionList.size(),true,MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newBottomSection(String title, Drawable icon,Intent target) {
        MaterialSection section = new MaterialSection(this,BOTTOM_SECTION_START + bottomSectionList.size(),true,MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newBottomSection(String title, Bitmap icon,Fragment target) {
        MaterialSection section = new MaterialSection(this,BOTTOM_SECTION_START + bottomSectionList.size(),true,MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newBottomSection(String title, Bitmap icon,Intent target) {
        MaterialSection section = new MaterialSection(this,BOTTOM_SECTION_START + bottomSectionList.size(),true,MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newBottomSection(String title, Fragment target) {
        MaterialSection section = new MaterialSection(this,BOTTOM_SECTION_START + bottomSectionList.size(),false,MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);
        return section;
    }

    public MaterialSection newBottomSection(String title, Intent target) {
        MaterialSection section = new MaterialSection(this,BOTTOM_SECTION_START + bottomSectionList.size(),false,MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    // abstract methods

    public abstract void init(Bundle savedInstanceState);
    // get methods

    public MaterialSection getCurrentSection() {
        return currentSection;
    }

    @Override
    public void onBackPressed() {

        try{

            if(drawerLayout.isDrawerOpen(drawer)){
                drawerLayout.closeDrawer(drawer);
            }else{
                new MaterialDialog.Builder(this)
                        .title(R.string.app_name)
                        .content(R.string.close_dialog_message)
                        .theme(Theme.DARK)
                        .positiveText(R.string.close_dialog_positive)
                        .negativeText(R.string.close_dialog_negative)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                MaterialNavigationDrawer.super.onBackPressed();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            }
                        })
                        .show();
            }
        }catch(Exception e){
        }finally{
        }
    }


}