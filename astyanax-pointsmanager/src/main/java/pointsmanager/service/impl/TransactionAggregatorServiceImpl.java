package pointsmanager.service.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pointsmanager.dao.MonthlyTransactionDao;
import pointsmanager.dao.TransactionDao;
import pointsmanager.entitystore.MonthlyTransactionRowKey;
import pointsmanager.entitystore.TransactionEvent;
import pointsmanager.entitystore.TransactionRowKey;
import pointsmanager.service.TransactionAggregatorService;

@Service
public class TransactionAggregatorServiceImpl implements TransactionAggregatorService{

	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private MonthlyTransactionDao monthlyTransactionDao;
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormat.forPattern("MM-yyyy");
	
	@Override
	public Integer findAmountForMember(String memberId, LocalDateTime date) {
		LocalDate localDate = LocalDate.parse(date.toString(DATE_FORMATTER));
		TransactionRowKey rowKey = createTransactionRowKey(memberId, localDate.toDate());
		TransactionEvent origEntity = createTransactionEntity(localDate, memberId, date);
		
		MonthlyTransactionRowKey key = new MonthlyTransactionRowKey();
		key.setMemberId(memberId);
		key.setMonthAndYear(date.toString(MONTH_YEAR_FORMATTER));

	    List<Integer> monthlyTransactionValues = monthlyTransactionDao.get(key, memberId);
	    int monthlyTotal = 0;
		for (Integer value : monthlyTransactionValues) {
			monthlyTotal += value;
		}
		int dayTotal = 0;
		List<Integer> currentDayTransactionValue = transactionDao.get(rowKey, origEntity);
		for (Integer value : currentDayTransactionValue) {
			dayTotal += value;
		}
		return monthlyTotal + dayTotal;
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
