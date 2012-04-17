package storage;

import java.sql.Timestamp;
import java.util.List;

import storage.exceptions.CreateTableException;
import storage.exceptions.InsertOutOfOrderException;
import storage.exceptions.KeyNotFoundException;
import storage.exceptions.TableNotFoundException;
import storage.exceptions.TimestampNotFoundException;

public interface Storage {

	public void create(String tableName, String schema) throws CreateTableException;
	
	public void insert(String tableName, String key, String value)  throws TableNotFoundException, InsertOutOfOrderException;
	
	public void insert(String tableName, String key, String value, Timestamp timestamp)  throws TableNotFoundException, InsertOutOfOrderException;
	
	public KeyResult read(String tableName, String key, Timestamp timestamp)  throws TableNotFoundException, KeyNotFoundException, TimestampNotFoundException;
	
	public KeyResult scan(String tableName, String key, Timestamp start, Timestamp end) throws TableNotFoundException, KeyNotFoundException, TimestampNotFoundException;
	
	public Byte[] compress(String tableName)  throws TableNotFoundException;
	
}
