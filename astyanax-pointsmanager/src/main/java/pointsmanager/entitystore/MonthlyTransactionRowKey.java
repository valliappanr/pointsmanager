package pointsmanager.entitystore;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

/**
 * Pojo Mapping Row Key of Monthly Transaction column family.
 * 
 * Row key is mapped to memberId : month-year
 * 
 */
public class MonthlyTransactionRowKey {

	@Component(ordinal=0) private String memberId;
	
	@Component(ordinal=1) private String monthAndYear;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMonthAndYear() {
		return monthAndYear;
	}

	public void setMonthAndYear(String monthAndYear) {
		this.monthAndYear = monthAndYear;
	}

	
	public static AnnotatedCompositeSerializer<MonthlyTransactionRowKey> getMonthlytxnrowkeyserializer() {
		return monthlyTxnRowKeySerializer;
	}


	private static final AnnotatedCompositeSerializer<MonthlyTransactionRowKey> monthlyTxnRowKeySerializer = 
			new AnnotatedCompositeSerializer<MonthlyTransactionRowKey>(MonthlyTransactionRowKey.class);	
	
	
}
