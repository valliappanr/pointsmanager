package pointsmanager.dao.impl;

import org.springframework.stereotype.Repository;

import com.netflix.astyanax.serializers.Int32Serializer;

import pointsmanager.dao.CategoryDao;
import pointsmanager.entitystore.CategoryEvent;
import pointsmanager.entitystore.CategoryRowKey;

@Repository
public class CategoryDaoImpl extends CFDaoImpl<CategoryRowKey, CategoryEvent> implements CategoryDao {

	private static final String CATEGORY_COLUMN_FAMILY_NAME = "product_category";
	public CategoryDaoImpl() {
		super(CATEGORY_COLUMN_FAMILY_NAME, CategoryRowKey.getCategoryrowkeyserializer(), 
				CategoryEvent.getCategoryeventserializer(), Int32Serializer.get());
	}
}
