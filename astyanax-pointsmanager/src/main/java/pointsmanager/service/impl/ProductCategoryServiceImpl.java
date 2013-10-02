package pointsmanager.service.impl;

import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pointsmanager.dao.CategoryDao;
import pointsmanager.entitystore.ProductCategoryEvent;
import pointsmanager.entitystore.ProductCategoryRowKey;
import pointsmanager.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private CategoryDao categoryDao;
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Override
	public void updateProductCategoryForMember(String memberId,
			String categoryId, Integer points, LocalDateTime date) {
		LocalDate localDate = LocalDate.parse(date.toString(DATE_FORMATTER));
		ProductCategoryRowKey rowKey = createCategoryRowKey(categoryId, localDate.toDate());
		ProductCategoryEvent event = createCategoryEvent(memberId, date.toDate());
		categoryDao.put(rowKey,  event, points);
	}
	
	private ProductCategoryEvent createCategoryEvent(String memberId, Date date) {
		ProductCategoryEvent categoryEvent = new ProductCategoryEvent();
		categoryEvent.setMemberId(memberId);
		categoryEvent.setDate(date);
		return categoryEvent;
		
	}
	
	private ProductCategoryRowKey createCategoryRowKey(String categoryId, Date date) {
		ProductCategoryRowKey categoryRowKey = new ProductCategoryRowKey();
		categoryRowKey.setCategoryId(categoryId);
		categoryRowKey.setDate(date);
		return categoryRowKey;
	}

}
