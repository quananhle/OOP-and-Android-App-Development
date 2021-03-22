package com.company.wizapp.mdui;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.annotation.Nullable;

import com.company.wizapp.R;
import com.company.wizapp.app.SAPWizardApplication;

import com.company.wizapp.app.WizardFlowStateListener;
import com.sap.cloud.mobile.flowv2.core.Flow;
import com.sap.cloud.mobile.flowv2.core.FlowContext;
import com.sap.cloud.mobile.flowv2.core.FlowContextRegistry;
import com.sap.cloud.mobile.flowv2.core.FlowContextBuilder;
import com.sap.cloud.mobile.flowv2.model.FlowConstants;
import com.sap.cloud.mobile.flowv2.model.FlowType;
import com.sap.cloud.mobile.flowv2.core.DialogHelper;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import androidx.preference.ListPreference;
import ch.qos.logback.classic.Level;
import com.sap.cloud.mobile.foundation.settings.policies.LogPolicy;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import com.sap.cloud.mobile.foundation.logging.Logging;

import android.widget.Toast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import androidx.preference.SwitchPreference;
import com.sap.cloud.mobile.foundation.networking.HttpException;
import com.sap.cloud.mobile.foundation.usage.AppUsageUploader;
import com.sap.cloud.mobile.foundation.usage.UsageBroker;
import com.company.wizapp.app.UsageConsentFlow;
import kotlin.jvm.JvmClassMappingKt;
import com.sap.cloud.mobile.foundation.mobileservices.SDKInitializer;
import com.sap.cloud.mobile.foundation.usage.UsageService;

import kotlin.Unit;

/** This fragment represents the settings screen. */
public class SettingsFragment extends PreferenceFragmentCompat implements Logging.UploadListener {

    private ListPreference logLevelPreference;
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsFragment.class);
    private SwitchPreference setUsagePermission;
    private Preference usageUploadPreference;
    private AppUsageUploader.UploadListener usageUploadListener;
    private Long usageUploadStartTime;
    private Preference changePasscodePreference;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        SAPWizardApplication application =
            (SAPWizardApplication) getActivity().getApplication();
        addPreferencesFromResource(R.xml.preferences);
        logLevelPreference = (ListPreference) findPreference(getString(R.string.log_level));
        prepareLogLevelSetting(logLevelPreference);

        // Upload log
        final Preference logUploadPreference = findPreference(getString(R.string.upload_log));
        logUploadPreference.setOnPreferenceClickListener(preference -> {
            logUploadPreference.setEnabled(false);
            Logging.upload();
            return false;
        });

        changePasscodePreference = findPreference(getString(R.string.manage_passcode));
        changePasscodePreference.setOnPreferenceClickListener(preference -> {
            changePasscodePreference.setEnabled(false);
            FlowContext flowContext = new FlowContextBuilder(FlowContextRegistry.getFlowContext())
                    .setFlowType(FlowType.CHANGEPASSCODE)
                    .build();
            Flow.start(this, flowContext);
            return false;
        });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(application);
        boolean usagePref = sp.getBoolean(WizardFlowStateListener.USAGE_SERVICE_PRE,
                false);
        setUsagePermission = (SwitchPreference) findPreference(
                getString(R.string.set_usage_consent));
        setUsagePermission.setChecked(usagePref);
        setUsagePermission.setOnPreferenceClickListener(preference -> {
            boolean started = sp.getBoolean(WizardFlowStateListener.USAGE_SERVICE_PRE, usagePref);
            if( started ) {
                SDKInitializer.INSTANCE.getService(JvmClassMappingKt.getKotlinClass(UsageService.class)).stopUsageBroker(true);
                usageUploadPreference.setEnabled(false);
                sp.edit().putBoolean(WizardFlowStateListener.USAGE_SERVICE_PRE, false).apply();
            } else {
                FlowContext flowContext = new FlowContextBuilder(FlowContextRegistry.getFlowContext())
                        .setFlow(new UsageConsentFlow(application))
                        .build();
                Flow.start(this, flowContext);
            }
            return false;
        });

        // Upload usage
        usageUploadPreference = findPreference(getString(R.string.upload_usage));
        if (!usagePref) {
            getPreferenceScreen().removePreference(usageUploadPreference);
            usageUploadPreference.setEnabled(false);
        }
        usageUploadListener = new AppUsageUploader.UploadListener() {
            @Override
            public void onSuccess() {
                usageUploadPreference.setEnabled(true);
                if (usageUploadStartTime != null) {
                    LOGGER.debug("Usage upload complete, time taken (in nanos): " + (System.nanoTime() - usageUploadStartTime));
                }
                Toast.makeText(getActivity(), R.string.usage_upload_ok, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error) {
                usageUploadPreference.setEnabled(true);
                if (error instanceof HttpException) {
                    LOGGER.debug("Usage Upload server error: {}, code = {}", ((HttpException) error).message(), ((HttpException) error).code());
                } else {
                    LOGGER.debug("Usage Upload error: {}", error.getMessage());
                }
                new DialogHelper(getActivity(), R.style.OnboardingDefaultTheme_Dialog_Alert)
                        .showOKOnlyDialog(
                                getActivity().getSupportFragmentManager(),
                                error.getLocalizedMessage(),
                                null, null, null
                        );
            }

            @Override
            public void onProgress(int i) {
                LOGGER.debug("Usage upload progress: " + i);
            }
        };
        usageUploadPreference.setOnPreferenceClickListener(preference -> {
            usageUploadPreference.setEnabled(false);
            usageUploadStartTime = System.nanoTime();
            UsageBroker.upload(getContext(), false);
            return false;
        });

        // Reset App
        Preference resetAppPreference = findPreference(getString(R.string.reset_app));
        resetAppPreference.setOnPreferenceClickListener(preference -> {
            FlowContext flowContext = new FlowContextBuilder(FlowContextRegistry.getFlowContext())
                .setFlowType(FlowType.RESET)
                .build();
            Flow.start(this, flowContext);
            return false;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FlowConstants.FLOW_ACTIVITY_REQUEST_CODE) {
            changePasscodePreference.setEnabled(true);
            boolean usageStarted = UsageBroker.isStarted();
            setUsagePermission.setChecked(usageStarted);
            usageUploadPreference.setEnabled(usageStarted);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logging.addUploadListener(this);
        AppUsageUploader.addUploadListener(usageUploadListener);
        logLevelPreference = findPreference(getString(R.string.log_level));
        prepareLogLevelSetting(logLevelPreference);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logging.removeUploadListener(this);
        AppUsageUploader.removeUploadListener(usageUploadListener);
    }

    @Override
    public void onSuccess() {
        enableLogUploadButton();
        Toast.makeText(getActivity(), R.string.log_upload_ok, Toast.LENGTH_LONG).show();
        LOGGER.info("Log is uploaded to the server.");
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        enableLogUploadButton();
        new DialogHelper(getActivity(), R.style.OnboardingDefaultTheme_Dialog_Alert)
                .showOKOnlyDialog(
                        getActivity().getSupportFragmentManager(),
                        throwable.getMessage(),
                        null, null, null
                );
        LOGGER.error("Log upload failed with error message: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onProgress(int i) {
        // You could add a progress indicator and update it from here
    }

    private void enableLogUploadButton() {
        final Preference logUploadPreference = findPreference(getString(R.string.upload_log));
        logUploadPreference.setEnabled(true);
    }


    private void prepareLogLevelSetting(ListPreference listPreference) {
        Map<Level,String> mapping = getLevelStrings();
        logLevelPreference.setEntries(mapping.values().toArray(new String[0]));
        logLevelPreference.setEntryValues(getLevelValues());
        logLevelPreference.setPersistent(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String logString = sp.getString(SAPWizardApplication.KEY_LOG_SETTING_PREFERENCE, new LogPolicy().toString());
        LogPolicy settings = LogPolicy.createFromJsonString(logString);

        logLevelPreference.setSummary(mapping.get(LogPolicy.getLogLevel(settings)));
        logLevelPreference.setValue(String.valueOf(LogPolicy.getLogLevel(settings).levelInt));
        logLevelPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            // Get the new value
            Level logLevel = Level.toLevel(Integer.valueOf((String) newValue));
            LogPolicy newSettings = settings.copy(
                    settings.getEnabled(), settings.getMaxFileSize(),
                    LogPolicy.getLogLevelString(logLevel),
                    settings.getEntryExpiry(), settings.getMaxFileNumber()
            );
            sp.edit().putString(SAPWizardApplication.KEY_LOG_SETTING_PREFERENCE, newSettings.toString()).apply();
            LogPolicy.setRootLogLevel(newSettings);
            preference.setSummary(mapping.get(LogPolicy.getLogLevel(newSettings)));

            return true;
        });
    }

    private Map<Level, String> getLevelStrings() {
        Map<Level, String> mapping = new LinkedHashMap<>();
        mapping.put(Level.ALL, getString(R.string.log_level_path));
        mapping.put(Level.DEBUG, getString(R.string.log_level_debug));
        mapping.put(Level.INFO, getString(R.string.log_level_info));
        mapping.put(Level.WARN, getString(R.string.log_level_warning));
        mapping.put(Level.ERROR, getString(R.string.log_level_error));
        mapping.put(Level.OFF, getString(R.string.log_level_none));
        return mapping;
    }

    private String[] getLevelValues() {
        return new String[]{
                String.valueOf(Level.ALL.levelInt),
                String.valueOf(Level.DEBUG.levelInt),
                String.valueOf(Level.INFO.levelInt),
                String.valueOf(Level.WARN.levelInt),
                String.valueOf(Level.ERROR.levelInt),
                String.valueOf(Level.OFF.levelInt)};
    }
}
