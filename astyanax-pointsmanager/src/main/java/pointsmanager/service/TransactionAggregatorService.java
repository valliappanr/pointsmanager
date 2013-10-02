package pointsmanager.service;

import org.joda.time.LocalDateTime;

public interface TransactionAggregatorService {

	Integer findAmountForMember(String memberId, LocalDateTime localDate);

}
