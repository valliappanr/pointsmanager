package pointsmanager.service;


import org.joda.time.LocalDateTime;

public interface TransactionService {
	
	void addTransactionForMember(String memberId, Integer points, LocalDateTime localDate);

	void deductPoints(String memberId, Integer points, LocalDateTime localDate);

}
