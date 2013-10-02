package pointsmanager.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import pointsmanager.dao.MonthlyTransactionDao;
import pointsmanager.dao.TransactionDao;
import pointsmanager.entitystore.MonthlyTransactionRowKey;
import pointsmanager.entitystore.TransactionRowKey;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TransactionAggregatorServiceImplTest {

	private TransactionAggregatorServiceImpl transactionAggregatorService = 
			new TransactionAggregatorServiceImpl();
	@Mock
	private TransactionDao transactionDao;
	
	@Mock
	private MonthlyTransactionDao monthlyTransactionDao;
	
	
	@Before
	public void setup() {
		transactionAggregatorService.setMonthlyTransactionDao(monthlyTransactionDao);
		transactionAggregatorService.setTransactionDao(transactionDao);
	}

	@Test
	public void shouldReturnZeroForNonMatchingMonthlyTransactionForMember() {
		final List<Integer> results = new ArrayList<Integer>();
		//given valid memberId and date
		String memberId = "member-001";
		LocalDateTime date = LocalDateTime.now();
		when(monthlyTransactionDao.get(any(MonthlyTransactionRowKey.class))).thenAnswer(generateAnswers(results));
		//when asked to calculate the points before yesterday
		Integer points = transactionAggregatorService.getPointsTillDayBefore(memberId, date);
		// then I should get zero
		assertTrue(points == 0);
	}

	@Test
	public void shouldReturnCorrectValueForMatchingMonthlyTransactionForMember() {
		final List<Integer> results = new ArrayList<Integer>();
		results.add(10);
		results.add(20);
		//given valid memberId and date
		String memberId = "member-001";
		LocalDateTime date = LocalDateTime.now();
		when(monthlyTransactionDao.get(any(MonthlyTransactionRowKey.class))).thenAnswer(generateAnswers(results));
		//when asked to calculate the points before yesterday
		Integer points = transactionAggregatorService.getPointsTillDayBefore(memberId, date);
		// then I should get 30
		assertTrue(points == 30);
	}

	@Test
	public void shouldReturnZeroForNonMatchingDailyTransactionForMember() {
		final List<Integer> results = new ArrayList<Integer>();
		//given valid memberId and date
		String memberId = "member-001";
		LocalDateTime date = LocalDateTime.now();
		when(transactionDao.get(any(TransactionRowKey.class))).thenAnswer(generateAnswers(results));
		//when asked to calculate the points for current date
		Integer points = transactionAggregatorService.getPointsFortheDay(memberId, date);
		// then I should get 0
		assertTrue(points == 0);
	}

	@Test
	public void shouldReturnCorrectValueForMatchingDailyTransactionForMember() {
		final List<Integer> results = new ArrayList<Integer>();
		results.add(10);
		results.add(20);
		//given valid memberId and date
		String memberId = "member-001";
		LocalDateTime date = LocalDateTime.now();
		when(transactionDao.get(any(TransactionRowKey.class))).thenAnswer(generateAnswers(results));
		//when asked to calculate the points current date
		Integer points = transactionAggregatorService.getPointsFortheDay(memberId, date);
		// then I should get 30
		assertTrue(points == 30);
	}
	
	
	
	private Answer<List<Integer>> generateAnswers(final List<Integer> results) {
		Answer<List<Integer>> answer = new Answer<List<Integer>>() {

			@Override
			public List<Integer> answer(InvocationOnMock invocation)
					throws Throwable {
				// TODO Auto-generated method stub
				return results;
			}
		};
		return answer;
		
	}
}
