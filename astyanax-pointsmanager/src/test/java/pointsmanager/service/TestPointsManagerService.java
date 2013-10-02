package pointsmanager.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pointsmanager.entitystore.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class TestPointsManagerService {
	
	@Autowired
	PointsManagerService pointsManagerService;

	@Test
	public void testAddPoints() {
		pointsManagerService.addPointsToMember("MEMBER-001", 10, Category.AMBIENT_FOODS.name());
	}

	@Test
	public void testDeductPoints() {
		pointsManagerService.deductPointsToMember("MEMBER-001", 5);
	}
	
}
