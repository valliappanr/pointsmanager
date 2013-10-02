package pointsmanager.service;

import org.joda.time.LocalDateTime;

public interface ProductCategoryService {
	
	void updateProductCategoryForMember(String memberId, String categoryId, Integer points, LocalDateTime localDate);

}
