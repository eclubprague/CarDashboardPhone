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
import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.fragments.ScreenSlidePageFragment;
import com.eclubprague.cardashboard.phone.utils.VerticalViewPager;

import java.util.List;

public class ScreenSlideActivity extends FragmentActivity implements IModuleContext {
    public static final String KEY_PARENT_MODULE = ScreenSlideActivity.class.getName() + ".KEY_PARENT_MODULE";
    private VerticalViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private List<IModule> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (VerticalViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), ModuleSupplier.getInstance().getHomeScreenModule(this).getSubmodules(this));
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    @Override
    public void goToSubmenu(IParentModule parentModule) {
        Intent intent = new Intent(this, ScreenSlideActivity.class);
        intent.putExtra(ScreenSlideActivity.KEY_PARENT_MODULE, parentModule.getId());
        startActivity(intent);
    }

    @Override
    public void goBack(IParentModule parentModule) {
        finish();
    }

    @Override
    public void swapModules(IModule oldModule, IModule newModule, boolean animate) {

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
