package storage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import storage.exceptions.KeyNotFoundException;
import storage.exceptions.TimestampNotFoundException;

public class TableImpl implements Table {

	private int numCol;
	private String name;
	
	private HashMap<String, ArrayList<KeyValue>> keyIndex;
	
	public TableImpl(String name, String schema){
		this.name = name;
		this.numCol = schema.split(",").length; // num of columns, comma separated
		this.keyIndex = new HashMap<String, ArrayList<KeyValue>>();
	}

	public int getNumCol() {
		return numCol;
	}

	public String getName() {
		return name;
	}

	@Override
	public void insert(String key, String value, Timestamp timestamp) {
		ArrayList<KeyValue> keyValues = keyIndex.get(key);
		
		if(keyValues == null){
			keyValues = new ArrayList<KeyValue>();
			keyIndex.put(key, keyValues);
		}
		
		keyValues.add(new KeyValue(value, timestamp));
	}

	@Override
	public KeyValue get(String key, Timestamp timestamp)
			throws KeyNotFoundException, TimestampNotFoundException {
		
		ArrayList<KeyValue> keyValues = keyIndex.get(key);
		
		if(keyValues == null){
			throw new KeyNotFoundException(name, key);
		}
		
		if(timestamp == null)
			return keyValues.get(keyValues.size()-1);
		else {
			KeyValue kv = search(keyValues, timestamp);
			
			if(kv == null)
				throw new TimestampNotFoundException(name, key, timestamp);
			else
				return kv;
		}
	}

	private KeyValue search(ArrayList<KeyValue> keyValues, Timestamp timestamp) {
		KeyValue current = null;
		for(int i = keyValues.size()-1; i >= 0; i--){
			current = keyValues.get(i);
			if(current.getTimestamp().before(timestamp))
				return current;
		}
		
		throw null;
	}
}
