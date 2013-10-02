package pointsmanager.service.impl;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pointsmanager.service.PointsManagerService;
import pointsmanager.service.ProductCategoryService;
import pointsmanager.service.TransactionAggregatorService;
import pointsmanager.service.TransactionService;

@Service
public class PointsManagerServiceImpl implements PointsManagerService {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ProductCategoryService categoryService;

	@Autowired
	private TransactionAggregatorService aggregatorService;
	
	public TransactionService getTransactionService() {
		return transactionService;
	}

	
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	public ProductCategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(ProductCategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public void addPointsToMember(String memberId, Integer points, String productCategory,
			LocalDateTime localDate) {
		transactionService.addTransactionForMember(memberId, points, localDate);
		categoryService.updateProductCategoryForMember(memberId, productCategory, points, localDate);
	}
	
	@Override
	public void addPointsToMember(String memberId, Integer points, String productCategory) {
		addPointsToMember(memberId, points, productCategory, LocalDateTime.now());
	}


	@Override
	public void deductPointsToMember(String memberId, Integer points, LocalDateTime localDate) {
		Integer amount = aggregatorService.findAmountForMember(memberId, localDate);
		if (amount > points) {
			transactionService.deductPoints(memberId, points, localDate);
		}
	}
	
	
	@Override
	public void deductPointsToMember(String memberId, Integer points) {
		deductPointsToMember(memberId, points, LocalDateTime.now());
	}

	@Override
	public void aggregatePointsForToday() {
		
	}

}
