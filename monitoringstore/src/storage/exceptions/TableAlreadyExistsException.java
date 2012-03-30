package storage.exceptions;

public class TableAlreadyExistsException extends CreateTableException {

	private static final long serialVersionUID = -4046818659360386087L;
	
	private String tableName;
	
	public TableAlreadyExistsException(String tableName) {
		super("Table " + tableName + " already exists.");
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

}
