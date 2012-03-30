package storage.exceptions;

public class CreateTableException extends StorageException {

	private static final long serialVersionUID = 6384614575708342826L;

	public CreateTableException(String error) {
		super(error);
	}
}
