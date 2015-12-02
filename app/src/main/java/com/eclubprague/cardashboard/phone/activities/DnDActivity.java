package com.eclubprague.cardashboard.phone.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;
import com.eclubprague.cardashboard.core.model.resources.StringResource;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.fragments.DnDFragment;
import com.mobeta.android.dslv.DragSortController;


public class DnDActivity extends Activity implements IModuleContext{

    private static final String TAG_DSLV_FRAGMENT = "dslv_fragment";
    public static final String TAG = DnDActivity.class.getSimpleName();

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalDataProvider.getInstance().setModuleContext(this);

        setContentView(R.layout.test_bed_main);
        if (mFragment == null)
            mFragment = getNewDslvFragment();
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag(TAG_DSLV_FRAGMENT) == null) {
            fm.beginTransaction()
                    .add(R.id.test_bed, mFragment, TAG_DSLV_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void setFragment(Fragment f) {
        mFragment = f;
    }


    private Fragment getNewDslvFragment() {
        DnDFragment f = new DnDFragment();
        f.removeMode = DragSortController.FLING_REMOVE;
        f.removeEnabled = true;
        f.dragStartMode = DragSortController.ON_DRAG;
        f.sortEnabled = true;
        f.dragEnabled = true;
        return f;
    }

    @Override
    public void goToParentModule(IParentModule parentModule) {

    }

    @Override
    public void goBackFromParentModule(IParentModule previousParentModule) {

    }

    @Override
    public void toggleQuickMenu(IModule module, boolean activate) {

    }

    @Override
    public void turnQuickMenusOff() {

    }

    @Override
    public void launchIntent(Intent intent, StringResource errorMessage) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onModuleEvent(IModule module, ModuleEvent event) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ViewGroup getSnackbarHolder() {
        return null;
    }

    @Override
    public void restartActivity() {

    }

    @Override
    public void restartApplication() {

    }
}
