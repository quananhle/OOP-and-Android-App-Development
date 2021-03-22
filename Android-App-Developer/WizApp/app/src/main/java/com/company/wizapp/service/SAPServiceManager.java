package com.company.wizapp.service;

import com.sap.cloud.android.odata.espmcontainer.ESPMContainer;
import com.sap.cloud.mobile.foundation.model.AppConfig;
import com.sap.cloud.mobile.foundation.common.ClientProvider;
import com.sap.cloud.mobile.odata.OnlineODataProvider;
import com.sap.cloud.mobile.odata.core.Action0;
import com.sap.cloud.mobile.odata.http.OKHttpHandler;

public class SAPServiceManager {

    private final AppConfig appConfig;
    private OnlineODataProvider provider;
    private String serviceRoot;
    ESPMContainer eSPMContainer;
    public static final String CONNECTION_ID_ESPMCONTAINER = "com.sap.edm.sampleservice.v2";

    public SAPServiceManager(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void openODataStore(Action0 callback) {
        if (appConfig != null) {
            String serviceUrl = appConfig.getServiceUrl();
            provider = new OnlineODataProvider("SAPService", serviceUrl + CONNECTION_ID_ESPMCONTAINER);
            provider.getNetworkOptions().setHttpHandler(new OKHttpHandler(ClientProvider.get()));
            provider.getServiceOptions().setCheckVersion(false);
            provider.getServiceOptions().setRequiresType(true);
            provider.getServiceOptions().setCacheMetadata(false);
            eSPMContainer = new ESPMContainer(provider);

        }
        callback.call();
    }

    public String getServiceRoot() {
        if (serviceRoot == null) {
            if (eSPMContainer == null) {
                throw new IllegalStateException("SAPServiceManager was not initialized");
            }
            provider = (OnlineODataProvider)eSPMContainer.getProvider();
            serviceRoot = provider.getServiceRoot();
        }
        return serviceRoot;
    }

    // This getter is used for the master-detail ui generation
    public ESPMContainer getESPMContainer() {
        if (eSPMContainer == null) {
            throw new IllegalStateException("SAPServiceManager was not initialized");
        }
        return eSPMContainer;
    }

}