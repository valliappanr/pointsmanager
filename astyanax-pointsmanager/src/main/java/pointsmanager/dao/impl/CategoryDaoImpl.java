package pointsmanager.dao.impl;

import org.springframework.stereotype.Repository;

import com.netflix.astyanax.serializers.Int32Serializer;

import pointsmanager.dao.CategoryDao;
import pointsmanager.entitystore.ProductCategoryEvent;
import pointsmanager.entitystore.ProductCategoryRowKey;

@Repository
public class CategoryDaoImpl extends CFDaoImpl<ProductCategoryRowKey, ProductCategoryEvent> implements CategoryDao {

	private static final String CATEGORY_COLUMN_FAMILY_NAME = "product_category";
	public CategoryDaoImpl() {
		super(CATEGORY_COLUMN_FAMILY_NAME, ProductCategoryRowKey.getCategoryrowkeyserializer(), 
				ProductCategoryEvent.getCategoryeventserializer(), Int32Serializer.get());
	}
}
