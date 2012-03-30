package storage;

import java.sql.Timestamp;

public class KeyValue {

	private String value;
	private Timestamp timestamp;
	
	public KeyValue(String value, Timestamp timestamp){
		this.value = value;
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
