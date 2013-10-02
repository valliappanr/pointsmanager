package pointsmanager.service;

import org.joda.time.LocalDateTime;

public interface PointsManagerService {

	public void addPointsToMember(String memberId, Integer points, String productCategory);
	
	public void deductPointsToMember(String memberId, Integer points);
	
	public void aggregatePointsForToday();

	void addPointsToMember(String memberId, Integer points,
			String productCategory, LocalDateTime localDate);

	void deductPointsToMember(String memberId, Integer points,
			LocalDateTime localDate);
}
