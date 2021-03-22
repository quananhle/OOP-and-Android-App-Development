package com.company.wizapp.app;

import android.app.Application;

import com.sap.cloud.mobile.flowv2.core.Flow;
import com.sap.cloud.mobile.flowv2.steps.UsageConsentFragment;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.JvmClassMappingKt;

/**
 * A custom flow for usage consent in settings page
 */
public class UsageConsentFlow extends Flow {
    public UsageConsentFlow(@NotNull Application application) {
        super(application);
        addSingleStep(com.sap.cloud.mobile.flowv2.R.id.stepUsageConsent, JvmClassMappingKt.getKotlinClass(UsageConsentFragment.class));
    }
}