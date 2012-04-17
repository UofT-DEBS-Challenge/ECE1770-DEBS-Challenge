package storage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import storage.exceptions.CreateTableException;
import storage.exceptions.InsertOutOfOrderException;
import storage.exceptions.KeyNotFoundException;
import storage.exceptions.TableAlreadyExistsException;
import storage.exceptions.TableNotFoundException;
import storage.exceptions.TimestampNotFoundException;

public class StorageImpl implements Storage {

	private HashMap<String, Table> tableIndex;
	
	public StorageImpl(){
		this.tableIndex = new HashMap<String, Table>();
	}
	
	@Override
	public void create(String tableName, String schema)
			throws CreateTableException {
		
		if(tableIndex.containsKey(tableName))
			throw new TableAlreadyExistsException(tableName);
		else
			tableIndex.put(tableName, new TableImpl(tableName, schema));		
	}

	@Override
	public void insert(String tableName, String key, String value) throws TableNotFoundException, InsertOutOfOrderException {
		insert(tableName, key, value, new Timestamp(new Date().getTime()));
	}

	@Override
	public KeyResult read(String tableName, String key, Timestamp timestamp) throws TableNotFoundException, KeyNotFoundException, TimestampNotFoundException {
		if(!tableIndex.containsKey(tableName))
			throw new TableNotFoundException(tableName);		
		
		KeyValue value = tableIndex.get(tableName).get(key, timestamp);
				
		return new KeyResult(key, value);
	}

	@Override
	public Byte[] compress(String tableName) throws TableNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyResult scan(String tableName, String key, Timestamp start,
			Timestamp end) throws TableNotFoundException, KeyNotFoundException {
		
		if(!tableIndex.containsKey(tableName))
			throw new TableNotFoundException(tableName);		
		
		List<KeyValue> values = tableIndex.get(tableName).scan(key, start, end);
		
		return new KeyResult(key, values);
	}

	@Override
	public void insert(String tableName, String key, String value,
			Timestamp timestamp) throws TableNotFoundException,
			InsertOutOfOrderException {
		
		if(!tableIndex.containsKey(tableName))
			throw new TableNotFoundException(tableName);
		
		tableIndex.get(tableName).insert(key, value, timestamp);	
	}

}
