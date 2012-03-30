package storage;

import java.sql.Timestamp;

public class KeyResult {

	private String key;
	private KeyValue kv;
	
	public KeyResult(String key, KeyValue kv) {
		this.key = key;
		this.kv = kv;
	}

	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return kv.getValue();
	}

	public Timestamp getTimestamp() {
		return kv.getTimestamp();
	}
}
