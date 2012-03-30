package storage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import storage.Table;
import storage.TableImpl;
import storage.exceptions.KeyNotFoundException;
import storage.exceptions.TimestampNotFoundException;

public class TableTest {

	private Table table;
	
	@Before
	public void setUp(){
		table = new TableImpl("Test", "a,b");
	}
	
	@After
	public void tearDown(){
		table = null;
	}
	
	@Test
	public void testSchema() {
		Table table = new TableImpl("test", "a,b");
		assertEquals("Table schema a,b should have 2 columns", 2, table.getNumCol());
		table = new TableImpl("test", "a,b,");
		assertEquals("Table schema a,b, should have 2 columns", 2, table.getNumCol());
		table = new TableImpl("test", "asd a,b");
		assertEquals("Table schema asd a,b should have 2 columns", 2, table.getNumCol());
		table = new TableImpl("test", "a,,");
		assertEquals("Table schema a,, should have 1 column", 1, table.getNumCol());
	}
	
	@Test
	public void testName(){
		assertEquals("Table name should be Test", "Test", table.getName());
	}
	
	@Test
	public void testSimpleInsertAndGet(){
		table.insert("key1", "v1,v2", new Timestamp(new Date().getTime()));
		
		try {
			assertEquals("key1 value should be", "v1,v2", table.get("key1", null).getValue());
		} catch (KeyNotFoundException e) {
			fail("key1 should be found");
		} catch (TimestampNotFoundException e) {
			fail("null timestamp should return latest value");
		}
	}

	@Test
	public void testNullGet(){
		table.insert("key1", "v1,v2", new Timestamp(new Date().getTime()));
				
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {	}
		
		table.insert("key1", "v2,v3", new Timestamp(new Date().getTime()));
		
		try {
			assertEquals("key1 value should be", "v2,v3", table.get("key1", null).getValue());
		} catch (KeyNotFoundException e) {
			fail("key1 should be found");
		} catch (TimestampNotFoundException e) {
			fail("null timestamp should return latest value");
		}
	}
	
	@Test
	public void testTimestamp(){
		Timestamp ts1 = new Timestamp(new Date().getTime());
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {	}
		
		Timestamp ts1b = new Timestamp(new Date().getTime());
		
		table.insert("key1", "v1,v2", ts1);
				
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {	}
		
		Timestamp ts2 = new Timestamp(new Date().getTime());
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {	}
		
		Timestamp ts2b = new Timestamp(new Date().getTime());
		
		table.insert("key1", "v2,v3", ts2);
		
		try {
			assertEquals("key1 value should be", "v1,v2", table.get("key1", ts1).getValue());
			assertEquals("key1 value should be", "v2,v3", table.get("key1", ts2).getValue());
			assertEquals("key1 value should be", "v1,v2", table.get("key1", ts1b).getValue());
			assertEquals("key1 value should be", "v2,v3", table.get("key1", ts2b).getValue());
		} catch (KeyNotFoundException e) {
			fail("key1 should be found");
		} catch (TimestampNotFoundException e) {
			fail("Timestamp should return a value");
		}
	}
	
	@Test(expected = TimestampNotFoundException.class) 
	public void testTimestampFail() throws Exception{
		Timestamp ts1 = new Timestamp(new Date().getTime());
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {	}
		
		Timestamp ts1b = new Timestamp(new Date().getTime());
		
		table.insert("key1", "v1,v2", ts1b);

		table.get("key1", ts1).getValue();
	}
	
	@Test(expected = KeyNotFoundException.class) 
	public void testKeyNotFoundFail() throws Exception{
		table.get("key1", null).getValue();
	}
}
