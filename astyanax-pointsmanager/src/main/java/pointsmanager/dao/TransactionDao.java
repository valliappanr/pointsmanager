package pointsmanager.dao;

import pointsmanager.entitystore.TransactionEvent;
import pointsmanager.entitystore.TransactionRowKey;

public interface TransactionDao extends CFDao<TransactionRowKey, TransactionEvent> {

}
