package pointsmanager.service;

import org.joda.time.LocalDateTime;

public interface TransactionAggregatorService {

	Integer calculateAggregatedPointsUptoDate(String memberId, LocalDateTime localDate);

}
