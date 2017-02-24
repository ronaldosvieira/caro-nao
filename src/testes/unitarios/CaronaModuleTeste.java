package testes.unitarios;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import dominio.CaronaModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaJaContemPassageirosException;
import excecoes.CaronaNaoExisteException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.ErroDeValidacao;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoComMenosVagasException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;

public class CaronaModuleTeste {
	private static CaronaModule cm;
	
	@BeforeClass
	public static void before() throws ClassNotFoundException, SQLException {
		cm = new CaronaModule();
	}
	
	@Test(expected = DataInvalidaException.class)
	public void testeInserirCaronaComDataInvalida() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "329je2i3ej", "15:00", "26170230", 
				"28", "26170230", "28");
	}
	
	@Test(expected = DataInvalidaException.class)
	public void testeInserirCaronaComHoraInvalida() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "s56d00", "26170230", 
				"28", "26170230", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirCaronaComCepOrigemNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "15:00", null, 
				"28", "26170230", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirCaronaComNumeroOrigemNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "15:00", "26170230", 
				null, "26170230", "28");
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeInserirCaronaComCepOrigemInvalido() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "15:00", "26170230000", 
				"28", "26170230", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirCaronaComCepDestinoNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "15:00", "26170230", 
				"28", null, "28");
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeInserirCaronaComCepDestinoInvalido() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "15:00", "26170230", 
				"28", "26170230000", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirCaronaComNumeroDestinoNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		cm.inserirCarona(1, "2017-02-20", "15:00", "26170230", 
				"28", "26170230", null);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarCaronaComCepOrigemNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, CaronaUsuarioNaoExisteException {
		cm.atualizarCarona(1, 1, null, "28", "26170230", "28");
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeAtualizarCaronaComCepOrigemInvalido() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, CaronaUsuarioNaoExisteException {
		cm.atualizarCarona(1, 1, "26170230000", "28", "26170230", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarCaronaComNumeroOrigemNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, CaronaUsuarioNaoExisteException {
		cm.atualizarCarona(1, 1, "26170230", null, "26170230", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarCaronaComCepDestinoNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, CaronaUsuarioNaoExisteException {
		cm.atualizarCarona(1, 1, "26170230", "28", null, "28");
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeAtualizarCaronaComCepDestinoInvalido() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, CaronaUsuarioNaoExisteException {
		cm.atualizarCarona(1, 1, "26170230", "28", "26170230000", "28");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarCaronaComNumeroDestinoNulo() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException,
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, CaronaUsuarioNaoExisteException {
		cm.atualizarCarona(1, 1, "26170230", "28", "26170230", null);
	}
}
