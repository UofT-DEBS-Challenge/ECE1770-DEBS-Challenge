package storage.exceptions;

import java.sql.Timestamp;

public class TimestampNotFoundException extends StorageException {

	private static final long serialVersionUID = 8725110354913565262L;
	
	private String tableName, key;
	private Timestamp timestamp;
	
	public TimestampNotFoundException(String tableName, String key, Timestamp timestamp){
		super("Key " + key + " for table " + tableName + " does not have a value recorded before " + timestamp + ".");
		this.tableName = tableName;
		this.key = key;
		this.timestamp = timestamp;
	}

	public String getTableName() {
		return tableName;
	}

	public String getKey() {
		return key;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
