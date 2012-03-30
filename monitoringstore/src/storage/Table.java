package storage;

import java.sql.Timestamp;

import storage.exceptions.KeyNotFoundException;
import storage.exceptions.TimestampNotFoundException;

public interface Table {

	public void insert(String key, String value, Timestamp timestamp) ;

	public KeyValue get(String key, Timestamp timestamp) throws KeyNotFoundException, TimestampNotFoundException;

}
