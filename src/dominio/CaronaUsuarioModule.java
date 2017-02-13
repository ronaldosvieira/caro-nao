package dominio;

import java.sql.SQLException;

import dados.CaronaUsuarioTableGateway;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExiste;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class CaronaUsuarioModule {
	private CaronaUsuarioTableGateway cutg;
	
	public CaronaUsuarioModule() throws ClassNotFoundException, SQLException {
		this.cutg = new CaronaUsuarioTableGateway();
	}
	
	public RecordSet obter(int idCarona, int idUsuario) throws SQLException, CaronaUsuarioNaoExiste {
		RecordSet caronaUsuario = cutg.obter(idCarona, idUsuario);
		
		if (caronaUsuario.isEmpty()) throw new CaronaUsuarioNaoExiste();
		
		return caronaUsuario;
	}
	
	public RecordSet obterCaronaUsuarioPorUsuario(int idUsuario) throws SQLException {
		return cutg.obterPorUsuario(idUsuario);
	}
	
	public RecordSet obterCaronaUsuarioPorCarona(int idCarona) throws SQLException {
		return cutg.obterPorCarona(idCarona);
	}
	
	public RecordSet listarUsuariosPorCarona(int idCarona) throws SQLException, ClassNotFoundException {
		RecordSet usuariosCarona = cutg.obterPorCarona(idCarona);
		
		UsuarioModule um = new UsuarioModule();
		
		return um.obterVarios("usuario_id", usuariosCarona);
	}
	
	public int inserirCaronaUsuario(int idCarona, int idUsuario, int idLogradouro) 
			throws SQLException, CaronaUsuarioJaExisteException {
		RecordSet jaExiste = cutg.obterPorUsuario(idUsuario);
		
		for (Row caronaUsuario : jaExiste) {
			if (caronaUsuario.getInt("carona_id")
					== idCarona) {
				throw new CaronaUsuarioJaExisteException();
			}
		}
		
		return cutg.inserir(idCarona, idUsuario, idLogradouro);
	}

	public void excluirCaronaUsuario(int idCarona, int idUsuario) 
			throws SQLException {
		cutg.excluir(idCarona, idUsuario);
	}
}
