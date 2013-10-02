package pointsmanager.service.impl;

import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pointsmanager.dao.TransactionDao;
import pointsmanager.entitystore.TransactionEvent;
import pointsmanager.entitystore.TransactionRowKey;
import pointsmanager.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionDao transactionDao;
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Override
	public void addTransactionForMember(String memberId, Integer points, LocalDateTime date) {
		LocalDate localDate = LocalDate.parse(date.toString(DATE_FORMATTER));
		TransactionRowKey rowKey = createTransactionRowKey(memberId, localDate.toDate());
		TransactionEvent origEntity = createTransactionEntity(localDate, memberId, date);
		transactionDao.put(rowKey, origEntity, points);
	}
	
	@Override
	public void deductPoints(String memberId, Integer points, LocalDateTime date) {
		LocalDate localDate = LocalDate.parse(date.toString(DATE_FORMATTER));
		TransactionRowKey rowKey = createTransactionRowKey(memberId, localDate.toDate());
		TransactionEvent origEntity = createTransactionEntity(localDate, memberId, date);
		transactionDao.put(rowKey, origEntity, -points);
	}	

	private TransactionRowKey createTransactionRowKey(String memberId, Date date) {
		TransactionRowKey rowKey = new TransactionRowKey();
		rowKey.setMemberId(memberId);
		rowKey.setDate(date);
		return rowKey;
	}
	
	private TransactionEvent createTransactionEntity(LocalDate localDate, String memberId,
			LocalDateTime dateTime) {
		TransactionEvent transactionEvent = new TransactionEvent();
		transactionEvent.setMemberId(memberId);
		transactionEvent.setTimeStamp(dateTime.toDate());
		return transactionEvent;
	}

}
