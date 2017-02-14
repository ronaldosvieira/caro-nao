package testes.unitarios;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import util.RecordSet;
import util.Row;

public class RecordSetTeste {
	private static RecordSet rs;
	
	@BeforeClass
	public static void before() {
		rs = new RecordSet();
		
		Row linha0 = new Row();
		linha0.put("coluna1", 1);
		rs.add(linha0);
		
		Row linha1 = new Row();
		linha1.put("coluna1", 3);
		rs.add(linha1);
		
		Row linha2 = new Row();
		linha2.put("coluna1", 3);
		rs.add(linha2);
	}
	
	@Test
	public void testeFindUnico() {
		int resultado = rs.find("coluna1", 1);
		
		assertNotEquals(-1, resultado);
		assertEquals(0, resultado);
	}
	
	@Test
	public void testeContainsUnico() {
		boolean resultado = rs.contains("coluna1", 1);
		
		assertTrue(resultado);
	}
	
	@Test
	public void testeFindDoisElementos() {
		int resultado = rs.find("coluna1", 3);
		
		assertNotEquals(-1, resultado);
		assertEquals(1, resultado);
	}
	
	@Test
	public void testeContainsDoisElementos() {
		boolean resultado = rs.contains("coluna1", 3);
		
		assertTrue(resultado);
	}
	
	@Test
	public void testeFindValorNaoExistente() {
		int resultado = rs.find("coluna1", 5);
		
		assertEquals(-1, resultado);
	}
	
	@Test
	public void testeContainsValorNaoExistente() {
		boolean resultado = rs.contains("coluna1", 5);
		
		assertFalse(resultado);
	}
	
	@Test
	public void testeFindColunaNaoExistente() {
		int resultado = rs.find("coluna2", 1);
		
		assertEquals(-1, resultado);
	}
	
	@Test
	public void testeContainsColunaNaoExistente() {
		boolean resultado = rs.contains("coluna2", 1);
		
		assertFalse(resultado);
	}
}
