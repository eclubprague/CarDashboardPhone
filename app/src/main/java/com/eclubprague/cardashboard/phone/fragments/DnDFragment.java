package com.eclubprague.cardashboard.phone.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;
import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.data.database.ModuleDAO;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.phone.R;
import com.eclubprague.cardashboard.phone.activities.DnDActivity;
import com.eclubprague.cardashboard.phone.activities.ScreenSlideActivity;
import com.eclubprague.cardashboard.phone.fab.FloatingActionButton;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class DnDFragment extends Fragment implements FloatingActionButton.OnCheckedChangeListener {

    public static String TAG = DnDFragment.class.getSimpleName();
    private ArrayAdapter<IModule> mAdapter;
    private IParentModule mParentModule;

    public static final String PARENT_MODULES_SCOPE_ID = "parentModulesScopeId";


    private final DragSortListView.DropListener mDropListener =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {

                    if (from != to) {
                        Log.d(TAG, mParentModule.getSubmodules().toString());
                        IModule item = mAdapter.getItem(from);
                        mAdapter.remove(item);
                        mAdapter.insert(item, to);
                        try {
                            ModuleDAO.saveParentModuleAsync(GlobalDataProvider.getInstance().getActivity(), mParentModule);
                            Log.d(TAG, mParentModule.getSubmodules().toString());
                            ScreenSlideActivity.modulesOrderChanged = true;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            };

    private final DragSortListView.RemoveListener mRemoveListener =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    mAdapter.remove(mAdapter.getItem(which));
                }
            };

    private DragSortListView mDslv;
    private DragSortController mController;

    public int dragStartMode = DragSortController.ON_DOWN;
    public boolean removeEnabled = false;
    public int removeMode = DragSortController.FLING_REMOVE;
    public boolean sortEnabled = true;
    public boolean dragEnabled = true;


    public DnDFragment() {
        super();
    }

    public DragSortController getController() {
        return mController;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dslv_fragment_main, container, false);
        mDslv = (DragSortListView) rootview.findViewById(R.id.DndList);

        // defaults are
        //   dragStartMode = onDown
        //   removeMode = flingRight
        DragSortController controller = new DragSortController(mDslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(removeEnabled);
        controller.setSortEnabled(sortEnabled);
        controller.setDragInitMode(dragStartMode);
        controller.setRemoveMode(removeMode);

        mController = controller;
        mDslv.setFloatViewManager(mController);
        mDslv.setOnTouchListener(mController);
        mDslv.setDragEnabled(dragEnabled);
        FloatingActionButton fab1 = (FloatingActionButton) rootview.findViewById(R.id.fab_1);
        fab1.setOnCheckedChangeListener(this);
        FloatingActionButton fab2 = (FloatingActionButton) rootview.findViewById(R.id.fab_2);
        fab2.setOnCheckedChangeListener(this);


        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDslv.setDropListener(mDropListener);
        mDslv.setRemoveListener(mRemoveListener);

        ModuleId parentModuleId = (ModuleId) getActivity().getIntent().getSerializableExtra(DnDFragment.PARENT_MODULES_SCOPE_ID);
        if (parentModuleId == null) {

            mParentModule = ModuleSupplier.getPersonalInstance().getHomeScreenModule(GlobalDataProvider.getInstance().getModuleContext());

        } else {
            mParentModule = (IParentModule) ModuleSupplier.getPersonalInstance().findModule(GlobalDataProvider.getInstance().getModuleContext(), parentModuleId);
        }
        if (mParentModule == null) return;


        //mAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_handle_left, R.id.text, mParentModule.getSubmodules());
        mAdapter = new IModuleArrayAdapter(getActivity(), R.id.text, mParentModule.getSubmodules());
        mDslv.setAdapter(mAdapter);
        ListView lv = mDslv;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemNumber,
                                    long arg3) {
                IModule current = mAdapter.getItem(clickedItemNumber);
                if (current instanceof IParentModule) {


                    Intent intent = new Intent(getActivity(), DnDActivity.class);
                    intent.putExtra(DnDFragment.PARENT_MODULES_SCOPE_ID, current.getId());
                    startActivity(intent);
                }
            }
        });
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
//                                           long arg3) {
//                String message = String.format("Long-clicked item %d", arg2);
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    @Override
    public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        switch (fabView.getId()) {
            case R.id.fab_1:
                Log.d(TAG, String.format("FAB 1 was %s.", isChecked ? "checked" : "unchecked"));
                break;
            case R.id.fab_2:
                Log.d(TAG, String.format("FAB 2 was %s.", isChecked ? "checked" : "unchecked"));
                break;
            default:
                break;
        }
    }


    private class IModuleArrayAdapter extends ArrayAdapter<IModule> {

        private class ViewHolder {
            private TextView itemView;
        }

        public IModuleArrayAdapter(Context context, int textViewResourceId, List<IModule> items) {
            super(context, textViewResourceId, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.list_item_handle_left, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.itemView = (TextView) convertView.findViewById(R.id.text);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            IModule item = getItem(position);
            if (item != null) {
                // My layout has only one TextView
                // do whatever you want with your string and long
                viewHolder.itemView.setText(item.getTitle().getString());
            }

            return convertView;
        }

    }
}
