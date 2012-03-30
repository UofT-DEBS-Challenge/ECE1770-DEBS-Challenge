package storage.exceptions;

public class StorageException extends Exception {

	private static final long serialVersionUID = -202177386615319860L;
	
	public StorageException(String error){
		super(error);
	}

}
