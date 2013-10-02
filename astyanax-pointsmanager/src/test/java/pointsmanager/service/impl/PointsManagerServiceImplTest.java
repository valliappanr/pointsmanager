package pointsmanager.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import pointsmanager.entitystore.ProductCategoryEnum;
import pointsmanager.service.ProductCategoryService;
import pointsmanager.service.TransactionAggregatorService;
import pointsmanager.service.TransactionService;

@RunWith(MockitoJUnitRunner.class)
public class PointsManagerServiceImplTest {
	
	PointsManagerServiceImpl pointsManagerServiceImpl;
	
	@Mock 
	TransactionService transactionService;

	@Mock 
	private ProductCategoryService categoryService;

	@Mock 
	private TransactionAggregatorService aggregatorService;
	

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		pointsManagerServiceImpl = new PointsManagerServiceImpl();
		pointsManagerServiceImpl.setCategoryService(categoryService);
		pointsManagerServiceImpl.setTransactionService(transactionService);
		pointsManagerServiceImpl.setTransactionAggregatorService(aggregatorService);
	}
	
	@Test
	public void shouldNotFlagAsExistsForNonExistingProductCategory() {
		//given non existing product category
		String nonExistingProductCategory = "TEST";
		//when asked to check whether the product category exists
		boolean productExists = pointsManagerServiceImpl.isProductCategoryExists(nonExistingProductCategory);
		//then I should get false
		assertFalse(productExists);
	}

	@Test
	public void shouldFlagAsExistsForExistingProductCategory() {
		//given existing product category
		String existingProductCategory = ProductCategoryEnum.AMBIENT_FOODS.name();
		//when asked to check whether the product category exists
		boolean productExists = pointsManagerServiceImpl.isProductCategoryExists(existingProductCategory);
		//then I should get true
		assertTrue(productExists);
	}

	@Test
	public void shouldThrowExceptionForAddingNegativePoints() {
		//given negative value for points
		Integer pointsToAdd = -10;

		//then I should get IllegalArgumentException
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("points -10 cannot be negative/zero");
		
		//when asked to check whether the product category exists
		pointsManagerServiceImpl.validatePointsForPointsManagement(pointsToAdd);
		
	}
	
	@Test
	public void shouldThrowExceptionForAddingZeroPoints() {
		//given zero value for points
		Integer pointsToAdd = 0;

		//then I should get IllegalArgumentException
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("points 0 cannot be negative/zero");
		
		//when asked to check whether the product category exists
		pointsManagerServiceImpl.validatePointsForPointsManagement(pointsToAdd);
		
	}

	@Test
	public void shouldThrowExceptionForMemberIdWithNullInput() {
		//given null input for memberId
		String memberId = null;

		//then I should get IllegalArgumentException
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("memberId cannot be null");
		
		//when asked to check whether the product category exists
		pointsManagerServiceImpl.validateMemberIdForPointsManagement(memberId);
		
	}
	
	@Test
	public void shouldCallServiceCorrectlyForAddingValidPoints() {
		//given valid input for adding points
		String memberId = "member-001";
		Integer points = 10;
		String productCategory = ProductCategoryEnum.AMBIENT_FOODS.name();
		
		//when asked to add points for a member
		pointsManagerServiceImpl.addPointsToMember(memberId, points, productCategory);
		
		//then repository call needs to be made to add points;
		verify(transactionService).addTransactionForMember(any(String.class), any(Integer.class), any(LocalDateTime.class));
	}
}
