package com.eclubprague.cardashboard.phone.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.fragments.DnDFragment;
import com.mobeta.android.dslv.DragSortController;


public class DnDActivity extends FragmentActivity {

    private static final String TAG_DSLV_FRAGMENT = "dslv_fragment";
    public static final String TAG = DnDActivity.class.getSimpleName();

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFragment == null)
            mFragment = getNewDslvFragment();
        setContentView(R.layout.test_bed_main);
        FragmentManager fm = getSupportFragmentManager();
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

}
