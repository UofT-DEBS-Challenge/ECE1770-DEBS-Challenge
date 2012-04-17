package storage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import storage.exceptions.InsertOutOfOrderException;
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

	@Override
	public int getNumCol() {
		return numCol;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void insert(String key, String value, Timestamp timestamp) throws InsertOutOfOrderException {
		ArrayList<KeyValue> keyValues = keyIndex.get(key);
		
		if(keyValues == null){
			keyValues = new ArrayList<KeyValue>();
			keyIndex.put(key, keyValues);
		} else {
			if(timestamp.compareTo(keyValues.get(keyValues.size()-1).getTimestamp()) < 0)
				throw new InsertOutOfOrderException(key, value, timestamp, keyValues.get(keyValues.size()-1).getTimestamp());
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

	@Override
	public List<KeyValue> scan(String key, Timestamp start, Timestamp end) throws KeyNotFoundException {
		
		ArrayList<KeyValue> keyValues = keyIndex.get(key);
		
		if(keyValues == null){
			throw new KeyNotFoundException(name, key);
		}
		
		return search(keyValues, start, end);		
	}
	
	private KeyValue search(ArrayList<KeyValue> keyValues, Timestamp timestamp) {
		KeyValue current = null;
		for(int i = keyValues.size()-1; i >= 0; i--){
			current = keyValues.get(i);
			if(current.getTimestamp().compareTo(timestamp) <= 0)
				return current;
		}
		
		return null;
	}

	private List<KeyValue> search(ArrayList<KeyValue> keyValues,
			Timestamp start, Timestamp end) {
		
		if(start == null){
			start = new Timestamp(0);
		}
		
		if(end == null){
			end = new Timestamp(Long.MAX_VALUE);
		}
		
		Vector<KeyValue> resultValues = new Vector<KeyValue>();
		
		KeyValue current = null;
		for(int i = keyValues.size()-1; i >= 0; i--){
			current = keyValues.get(i);
						
			if(current.getTimestamp().compareTo(start) < 0){
				break;
			}
			
			if(current.getTimestamp().compareTo(end) <= 0){
				resultValues.add(current);
			}
		}
		
		return resultValues;
	}

}