package com.eclubprague.cardashboard.phone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.eclubprague.cardashboard.core.data.ModuleSupplier;
import com.eclubprague.cardashboard.core.modules.base.IParentModule;
import com.eclubprague.cardashboard.core.modules.base.models.ModuleId;
import com.eclubprague.cardashboard.phone.R;

import java.io.IOException;

public class ModuleActivity extends ScreenSlideActivity {
    public static final String KEY_PARENT_MODULE = ModuleActivity.class.getName() + ".KEY_PARENT_MODULE";
    public static final String TAG = ModuleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IParentModule module;
        if (getIntent() == null || getIntent().getSerializableExtra(KEY_PARENT_MODULE) == null) {
            module = ModuleSupplier.getPersonalInstance().getHomeScreenModule(this);
        } else {
            Intent intent = getIntent();
            ModuleId parentModuleId = (ModuleId) intent.getSerializableExtra(KEY_PARENT_MODULE);
            module = ModuleSupplier.getPersonalInstance().findSubmenuModule(this, parentModuleId);
        }
        setModule(module);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            ModuleSupplier.getPersonalInstance().save(this);
        } catch (IOException e) {
            Log.e(TAG, "Saving modules was not successful.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_module, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PhoneSettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}
