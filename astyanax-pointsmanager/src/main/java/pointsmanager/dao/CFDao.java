package pointsmanager.dao;

import java.util.List;

public interface CFDao<K, C> {
	
	<V> void put(K key, C column, V value);
	
	 <V> List<V> get(K key, C column);

}
