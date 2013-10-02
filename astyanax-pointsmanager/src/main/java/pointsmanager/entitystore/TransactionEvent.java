package pointsmanager.entitystore;


import java.util.Date;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

/**
 * Pojo Mapping Composite column name mapping for transaction column family.
 * 
 * Row key is mapped to memberId : timestamp
 * 
 */
public class TransactionEvent {
	
	@Component(ordinal=0) String memberId;       // This will be the first part of the composite
	@Component(ordinal=1) Date   timeStamp;       // This will be the second part of the composite

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public static AnnotatedCompositeSerializer<TransactionEvent> getTransactioneventserializer() {
		return transactionEventSerializer;
	}

	private static final AnnotatedCompositeSerializer<TransactionEvent> transactionEventSerializer = 
			new AnnotatedCompositeSerializer<TransactionEvent>(TransactionEvent.class);	
	
}
