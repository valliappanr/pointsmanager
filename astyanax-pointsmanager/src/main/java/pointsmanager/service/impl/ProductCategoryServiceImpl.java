package pointsmanager.service.impl;

import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pointsmanager.dao.CategoryDao;
import pointsmanager.entitystore.CategoryEvent;
import pointsmanager.entitystore.CategoryRowKey;
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
		CategoryRowKey rowKey = createCategoryRowKey(categoryId, localDate.toDate());
		CategoryEvent event = createCategoryEvent(memberId, date.toDate());
		categoryDao.put(rowKey,  event, points);
	}
	
	private CategoryEvent createCategoryEvent(String memberId, Date date) {
		CategoryEvent categoryEvent = new CategoryEvent();
		categoryEvent.setMemberId(memberId);
		categoryEvent.setDate(date);
		return categoryEvent;
		
	}
	
	private CategoryRowKey createCategoryRowKey(String categoryId, Date date) {
		CategoryRowKey categoryRowKey = new CategoryRowKey();
		categoryRowKey.setCategoryId(categoryId);
		categoryRowKey.setDate(date);
		return categoryRowKey;
	}

}
