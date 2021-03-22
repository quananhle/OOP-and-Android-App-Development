package com.company.wizapp.viewmodel.purchaseorderitem;

import android.app.Application;
import android.os.Parcelable;

import com.company.wizapp.viewmodel.EntityViewModel;
import com.sap.cloud.android.odata.espmcontainer.PurchaseOrderItem;
import com.sap.cloud.android.odata.espmcontainer.ESPMContainerMetadata.EntitySets;

/*
 * Represents View model for PurchaseOrderItem
 * Having an entity view model for each <T> allows the ViewModelProvider to cache and
 * return the view model of that type. This is because the ViewModelStore of
 * ViewModelProvider cannot not be able to tell the difference between EntityViewModel<type1>
 * and EntityViewModel<type2>.
 */
public class PurchaseOrderItemViewModel extends EntityViewModel<PurchaseOrderItem> {

    /**
    * Default constructor for a specific view model.
    * @param application - parent application
    */
    public PurchaseOrderItemViewModel(Application application) {
        super(application, EntitySets.purchaseOrderItems, PurchaseOrderItem.currencyCode);
    }

    /**
    * Constructor for a specific view model with navigation data.
    * @param application - parent application
    * @param navigationPropertyName - name of the navigation property
    * @param entityData - parent entity (starting point of the navigation)
    */
	 public PurchaseOrderItemViewModel(Application application, String navigationPropertyName, Parcelable entityData) {
        super(application, EntitySets.purchaseOrderItems, PurchaseOrderItem.currencyCode, navigationPropertyName, entityData);
    }
}
