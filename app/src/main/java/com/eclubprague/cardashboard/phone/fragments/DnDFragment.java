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
import com.eclubprague.cardashboard.core.fragments.ModuleListDialogFragment;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.core.modules.predefined.EmptyModule;
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


public class DnDFragment extends Fragment implements View.OnClickListener {

    public static String TAG = DnDFragment.class.getSimpleName();
    private ArrayAdapter<IModule> mAdapter;
    private IParentModule mGlobalParentModule = ModuleSupplier.getPersonalInstance().getHomeScreenModule(GlobalDataProvider.getInstance().getModuleContext());
    private IParentModule mCurrentParentModule;
    private static boolean changed = false;

    public static final String PARENT_MODULES_SCOPE_ID = "parentModulesScopeId";


    private final DragSortListView.DropListener mDropListener =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {

                    if (from != to) {
                        Log.d(TAG, mCurrentParentModule.getSubmodules().toString());
                        IModule item = mAdapter.getItem(from);
                        mAdapter.remove(item);
                        mAdapter.insert(item, to);
                        try {
                            ModuleDAO.saveParentModuleAsync((IModuleContext) GlobalDataProvider.getInstance().getActivity(), mCurrentParentModule);
                            Log.d(TAG, mCurrentParentModule.getSubmodules().toString());
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
                    try {
                        mAdapter.remove(mAdapter.getItem(which));
                        ScreenSlideActivity.modulesOrderChanged = true;
                        ModuleDAO.saveParentModuleAsync((IModuleContext) GlobalDataProvider.getInstance().getActivity(), mGlobalParentModule);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
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
        fab1.setOnClickListener(this);


        return rootview;
    }

    ModuleId parentModuleId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDslv.setDropListener(mDropListener);
        mDslv.setRemoveListener(mRemoveListener);

        parentModuleId = (ModuleId) getActivity().getIntent().getSerializableExtra(DnDFragment.PARENT_MODULES_SCOPE_ID);
        if (parentModuleId == null) {
            mCurrentParentModule = mGlobalParentModule;
        } else {
            mCurrentParentModule = (IParentModule) ModuleSupplier.getPersonalInstance().findModule(GlobalDataProvider.getInstance().getModuleContext(), parentModuleId);
        }
        if (mCurrentParentModule == null) return;


        mAdapter = new IModuleArrayAdapter(getActivity(), R.id.text, mCurrentParentModule.getSubmodules());
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

        getActivity().setTitle(mCurrentParentModule.getTitle().getString());
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
    public void onClick(View v) {
        ModuleListDialogFragment dialog = ModuleListDialogFragment.newInstance((IModuleContext) getActivity(), new ModuleListDialogFragment.OnAddModuleListener() {
            @Override
            public void addModule(IModule module) {
                try {
                    mCurrentParentModule.addSubmodules(module);
                    ModuleDAO.saveParentModuleAsync((IModuleContext) GlobalDataProvider.getInstance().getActivity(), mGlobalParentModule);
                    ScreenSlideActivity.modulesOrderChanged = true;
                    mAdapter = new IModuleArrayAdapter(getActivity(), R.id.text, mCurrentParentModule.getSubmodules());
                    mDslv.setAdapter(mAdapter);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(dialog, null);
        ft.commitAllowingStateLoss();

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
