package storage.exceptions;

public class TableNotFoundException extends StorageException {

	private static final long serialVersionUID = -1673739468572965998L;
	
	private String tableName;
	
	public TableNotFoundException(String tableName){
		super("Table " + tableName + " not found.");
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

}
