package com.eclubprague.cardashboard.phone.activities;

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

import com.eclubprague.cardashboard.core.modules.base.IActivityStateChangeListener;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.core.modules.base.models.resources.StringResource;
import com.eclubprague.cardashboard.core.views.ModuleListDialogFragment;
import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.fragments.ScreenSlidePageFragment;
import com.eclubprague.cardashboard.phone.utils.VerticalViewPager;

import java.util.LinkedList;
import java.util.List;

public class ScreenSlideActivity extends FragmentActivity implements IModuleContext {
    private static final String TAG = ScreenSlideActivity.class.getSimpleName();


    private VerticalViewPager mPager;
    private final int SLIDES_COUNT = 10000;
    private PagerAdapter mPagerAdapter;
    private IParentModule parentModule;
    private List<IModule> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (VerticalViewPager) findViewById(R.id.pager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void initPager() {
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), modules);
        mPager.setAdapter(mPagerAdapter);
        //mPager.setCurrentItem(5000);
    }

    protected void setModule(IParentModule parentModule) {
        getActionBar().setTitle(parentModule.getTitle().getString(this));
        getActionBar().setIcon(parentModule.getIcon().getIcon(this));
        this.parentModule = parentModule;
        this.modules = this.parentModule.getSubmodules(this);
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
    public void toggleQuickMenu(IModule module, boolean activate) {
        ViewSwitcher holder = (ViewSwitcher) module.getHolder();
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
    public void swapModules(IModule oldModule, IModule newModule, boolean animate) {
        //TODO
    }

    @Override
    public Context getContext() {
        return this;
    }

    List<IActivityStateChangeListener> moduleListeners = new LinkedList<IActivityStateChangeListener>();

    @Override
    public void addListener(IActivityStateChangeListener listener) {
        moduleListeners.add(listener);
    }

    @Override
    public void removeListener(IActivityStateChangeListener listener) {
        moduleListeners.remove(listener);
    }

    @Override
    public void onModuleEvent(IModule module, ModuleEvent event) {
        Log.d("onModuleEvent", event.name());
        switch (event) {
            case CANCEL:
                toggleQuickMenu(module, false);
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
    protected void onPause() {
        super.onPause();
        for (IActivityStateChangeListener iActivityStateChangeListener : moduleListeners) {
            iActivityStateChangeListener.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (IActivityStateChangeListener iActivityStateChangeListener : moduleListeners) {
            iActivityStateChangeListener.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (IActivityStateChangeListener iActivityStateChangeListener : moduleListeners) {
            iActivityStateChangeListener.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (IActivityStateChangeListener iActivityStateChangeListener : moduleListeners) {
            iActivityStateChangeListener.onStop();
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
            Log.d("position", ""+position);
            Log.d("position toget", ""+((SLIDES_COUNT/2) % modules.size()));
            Log.d("modulessize",""+modules.size());
            //return ScreenSlidePageFragment.newInstance(modules.get((SLIDES_COUNT/2) % modules.size()));
            return ScreenSlidePageFragment.newInstance(modules.get(position));
        }

        @Override
        public int getCount() {
            //return SLIDES_COUNT;
            return modules.size();
        }
    }
}
