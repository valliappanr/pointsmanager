package pointsmanager.context.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pointsmanager.context.CFContext;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

@Component
public class CFContextImpl implements CFContext, InitializingBean {

	private Keyspace                  keyspace;
	private AstyanaxContext<Keyspace> keyspaceContext;
	
	@Value("${cluster.name}")
	private String clusterName;
	
	@Value("${keyspace.name}")	
	private String keySpaceName;
	
	@Value("${seeds.name}")	
	private String seeds;
	
	public void afterPropertiesSet() {
		createAndStartKeySpace();
	}
	
	private void createAndStartKeySpace() {
		String connectionConfigName = new StringBuilder().append(clusterName)
				.append(" ").append(keySpaceName).toString();
        keyspaceContext = new AstyanaxContext.Builder()
        .forCluster(clusterName)
        .forKeyspace(keySpaceName)
        .withAstyanaxConfiguration(
                new AstyanaxConfigurationImpl()
                        .setDiscoveryType(NodeDiscoveryType.NONE))
        .withConnectionPoolConfiguration(
                new ConnectionPoolConfigurationImpl(connectionConfigName)
                        .setSocketTimeout(30000)
                        .setMaxTimeoutWhenExhausted(2000)
                        .setMaxConnsPerHost(1).setSeeds(seeds))
        .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
        .buildKeyspace(ThriftFamilyFactory.getInstance());
        
        keyspaceContext.start();

        keyspace = keyspaceContext.getClient();



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
	}

	public Keyspace getKeyspace() {
		return keyspace;
	}

	public void shutdown() {
		keyspaceContext.shutdown();
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getKeySpaceName() {
		return keySpaceName;
	}

	public void setKeySpaceName(String keySpaceName) {
		this.keySpaceName = keySpaceName;
	}

	public String getSeeds() {
		return seeds;
	}

	public void setSeeds(String seeds) {
		this.seeds = seeds;
	}
}
