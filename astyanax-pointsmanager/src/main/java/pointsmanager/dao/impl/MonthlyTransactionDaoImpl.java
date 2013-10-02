package pointsmanager.dao.impl;

import org.springframework.stereotype.Repository;

import com.netflix.astyanax.serializers.Int32Serializer;
import com.netflix.astyanax.serializers.StringSerializer;

import pointsmanager.dao.MonthlyTransactionDao;
import pointsmanager.entitystore.MonthlyTransactionRowKey;

@Repository
public class MonthlyTransactionDaoImpl extends CFDaoImpl<MonthlyTransactionRowKey, String> 
		implements MonthlyTransactionDao {

	private static final String MONTHLY_TXT_COLUMN_FAMILY_NAME = "monthly_txn";
	
	public MonthlyTransactionDaoImpl() {
		super(MONTHLY_TXT_COLUMN_FAMILY_NAME, MonthlyTransactionRowKey.getMonthlytxnrowkeyserializer(), 
				StringSerializer.get(), Int32Serializer.get());
	}

}
