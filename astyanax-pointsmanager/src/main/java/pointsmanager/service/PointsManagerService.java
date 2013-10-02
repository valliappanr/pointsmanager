package pointsmanager.service;

public interface PointsManagerService {
	
	/**
	 * Add points to a member using current time.
	 * 
	 * @param memberId - memberId for which points to be added
	 * @param points - points to add
	 * @param productCategory - product category for which the points need to be added.
	 */
	void addPointsToMember(String memberId, Integer points, String productCategory);
	

	/**
	 * Deduct points to a member using current time.
	 * 
	 * @param memberId - memberId for which points to be deducted
	 * @param points - points to deducted
	 */
	void deductPointsToMember(String memberId, Integer points);

	/**
	 * Service method to calculate the aggregated points for today.
	 */
	void aggregatePointsForToday();
}
