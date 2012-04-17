package storage;

import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

public class KeyResult {

	private String key;
	private List<KeyValue> kvs;
	
	public KeyResult(String key, KeyValue kv) {
		this.key = key;
		this.kvs = new Vector<KeyValue>();
		this.kvs.add(kv);
	}

	public KeyResult(String key, List<KeyValue> kvs) {
		this.key = key;
		this.kvs = kvs;
	}

	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return kvs.get(0).getValue();
	}

	public Timestamp getTimestamp() {
		return kvs.get(0).getTimestamp();
	}
	
	public List<String> getValues(){
		List<String> values = new Vector<String>();
		
		for(KeyValue kv : kvs){
			values.add(kv.getValue());
		}
		
		return values;
	}
	
	public List<Timestamp> getTimestamps(){
		List<Timestamp> ts = new Vector<Timestamp>();
		
		for(KeyValue kv : kvs){
			ts.add(kv.getTimestamp());
		}
		
		return ts;
	}
}