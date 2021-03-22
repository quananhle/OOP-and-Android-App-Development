package com.company.wizapp.viewmodel.salesorderheader;

import android.app.Application;
import android.os.Parcelable;

import com.company.wizapp.viewmodel.EntityViewModel;
import com.sap.cloud.android.odata.espmcontainer.SalesOrderHeader;
import com.sap.cloud.android.odata.espmcontainer.ESPMContainerMetadata.EntitySets;

/*
 * Represents View model for SalesOrderHeader
 * Having an entity view model for each <T> allows the ViewModelProvider to cache and
 * return the view model of that type. This is because the ViewModelStore of
 * ViewModelProvider cannot not be able to tell the difference between EntityViewModel<type1>
 * and EntityViewModel<type2>.
 */
public class SalesOrderHeaderViewModel extends EntityViewModel<SalesOrderHeader> {

    /**
    * Default constructor for a specific view model.
    * @param application - parent application
    */
    public SalesOrderHeaderViewModel(Application application) {
        super(application, EntitySets.salesOrderHeaders, SalesOrderHeader.createdAt);
    }

    /**
    * Constructor for a specific view model with navigation data.
    * @param application - parent application
    * @param navigationPropertyName - name of the navigation property
    * @param entityData - parent entity (starting point of the navigation)
    */
	 public SalesOrderHeaderViewModel(Application application, String navigationPropertyName, Parcelable entityData) {
        super(application, EntitySets.salesOrderHeaders, SalesOrderHeader.createdAt, navigationPropertyName, entityData);
    }
}
