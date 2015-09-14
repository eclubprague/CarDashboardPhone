package com.eclubprague.cardashboard.phone.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;
import com.eclubprague.cardashboard.core.data.database.ModuleDAO;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.phone.R;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class DnDFragment extends ListFragment {

    private ArrayAdapter<String> mAdapter;

    private final DragSortListView.DropListener mDropListener =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        String item = mAdapter.getItem(from);
                        mAdapter.remove(item);
                        mAdapter.insert(item, to);
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
        mDslv = (DragSortListView) inflater.inflate(R.layout.dslv_fragment_main, container, false);

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

        return mDslv;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDslv = (DragSortListView) getListView();

        mDslv.setDropListener(mDropListener);
        mDslv.setRemoveListener(mRemoveListener);


        IParentModule parentModule = null;
        try {
            parentModule = ModuleDAO.loadParentModule(GlobalDataProvider.getInstance().getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (parentModule == null) return;


        List<String> names = new LinkedList<>();
        for (IModule module : parentModule.getSubmodules()) {
            names.add(module.getTitle().getString());
        }

        mAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_handle_left, R.id.text, names);
        setListAdapter(mAdapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String message = String.format("Clicked item %d", arg2);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                           long arg3) {
                String message = String.format("Long-clicked item %d", arg2);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


}
