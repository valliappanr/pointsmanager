package pointsmanager.context;

import com.netflix.astyanax.Keyspace;

public interface CFContext {

	public Keyspace getKeyspace();
	
	public void shutdown();	
}
