package com.eclubprague.cardashboard.phone.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;
import com.eclubprague.cardashboard.core.fragments.ModuleListDialogFragment;
import com.eclubprague.cardashboard.core.model.resources.StringResource;
import com.eclubprague.cardashboard.core.modules.base.IActivityStateChangeListener;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.core.obd.OBDGatewayService;
import com.eclubprague.cardashboard.core.views.ModuleView;
import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.fragments.ScreenSlidePageFragment;
import com.eclubprague.cardashboard.phone.utils.VerticalViewPager;

import java.util.List;

public class ScreenSlideActivity extends FragmentActivity implements IModuleContext {
    private static final String TAG = ScreenSlideActivity.class.getSimpleName();


    private VerticalViewPager mPager;
    private final int LOOPS_COUNT = 1000;
    private PagerAdapter mPagerAdapter;
    private IParentModule parentModule;
    private List<IModule> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (VerticalViewPager) findViewById(R.id.pager);
        GlobalDataProvider.getInstance().setModuleContext(this);
        if (OBDGatewayService.getInstance() == null) {
            Intent t = new Intent(this, OBDGatewayService.class);
            startService(t);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void initPager() {
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), modules);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(modules.size() * LOOPS_COUNT / 2, false);
    }

    protected void setModule(IParentModule parentModule) {
        getActionBar().setTitle(parentModule.getTitle().getString(this));
        getActionBar().setIcon(parentModule.getIcon().getIcon(this));
        this.parentModule = parentModule;
        this.modules = this.parentModule.getSubmodules();
        initPager();
    }

    @Override
    public void goToSubmodules(IParentModule parentModule) {
        Intent intent = new Intent(this, ModuleActivity.class);
        intent.putExtra(ModuleActivity.KEY_PARENT_MODULE, parentModule.getId());
        startActivity(intent);
    }

    @Override
    public void goBackFromSubmodules(IParentModule parentModule) {
        finish();
    }

    @Override
    public void toggleQuickMenu(IModule module, ModuleView moduleView, boolean activate) {
        ViewSwitcher holder = (ViewSwitcher) moduleView.getViewHolder();
        if (activate) {
            if (holder.getDisplayedChild() != 1) holder.setDisplayedChild(1);
        } else {
            if (holder.getDisplayedChild() != 0) holder.setDisplayedChild(0);
        }
    }

    @Override
    public void turnQuickMenusOff() {
        for (IModule m : modules) {

        }
    }

    @Override
    public void launchIntent(Intent intent, StringResource errorMessage) {
        startActivity(intent);
        // TODO:
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onModuleEvent(IModule module, ModuleView moduleView, ModuleEvent event) {
        Log.d("onModuleEvent", event.name());
        switch (event) {
            case CANCEL:
                toggleQuickMenu(module, moduleView, false);
                break;
            case DELETE:
                break;
            case MOVE:
                break;
            case MORE:
                break;
            case ADD:
                ModuleListDialogFragment dialog = ModuleListDialogFragment.newInstance(this, new ModuleListDialogFragment.OnMultiAddModuleListener() {
                    @Override
                    public void addModules(List<IModule> modules) {
                        Log.d("adding modules", modules.toString());
                    }
                });
                dialog.show(getFragmentManager(), "Applist");

                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (IActivityStateChangeListener iActivityStateChangeListener : modules) {
            iActivityStateChangeListener.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalDataProvider.getInstance().setModuleContext(this);
        for (IActivityStateChangeListener iActivityStateChangeListener : modules) {
            iActivityStateChangeListener.onResume(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalDataProvider.getInstance().setModuleContext(this);
        for (IActivityStateChangeListener iActivityStateChangeListener : modules) {
            iActivityStateChangeListener.onStart(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (IActivityStateChangeListener iActivityStateChangeListener : modules) {
            iActivityStateChangeListener.onStop(this);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<IModule> modules;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<IModule> modules) {
            super(fm);
            this.modules = modules;
        }


        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(modules.get(position % modules.size()));
        }

        @Override
        public int getCount() {
            return modules.size() * LOOPS_COUNT;
            //return modules.size();
        }
    }
}
