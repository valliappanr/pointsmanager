package pointsmanager.dao.impl;

import org.springframework.stereotype.Repository;

import com.netflix.astyanax.serializers.Int32Serializer;

import pointsmanager.dao.TransactionDao;
import pointsmanager.entitystore.TransactionEvent;
import pointsmanager.entitystore.TransactionRowKey;

@Repository
public class TransactionDaoImpl extends CFDaoImpl<TransactionRowKey, TransactionEvent> implements TransactionDao {

	private static final String TRANSACTION_COLUMN_FAMILY_NAME = "transaction";
	
	public TransactionDaoImpl() {
		super(TRANSACTION_COLUMN_FAMILY_NAME, TransactionRowKey.getTransactionrowkeyserializer(), 
				TransactionEvent.getTransactioneventserializer(), Int32Serializer.get());
	}

}
