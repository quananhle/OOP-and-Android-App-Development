package com.company.wizapp.app;

import android.content.Intent;
import com.sap.cloud.mobile.flowv2.ext.FlowStateListener;
import com.sap.cloud.mobile.foundation.model.AppConfig;
import android.content.SharedPreferences;
import com.sap.cloud.mobile.foundation.authentication.AppLifecycleCallbackHandler;
import com.sap.cloud.mobile.foundation.settings.policies.ClientPolicies;
import com.sap.cloud.mobile.foundation.settings.policies.LogPolicy;
import ch.qos.logback.classic.Level;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import com.company.wizapp.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.cloud.mobile.flowv2.ext.ConsentType;
import kotlin.Pair;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WizardFlowStateListener extends FlowStateListener {
    private static Logger logger = LoggerFactory.getLogger(WizardFlowStateListener.class);
    public static final String USAGE_SERVICE_PRE = "pref_usage_service";
    public static final String CRASH_SERVICE_PRE = "pref_crash_service";
    private final SAPWizardApplication application;

    public WizardFlowStateListener(@NotNull SAPWizardApplication application) {
        super();
        this.application = application;
    }

    @Override
    public void onAppConfigRetrieved(@NotNull AppConfig appConfig) {
        logger.debug(String.format("onAppConfigRetrieved: %s", appConfig.toString()));
        application.initializeServiceManager(appConfig);
        application.setAppConfig(appConfig);
    }

    @Override
    public void onApplicationReset() {
        this.application.resetApp();
        Intent intent = new Intent(application, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        application.startActivity(intent);
    }

    @Override
    public void onApplicationLocked() {
        super.onApplicationLocked();
        application.isApplicationUnlocked = false;
    }

    @Override
    public void onFlowFinished(@Nullable String flowName) {
        if(flowName != null) {
            application.isApplicationUnlocked = true;
        }
    }

    @Override
    public void onClientPolicyRetrieved(@NotNull ClientPolicies policies) {
        SharedPreferences sp = application.sp;
        String logString = sp.getString(SAPWizardApplication.KEY_LOG_SETTING_PREFERENCE, "");
        LogPolicy currentSettings;
        if (logString.isEmpty()) {
            currentSettings = new LogPolicy();
        } else {
            currentSettings = LogPolicy.createFromJsonString(logString);
        }

        LogPolicy logSettings = policies.getLogPolicy();
        if (!currentSettings.getLogLevel().equals(logSettings.getLogLevel()) || logString.isEmpty()) {
            sp.edit().putString(SAPWizardApplication.KEY_LOG_SETTING_PREFERENCE,
                    logSettings.toString()).apply();
            LogPolicy.setRootLogLevel(logSettings);
            AppLifecycleCallbackHandler.getInstance().getActivity().runOnUiThread(() -> {
                Map mapping = new HashMap<Level, String>();
                mapping.put(Level.ALL, application.getString(R.string.log_level_path));
                mapping.put(Level.DEBUG, application.getString(R.string.log_level_debug));
                mapping.put(Level.INFO, application.getString(R.string.log_level_info));
                mapping.put(Level.WARN, application.getString(R.string.log_level_warning));
                mapping.put(Level.ERROR, application.getString(R.string.log_level_error));
                mapping.put(Level.OFF, application.getString(R.string.log_level_none));
                Toast.makeText(
                        application,
                        String.format(
                                application.getString(R.string.log_level_changed),
                                mapping.get(LogPolicy.getLogLevel(logSettings))
                        ),
                        Toast.LENGTH_SHORT
                ).show();
                logger.info(String.format(
                                application.getString(R.string.log_level_changed),
                                mapping.get(LogPolicy.getLogLevel(logSettings))
                        ));
            });
        }
    }

    @Override
    public void onConsentStatusChange(
            @NotNull List<? extends Pair<? extends ConsentType, Boolean>> consents) {
        SharedPreferences sp = application.sp;
        for( Pair<? extends ConsentType, Boolean> consent: consents ) {
            ConsentType first = consent.getFirst();
            if (first == ConsentType.USAGE) {
                sp.edit().putBoolean(USAGE_SERVICE_PRE, consent.getSecond()).apply();
            } else if (first == ConsentType.CRASH_REPORT) {
                sp.edit().putBoolean(CRASH_SERVICE_PRE, consent.getSecond()).apply();
            }
        }
    }

}
