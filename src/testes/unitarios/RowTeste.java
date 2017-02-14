package testes.unitarios;

import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Row;

public class RowTeste {
	private static Row row;
	
	@BeforeClass
	public static void before() {
		row = new Row();
		
		row.put("int", 10);
		row.put("float", 10.0f);
		row.put("boolean", true);
		row.put("string", "Teste");
		row.put("timestamp", new Timestamp(System.currentTimeMillis()));
	}
	
	@Test
	public void testeObterInt() {
		row.getInt("int");
	}

	@Test
	public void testeObterFloat() {
		row.getFloat("float");
	}
	
	@Test
	public void testeObterBoolean() {
		row.getBoolean("boolean");
	}
	
	@Test
	public void testeObterString() {
		row.getString("string");
	}
	
	@Test
	public void testeObterTimestamp() {
		row.getTimestamp("timestamp");
	}
	
	@Test(expected = NullPointerException.class)
	public void testeObterIntNaoExistente() {
		int i = row.getInt("coluna_inexistente");
		
		assertNull(i);
	}
	
	@Test(expected = NullPointerException.class)
	public void testeObterFloatNaoExistente() {
		float f = row.getFloat("coluna_inexistente");
		
		assertNull(f);
	}
	
	@Test(expected = NullPointerException.class)
	public void testeObterBooleanNaoExistente() {
		boolean b = row.getBoolean("coluna_inexistente");
		
		assertNull(b);
	}
	
	@Test
	public void testeObterStringNaoExistente() {
		String s = row.getString("coluna_inexistente");
		
		assertNull(s);
	}
	
	@Test
	public void testeObterTimestampNaoExistente() {
		Timestamp t = row.getTimestamp("coluna_inexistente");
		
		assertNull(t);
	}
	
	@Test(expected = ClassCastException.class)
	public void testeObterIntErrado() {
		row.getFloat("int");
	}
	
	@Test(expected = ClassCastException.class)
	public void testeObterFloatErrado() {
		row.getInt("float");
	}
	
	@Test(expected = ClassCastException.class)
	public void testeObterStringErrado() {
		row.getFloat("string");
	}
	
	@Test(expected = ClassCastException.class)
	public void testeObterBooleanErrado() {
		row.getFloat("boolean");
	}
	
	@Test(expected = ClassCastException.class)
	public void testeObterTimestampErrado() {
		row.getFloat("timestamp");
	}
}
