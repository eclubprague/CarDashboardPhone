package com.eclubprague.cardashboard.phone.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.phone.R;

public class ModuleActivity extends ScreenSlideActivity {
    public static final String KEY_PARENT_MODULE = ModuleActivity.class.getName() + ".KEY_PARENT_MODULE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IParentModule module;
        if (getIntent() == null || getIntent().getSerializableExtra(KEY_PARENT_MODULE) == null) {
            module = ModuleSupplier.getInstance().getHomeScreenModule(this);
        } else {
            Intent intent = getIntent();
            ModuleId parentModuleId = (ModuleId) intent.getSerializableExtra(KEY_PARENT_MODULE);
            module = ModuleSupplier.getInstance().findSubmenuModule(this, parentModuleId);
        }
        setModule(module);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_module, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
