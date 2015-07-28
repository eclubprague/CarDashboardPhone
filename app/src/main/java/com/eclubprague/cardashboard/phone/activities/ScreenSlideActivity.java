package com.eclubprague.cardashboard.phone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.predefined.EmptyModule;
import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.fragments.ScreenSlidePageFragment;
import com.eclubprague.cardashboard.phone.utils.VerticalViewPager;

import java.util.List;

public class ScreenSlideActivity extends FragmentActivity implements IModuleContext {
    public static final String KEY_PARENT_MODULE = ScreenSlideActivity.class.getName() + ".KEY_PARENT_MODULE";
    private VerticalViewPager mPager;

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
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    protected void initPager(){
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), modules);
        mPager.setAdapter(mPagerAdapter);
    }

    protected void setModule(IParentModule parentModule){
        getActionBar().setTitle(parentModule.getTitle().getString(this));
        getActionBar().setIcon(parentModule.getIcon().getIcon(this));
        this.parentModule = parentModule;
        this.modules = this.parentModule.getSubmodules(this);
        addEmptyModule();
        initPager();
    }

    private void addEmptyModule() {
        modules.add(new EmptyModule(this, parentModule, null, null));
    }


    @Override
    public void goToSubmodules(IParentModule parentModule) {
//TODO
    }

    @Override
    public void goBackFromSubmodules(IParentModule parentModule) {
//TODO
    }

    @Override
    public void toggleQuickMenu(IModule module, boolean activate) {
//TODO
    }

    @Override
    public void launchIntent(Intent intent) {
//TODO
    }

    @Override
    public void swapModules(IModule oldModule, IModule newModule, boolean animate) {
//TODO
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<IModule> modules;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<IModule> modules) {
            super(fm);
            this.modules = modules;
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(modules.get(position));
        }

        @Override
        public int getCount() {
            return modules.size();
        }
    }
}
