package pointsmanager.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pointsmanager.entitystore.Category;
import pointsmanager.entitystore.CategoryEvent;
import pointsmanager.entitystore.CategoryRowKey;
import pointsmanager.entitystore.MonthlyTransactionRowKey;
import pointsmanager.entitystore.TransactionEvent;
import pointsmanager.entitystore.TransactionRowKey;
import pointsmanager.entitystore.TransactionCategory.TransactionCategoryRowKey;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Cluster;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.ddl.ColumnFamilyDefinition;
import com.netflix.astyanax.ddl.KeyspaceDefinition;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Composite;
import com.netflix.astyanax.model.DynamicComposite;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.ComparatorType;
import com.netflix.astyanax.serializers.CompositeSerializer;
import com.netflix.astyanax.serializers.DateSerializer;
import com.netflix.astyanax.serializers.DynamicCompositeSerializer;
import com.netflix.astyanax.serializers.Int32Serializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.SingletonEmbeddedCassandra;

public class TransactionEntityManagerTest {

	private static Keyspace                  keyspace;
	private static AstyanaxContext<Keyspace> keyspaceContext;
	private static AstyanaxContext<Cluster> clusterContext;

	private static String TEST_CLUSTER_NAME  = "localhost";
	private static String TEST_KEYSPACE_NAME = "pointsbank";
	private static final String SEEDS = "localhost:9160";
	

	public static final ColumnFamily<TransactionRowKey, TransactionEvent> TRANSACTION_CF = ColumnFamily.newColumnFamily(
			"transaction", 
			TransactionRowKey.getTransactionrowkeyserializer(),
			TransactionEvent.getTransactioneventserializer(), Int32Serializer.get());

	public static final ColumnFamily<CategoryRowKey, CategoryEvent> CATEGORY_CF = ColumnFamily.newColumnFamily(
			"product_category", 
			CategoryRowKey.getCategoryrowkeyserializer(),
			CategoryEvent.getCategoryeventserializer(), Int32Serializer.get());

	public static final ColumnFamily<MonthlyTransactionRowKey, String> MONTHLY_TXN_CF = ColumnFamily.newColumnFamily(
			"monthly_txn", 
			MonthlyTransactionRowKey.getMonthlytxnrowkeyserializer(),
			StringSerializer.get(), Int32Serializer.get());
	
	private static final List<String> MEMBER_IDS = Arrays.asList("MEMBER-001", "MEMBER-002", "MEMBER-003", "MEMBER-004");
	
	private static final Random random = new Random();

	@BeforeClass
	public static void setup() throws Exception {
		Thread.sleep(1000 * 3);

		createKeySpace();
		
		keyspaceContext.start();

		Thread.sleep(1000 * 3);
		
	}

	@AfterClass
	public static void teardown() throws Exception {
		if (keyspaceContext != null)
			keyspaceContext.shutdown();

		Thread.sleep(1000 * 10);
	}
	
	@Test
	public void basicLifecycle() throws Exception {
		DateTimeFormatter dateFormater = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate localDate = LocalDate.parse("2013-01-01", dateFormater);
		LocalDateTime localDateTime = LocalDateTime.parse("2013-01-01 03:02:01", timeFormater);
		DateTime currentDateTime = DateTime.parse("2013-01-02 00:00:00", timeFormater);
		DateTimeFormatter monthAndYearFormater = DateTimeFormat.forPattern("MM-yyyy");
		String memberId = MEMBER_IDS.get(0);
		TransactionEvent origEntity = createTransactionEntity(localDate, memberId, localDateTime);

		MonthlyTransactionRowKey key = new MonthlyTransactionRowKey();
		key.setMemberId(memberId);
		key.setMonthAndYear(currentDateTime.toString(monthAndYearFormater));

		MutationBatch mutation = keyspace.prepareMutationBatch();


		TransactionRowKey rowKey = new TransactionRowKey();
		rowKey.setMemberId(memberId);
		rowKey.setDate(localDate.toDate());
		mutation.withRow(TRANSACTION_CF, rowKey).putColumn(origEntity, 30);
		CategoryRowKey categoryRowKey = new CategoryRowKey();
		categoryRowKey.setCategoryId(Category.AMBIENT_FOODS.name());
		categoryRowKey.setDate(localDate.toDate());
		CategoryEvent categoryEvent = new CategoryEvent();
		categoryEvent.setMemberId(memberId);
		categoryEvent.setDate(localDateTime.toDate());
		mutation.withRow(CATEGORY_CF, categoryRowKey).putColumn(categoryEvent, 30);
	    mutation.execute();

		MutationBatch mutation1 = keyspace.prepareMutationBatch();
	    
		DateTime todayAtMidnight = currentDateTime.withTimeAtStartOfDay();
		if (todayAtMidnight.equals(currentDateTime)) {
		    ColumnList<String> cl = keyspace.prepareQuery(MONTHLY_TXN_CF).getKey(key).execute().getResult();
		    System.out.println("Got column : " + ToStringBuilder.reflectionToString(cl));
		    Integer sum = calculatePointsForADay(rowKey);
		    if (cl.isEmpty()) {
		    	mutation1.withRow(MONTHLY_TXN_CF, key).incrementCounterColumn(memberId, sum);
		    } else {
		    	mutation1.withRow(MONTHLY_TXN_CF, key).incrementCounterColumn(memberId, sum);
		    }
		}
		
		mutation1.execute();

	    ColumnList<TransactionEvent> cl1 = keyspace.prepareQuery(TRANSACTION_CF).getKey(rowKey).execute().getResult();
	    
	    System.out.println("Got column : " + ToStringBuilder.reflectionToString(cl1));
		for (Column<TransactionEvent> c : cl1) {
			TransactionEvent event  = c.getName();
		    System.out.println("Got column : " + ToStringBuilder.reflectionToString(event));
		    System.out.println("Got value : " + c.getIntegerValue());
		}	    
		
	}

	private Integer calculatePointsForADay(TransactionRowKey rowKey) throws ConnectionException {
	    ColumnList<TransactionEvent> cl1 = keyspace.prepareQuery(TRANSACTION_CF).getKey(rowKey).execute().getResult();
	    int sum = 0;
		for (Column<TransactionEvent> c : cl1) {
			TransactionEvent event  = c.getName();
			sum += c.getIntegerValue();
		}
		return sum;
	}

	private TransactionEvent createTransactionEntity(LocalDate localDate, String memberId,
			LocalDateTime dateTime) {
		TransactionEvent transaction = new TransactionEvent();
		//transaction.setId(localDate.toDate());
		transaction.setMemberId(memberId);
		transaction.setTimeStamp(dateTime.toDate());
		//transaction.setValue(10);
		return transaction;
	}
	
	private static void createKeySpace() throws ConnectionException {
		clusterContext = new AstyanaxContext.Builder()
		.forCluster(TEST_CLUSTER_NAME)
		.forKeyspace(TEST_KEYSPACE_NAME)
		.withAstyanaxConfiguration(
				new AstyanaxConfigurationImpl()
				.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
				.setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE))
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl(TEST_CLUSTER_NAME
								+ "_" + TEST_KEYSPACE_NAME)
						.setSocketTimeout(30000)
						.setMaxTimeoutWhenExhausted(2000)
						.setMaxConnsPerHost(20)
						.setInitConnsPerHost(10)
						.setSeeds(SEEDS))
						.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
						.buildCluster(ThriftFamilyFactory.getInstance());

		clusterContext.start();

		//keyspace = keyspaceContext.getEntity();

		Cluster cluster = (Cluster) clusterContext.getClient();

		//try {
		//	cluster.dropKeyspace(TEST_KEYSPACE_NAME);
		//}
		//catch (Exception e) {
			//e.printStackTrace();
		//}

//        Map<String, String> stratOptions = new HashMap<String, String>();
//        stratOptions.put("replication_factor", "1");
//		
//		KeyspaceDefinition ksDef = cluster.makeKeyspaceDefinition();
//
//        ksDef.setName(TEST_KEYSPACE_NAME)
//                .setStrategyOptions(stratOptions)
//                .setStrategyClass("SimpleStrategy")
//                .addColumnFamily(cluster.makeColumnFamilyDefinition().setKeyspace(TEST_KEYSPACE_NAME)
//				.setName("transaction")
//				.setKeyValidationClass("CompositeType(UTF8Type, DateType)")
//				.setDefaultValidationClass(ComparatorType.INTEGERTYPE.getClassName()))
//                .addColumnFamily(cluster.makeColumnFamilyDefinition().setKeyspace(TEST_KEYSPACE_NAME)
//				.setName("monthly_txn")
//				.setKeyValidationClass("CompositeType(UTF8Type, UTF8Type)")
//				.setDefaultValidationClass(ComparatorType.COUNTERTYPE.getClassName())
//				.setComparatorType(ComparatorType.UTF8TYPE.getClassName()))				
//                .addColumnFamily(cluster.makeColumnFamilyDefinition().setKeyspace(TEST_KEYSPACE_NAME)
//				.setName("product_category")
//				.setKeyValidationClass("CompositeType(UTF8Type, DateType)")
//				.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName()));				;        
//        cluster.addKeyspace(ksDef);
//
        keyspaceContext = new AstyanaxContext.Builder()
        .forCluster(TEST_CLUSTER_NAME)
        .forKeyspace(TEST_KEYSPACE_NAME)
        .withAstyanaxConfiguration(
                new AstyanaxConfigurationImpl()
                        .setDiscoveryType(NodeDiscoveryType.NONE))
        .withConnectionPoolConfiguration(
                new ConnectionPoolConfigurationImpl(TEST_CLUSTER_NAME
                        + "_" + TEST_KEYSPACE_NAME)
                        .setSocketTimeout(30000)
                        .setMaxTimeoutWhenExhausted(2000)
                        .setMaxConnsPerHost(1).setSeeds(SEEDS))
        .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
        .buildKeyspace(ThriftFamilyFactory.getInstance());
        
        keyspaceContext.start();

        keyspace = keyspaceContext.getClient();
	}
	
}
