package storage;

import java.sql.Timestamp;
import java.util.List;

import storage.exceptions.InsertOutOfOrderException;
import storage.exceptions.KeyNotFoundException;
import storage.exceptions.TimestampNotFoundException;

public interface Table {

	public void insert(String key, String value, Timestamp timestamp) throws InsertOutOfOrderException ;
	
	public KeyValue get(String key, Timestamp timestamp) throws KeyNotFoundException, TimestampNotFoundException;

	public int getNumCol();
	
	public String getName();

	public List<KeyValue> scan(String key, Timestamp start, Timestamp end) throws KeyNotFoundException;
}
