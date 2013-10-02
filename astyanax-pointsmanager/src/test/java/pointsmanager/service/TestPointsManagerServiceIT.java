package pointsmanager.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pointsmanager.entitystore.ProductCategoryEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class TestPointsManagerServiceIT {
	
	private static final String DEFAULT_MEMBER_ID = "MEMBER-001";
	@Autowired
	PointsManagerService pointsManagerService;

	@Test
	public void testAddPoints() {
		Integer pointsBeforeAdding = pointsManagerService.getPointsForMember(DEFAULT_MEMBER_ID);
		pointsManagerService.addPointsToMember(DEFAULT_MEMBER_ID, 10, ProductCategoryEnum.AMBIENT_FOODS.name());
		Integer pointsAfterAdding = pointsManagerService.getPointsForMember(DEFAULT_MEMBER_ID);
		assertTrue((pointsAfterAdding - pointsBeforeAdding) == 10);
	}

	@Test
	public void testDeductPoints() {
		Integer pointsBeforeAdding = pointsManagerService.getPointsForMember(DEFAULT_MEMBER_ID);
		pointsManagerService.deductPointsToMember("MEMBER-001", 5);
		Integer pointsAfterDetucting = pointsManagerService.getPointsForMember(DEFAULT_MEMBER_ID);
		assertTrue((pointsBeforeAdding - pointsAfterDetucting) == 5);
	}
	
}
