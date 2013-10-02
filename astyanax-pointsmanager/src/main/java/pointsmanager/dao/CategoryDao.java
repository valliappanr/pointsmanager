package pointsmanager.dao;

import pointsmanager.entitystore.ProductCategoryEvent;
import pointsmanager.entitystore.ProductCategoryRowKey;

public interface CategoryDao extends CFDao<ProductCategoryRowKey, ProductCategoryEvent>{

}
