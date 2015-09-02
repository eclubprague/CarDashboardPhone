package com.eclubprague.cardashboard.phone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.phone.R;

public class ScreenSlidePageFragment extends Fragment {
    private static final String TAG = ScreenSlidePageFragment.class.getSimpleName();
    private IModule module;

    public static ScreenSlidePageFragment newInstance(IModule module) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setModule(module);
        Bundle b = new Bundle();
        b.putSerializable("moduleId", module.getId());
        fragment.setArguments(b);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            ModuleId moduleId = (ModuleId) b.getSerializable("moduleId");
            this.module = ModuleSupplier.getBaseInstance().findModule((IModuleContext) getActivity(), moduleId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("moduleId", this.module.getId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        LinearLayout cardWrapper = (LinearLayout) rootView.findViewById(R.id.card_wrapper);
        ViewSwitcher moduleContent = (ViewSwitcher) (this.module.createViewWithHolder((IModuleContext)getActivity(), R.layout.module_holder, cardWrapper)).holder;
        moduleContent.addView(this.module.createQuickMenuView((IModuleContext)getActivity(), moduleContent));
        cardWrapper.addView(moduleContent);
        return rootView;
    }

    public void setModule(IModule module) {
        this.module = module;
    }


}
