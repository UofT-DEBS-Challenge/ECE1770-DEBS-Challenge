package storage.exceptions;

import java.sql.Timestamp;

public class InsertOutOfOrderException extends StorageException {

	private static final long serialVersionUID = -5071799442533720499L;
	
	public String tableName, keyName;
	public Timestamp timestamp, current;
	
	public InsertOutOfOrderException(String tableName, String keyName, Timestamp timestamp, Timestamp current){
		super("Tried to insert: [" + timestamp.toString() + "] for Key " + keyName + " of table " + tableName + ", where latest timestamp is: " + current.toString() + ".");
		this.tableName = tableName;
		this.keyName = keyName;
		this.timestamp = timestamp;
		this.current = current;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTableName() {
		return tableName;
	}

	public String getKeyName() {
		return keyName;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Timestamp getCurrent() {
		return current;
	}	
}
