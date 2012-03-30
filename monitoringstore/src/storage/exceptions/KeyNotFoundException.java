package storage.exceptions;

public class KeyNotFoundException extends StorageException {

	private static final long serialVersionUID = -1063175748246617228L;
	
	public String tableName, keyName;
	
	public KeyNotFoundException (String tableName, String keyName){
		super("Key " + keyName + " of table " + tableName + " not found.");
		this.tableName = tableName;
		this.keyName = keyName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getKeyName() {
		return keyName;
	}
	
}
