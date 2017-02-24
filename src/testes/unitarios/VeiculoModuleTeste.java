package testes.unitarios;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import dominio.VeiculoModule;
import excecoes.EmailJaCadastradoException;
import excecoes.ErroDeValidacao;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoNaoExisteException;

public class VeiculoModuleTeste {
	private static VeiculoModule vm;
	
	@BeforeClass
	public static void before() throws ClassNotFoundException, SQLException {
		vm = new VeiculoModule();
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirVeiculoComModeloNulo() 
			throws SQLException, ErroDeValidacao {
		vm.inserirVeiculo(null, "DJS-8399", "Vermelho", 5, 1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirVeiculoComPlacaNula() 
			throws SQLException, ErroDeValidacao {
		vm.inserirVeiculo("Punto", null, "Vermelho", 5, 1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirVeiculoComCorNula() 
			throws SQLException, ErroDeValidacao {
		vm.inserirVeiculo("Punto", "DJS-8399", null, 5, 1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirVeiculoComVagasNegativas() 
			throws SQLException, ErroDeValidacao {
		vm.inserirVeiculo("Punto", "DJS-8399", "Vermelho", -1, 1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirVeiculoComVagasExcessivas() 
			throws SQLException, ErroDeValidacao {
		vm.inserirVeiculo("Punto", "DJS-8399", "Vermelho", 5000, 1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarVeiculoComCorNula() 
			throws SQLException, ErroDeValidacao, VeiculoNaoExisteException {
		vm.atualizarVeiculo(1, null);
	}
}
