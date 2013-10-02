package pointsmanager.service.impl;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import pointsmanager.entitystore.ProductCategoryEnum;
import pointsmanager.service.PointsManagerService;
import pointsmanager.service.ProductCategoryService;
import pointsmanager.service.TransactionAggregatorService;
import pointsmanager.service.TransactionService;

@Service
public class PointsManagerServiceImpl implements PointsManagerService {

	private TransactionService transactionService;

	private ProductCategoryService categoryService;

	private TransactionAggregatorService aggregatorService;
	
	public TransactionService getTransactionService() {
		return transactionService;
	}

	
	@Autowired
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	public ProductCategoryService getCategoryService() {
		return categoryService;
	}

	@Autowired
	public void setCategoryService(ProductCategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Autowired
	public void setTransactionAggregatorService(
			TransactionAggregatorService aggregatorService) {
		this.aggregatorService = aggregatorService;
	}
	
	
	@Override
	public void addPointsToMember(String memberId, Integer points, String productCategory) {
		addPointsToMember(memberId, points, productCategory, LocalDateTime.now());
	}

	@Override
	public Integer getPointsForMember(String memberId) {
		return aggregatorService.calculateAggregatedPointsUptoDate(memberId, LocalDateTime.now());
	}
	

	@Override
	public void deductPointsToMember(String memberId, Integer points) {
		deductPointsToMember(memberId, points, LocalDateTime.now());
	}

	@Override
	public void aggregatePointsForToday() {
		throw new RuntimeException("Not supported yet");
	}
	
	void addPointsToMember(String memberId, Integer points, String productCategory,
			LocalDateTime localDate) {
		validateInputForPointsManagement(memberId, points);
		Assert.isTrue(isProductCategoryExists(productCategory), 
				String.format("Product category %s not found", productCategory));
		
		transactionService.addTransactionForMember(memberId, points, localDate);
		categoryService.updateProductCategoryForMember(memberId, productCategory, points, localDate);
	}
	
	void deductPointsToMember(String memberId, Integer points, LocalDateTime localDate) {
		validateInputForPointsManagement(memberId, points);
		Integer amount = aggregatorService.calculateAggregatedPointsUptoDate(memberId, localDate);
		if (amount > points) {
			transactionService.deductPoints(memberId, points, localDate);
		}
	}
	
	
	void validateInputForPointsManagement(String memberId, Integer points) {
		validateMemberIdForPointsManagement(memberId);
		validatePointsForPointsManagement(points);
	}
	
	void validateMemberIdForPointsManagement(String memberId) {
		Assert.notNull(memberId, "memberId cannot be null");
	}
	
	void validatePointsForPointsManagement(Integer points) {
		Assert.isTrue(points > 0, String.format("points %s cannot be negative/zero", points));
	}
	
	boolean isProductCategoryExists(String productCategory) {
		if (!ProductCategoryEnum.isProductCategoryFound(productCategory)) {
			return false;
		}
		return true;
	}


}
