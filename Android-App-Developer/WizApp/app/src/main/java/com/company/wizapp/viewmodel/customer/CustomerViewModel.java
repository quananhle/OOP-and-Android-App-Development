package com.company.wizapp.viewmodel.customer;

import android.app.Application;
import android.os.Parcelable;

import com.company.wizapp.viewmodel.EntityViewModel;
import com.sap.cloud.android.odata.espmcontainer.Customer;
import com.sap.cloud.android.odata.espmcontainer.ESPMContainerMetadata.EntitySets;

/*
 * Represents View model for Customer
 * Having an entity view model for each <T> allows the ViewModelProvider to cache and
 * return the view model of that type. This is because the ViewModelStore of
 * ViewModelProvider cannot not be able to tell the difference between EntityViewModel<type1>
 * and EntityViewModel<type2>.
 */
public class CustomerViewModel extends EntityViewModel<Customer> {

    /**
    * Default constructor for a specific view model.
    * @param application - parent application
    */
    public CustomerViewModel(Application application) {
        super(application, EntitySets.customers, Customer.city);
    }

    /**
    * Constructor for a specific view model with navigation data.
    * @param application - parent application
    * @param navigationPropertyName - name of the navigation property
    * @param entityData - parent entity (starting point of the navigation)
    */
	 public CustomerViewModel(Application application, String navigationPropertyName, Parcelable entityData) {
        super(application, EntitySets.customers, Customer.city, navigationPropertyName, entityData);
    }
}
