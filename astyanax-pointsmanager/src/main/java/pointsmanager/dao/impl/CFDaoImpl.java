package pointsmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import pointsmanager.context.CFContext;
import pointsmanager.dao.CFDao;

import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.Serializer;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;


/**
 * Base class DAO implementation to access the column family.
 * 
 */
public class CFDaoImpl<K, C> implements CFDao<K, C> {

	@Autowired
	private CFContext cfContext;
	
	private ColumnFamily<K, C> comlumFamily;
	
	private Serializer<?> valueSerializer;
	

	public CFDaoImpl(String columnFamilyName, Serializer<K> rowSerializer, 
			Serializer<C> columnSerializer, Serializer<?> valueSerializer) {
		comlumFamily = ColumnFamily.newColumnFamily(
				columnFamilyName, 
				rowSerializer,
				columnSerializer);
		this.valueSerializer = valueSerializer;
	}

	public ColumnFamily<K, C> getComlumFamily() {
		return comlumFamily;
	}

	public void setComlumFamily(ColumnFamily<K, C> comlumFamily) {
		this.comlumFamily = comlumFamily;
	}

	public Serializer<?> getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(Serializer<?> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}
	

	/**
	 * Puts an entity into the repository
	 *  
	 * @param key - row key of the entity
	 * @param column - Column of the entity
	 * @param value - value of the entity
	 */
	@Override
	public <V> void put(K key, C column, V value) {
		MutationBatch mutation = cfContext.getKeyspace().prepareMutationBatch();
		String valueSerializerClassName = valueSerializer.getClass().getName();
		if (valueSerializerClassName.equals("com.netflix.astyanax.serializers.IntegerSerializer")) {
			mutation.withRow(comlumFamily, key).putColumn(column, (Integer)value);
		} else if (valueSerializerClassName.equals("com.netflix.astyanax.serializers.StringSerializer")) {
			mutation.withRow(comlumFamily, key).putColumn(column, (String)value);
		} else {
			throw new RuntimeException("Only String / Integer type supported");
		}
		try {
			mutation.execute();
		} catch (ConnectionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrives the column values of a matching row key
	 *  
	 * @param key - row key of the entity
	 * @return value list- list of values of matching row key.
	 */
	@Override
	public <V> List<V> get(K key) {
		List<V> resultList = new ArrayList<V>();
		try {
			ColumnList<C> cl1 = cfContext.getKeyspace().prepareQuery(comlumFamily).getKey(key).execute().getResult();
			for (Column<C> c : cl1) {
				@SuppressWarnings("unchecked")
				V value = (V) c.getValue(valueSerializer);
				resultList.add(value);
			}
		} catch (ConnectionException e) {
			throw new RuntimeException(e);
		}
		return resultList;
	}

}
