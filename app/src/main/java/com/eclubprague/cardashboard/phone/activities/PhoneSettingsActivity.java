package com.eclubprague.cardashboard.phone.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ViewGroup;

import com.eclubprague.cardashboard.core.application.GlobalDataProvider;
import com.eclubprague.cardashboard.core.model.resources.StringResource;
import com.eclubprague.cardashboard.core.modules.base.IModule;
import com.eclubprague.cardashboard.core.modules.base.IModuleContext;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.ModuleEvent;
import com.eclubprague.cardashboard.core.preferences.SettingsActivity;

public class PhoneSettingsActivity extends SettingsActivity implements IModuleContext {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        GlobalDataProvider.getInstance().setModuleContext(this);
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
