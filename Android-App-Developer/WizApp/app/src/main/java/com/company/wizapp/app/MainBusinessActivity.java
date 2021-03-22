package com.company.wizapp.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.company.wizapp.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.wizapp.mdui.EntitySetListActivity;

public class MainBusinessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_business);
    }

    private void startEntitySetListActivity() {
        SAPWizardApplication application = (SAPWizardApplication) getApplication();
        application.getSAPServiceManager().openODataStore(() -> {
            Intent intent = new Intent(this, EntitySetListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startEntitySetListActivity();
    }

}
