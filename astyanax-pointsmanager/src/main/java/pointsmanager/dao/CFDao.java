package pointsmanager.dao;

import java.util.List;

/**
 * Interface definition for column family of type K - row key
 *  C - Column family
 * 
 */

public interface CFDao<K, C> {
	

	/**
	 * Puts an entity into the repository
	 *  
	 * @param key - row key of the entity
	 * @param column - Column of the entity
	 * @param value - value of the entity
	 */
	<V> void put(K key, C column, V value);

	/**
	 * Retrives the column values of a matching row key
	 *  
	 * @param key - row key of the entity
	 * @return value list- list of values of matching row key.
	 */
	 <V> List<V> get(K key);
}
