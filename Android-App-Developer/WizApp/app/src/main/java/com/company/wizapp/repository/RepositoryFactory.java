package com.company.wizapp.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.company.wizapp.service.SAPServiceManager;

import com.sap.cloud.android.odata.espmcontainer.ESPMContainer;
import com.sap.cloud.android.odata.espmcontainer.ESPMContainerMetadata.EntitySets;

import com.sap.cloud.android.odata.espmcontainer.Customer;
import com.sap.cloud.android.odata.espmcontainer.ProductCategory;
import com.sap.cloud.android.odata.espmcontainer.ProductText;
import com.sap.cloud.android.odata.espmcontainer.Product;
import com.sap.cloud.android.odata.espmcontainer.PurchaseOrderHeader;
import com.sap.cloud.android.odata.espmcontainer.PurchaseOrderItem;
import com.sap.cloud.android.odata.espmcontainer.SalesOrderHeader;
import com.sap.cloud.android.odata.espmcontainer.SalesOrderItem;
import com.sap.cloud.android.odata.espmcontainer.Stock;
import com.sap.cloud.android.odata.espmcontainer.Supplier;

import com.sap.cloud.mobile.odata.EntitySet;
import com.sap.cloud.mobile.odata.Property;

import java.util.WeakHashMap;

/*
 * Repository factory to construct repository for an entity set
 */
public class RepositoryFactory {

    /*
     * Cache all repositories created to avoid reconstruction and keeping the entities of entity set
     * maintained by each repository in memory. Use a weak hash map to allow recovery in low memory
     * conditions
     */
    private WeakHashMap<String, Repository> repositories;
    /*
     * Service manager to interact with OData service
     */
    private SAPServiceManager sapServiceManager;
    /**
     * Construct a RepositoryFactory instance. There should only be one repository factory and used
     * throughout the life of the application to avoid caching entities multiple times.
     * @param sapServiceManager - Service manager for interaction with OData service
     */
    public RepositoryFactory(SAPServiceManager sapServiceManager) {
        repositories = new WeakHashMap<>();
        this.sapServiceManager = sapServiceManager;
    }

    /**
     * Construct or return an existing repository for the specified entity set
     * @param entitySet - entity set for which the repository is to be returned
     * @param orderByProperty - if specified, collection will be sorted ascending with this property
     * @return a repository for the entity set
     */
    public Repository getRepository(@NonNull EntitySet entitySet, @Nullable Property orderByProperty) {
        ESPMContainer eSPMContainer = sapServiceManager.getESPMContainer();
        String key = entitySet.getLocalName();
        Repository repository = repositories.get(key);
        if (repository == null) {
            if (key.equals(EntitySets.customers.getLocalName())) {
                repository = new Repository<Customer>(eSPMContainer, EntitySets.customers, orderByProperty);
            } else if (key.equals(EntitySets.productCategories.getLocalName())) {
                repository = new Repository<ProductCategory>(eSPMContainer, EntitySets.productCategories, orderByProperty);
            } else if (key.equals(EntitySets.productTexts.getLocalName())) {
                repository = new Repository<ProductText>(eSPMContainer, EntitySets.productTexts, orderByProperty);
            } else if (key.equals(EntitySets.products.getLocalName())) {
                repository = new Repository<Product>(eSPMContainer, EntitySets.products, orderByProperty);
            } else if (key.equals(EntitySets.purchaseOrderHeaders.getLocalName())) {
                repository = new Repository<PurchaseOrderHeader>(eSPMContainer, EntitySets.purchaseOrderHeaders, orderByProperty);
            } else if (key.equals(EntitySets.purchaseOrderItems.getLocalName())) {
                repository = new Repository<PurchaseOrderItem>(eSPMContainer, EntitySets.purchaseOrderItems, orderByProperty);
            } else if (key.equals(EntitySets.salesOrderHeaders.getLocalName())) {
                repository = new Repository<SalesOrderHeader>(eSPMContainer, EntitySets.salesOrderHeaders, orderByProperty);
            } else if (key.equals(EntitySets.salesOrderItems.getLocalName())) {
                repository = new Repository<SalesOrderItem>(eSPMContainer, EntitySets.salesOrderItems, orderByProperty);
            } else if (key.equals(EntitySets.stock.getLocalName())) {
                repository = new Repository<Stock>(eSPMContainer, EntitySets.stock, orderByProperty);
            } else if (key.equals(EntitySets.suppliers.getLocalName())) {
                repository = new Repository<Supplier>(eSPMContainer, EntitySets.suppliers, orderByProperty);
            } else {
                throw new AssertionError("Fatal error, entity set[" + key + "] missing in generated code");
            }
            repositories.put(key, repository);
        }
        return repository;
    }

    /**
     * Get rid of all cached repositories
     */
    public void reset() {
        repositories.clear();
    }
 }
