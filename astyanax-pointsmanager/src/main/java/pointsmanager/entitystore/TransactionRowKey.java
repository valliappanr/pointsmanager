package pointsmanager.entitystore;

import java.util.Date;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

public class TransactionRowKey  {
	
	@Component(ordinal=0) private String memberId;
	
	@Component(ordinal=1) private Date date;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	public static AnnotatedCompositeSerializer<TransactionRowKey> getTransactionrowkeyserializer() {
		return transactionRowKeySerializer;
	}


	private static final AnnotatedCompositeSerializer<TransactionRowKey> transactionRowKeySerializer = 
			new AnnotatedCompositeSerializer<TransactionRowKey>(TransactionRowKey.class);	

}
