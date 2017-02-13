package dominio;

import java.sql.SQLException;

import dados.CaronaUsuarioTableGateway;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class CaronaUsuarioModule {
	private CaronaUsuarioTableGateway cutg;
	
	public CaronaUsuarioModule() throws ClassNotFoundException, SQLException {
		this.cutg = new CaronaUsuarioTableGateway();
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
	
	public int inserirCaronaUsuario(int idCarona, int idUsuario) 
			throws SQLException, CaronaUsuarioJaExisteException {
		RecordSet jaExiste = cutg.obterPorUsuario(idUsuario);
		
		for (Row caronaUsuario : jaExiste) {
			if (caronaUsuario.getInt("carona_id")
					== idCarona) {
				throw new CaronaUsuarioJaExisteException();
			}
		}
		
		Row caronaUsuario = new Row();
		
		caronaUsuario.put("carona_id", idCarona);
		caronaUsuario.put("usuario_id", idUsuario);
		
		return cutg.inserir(idCarona, idUsuario);
	}

	public void excluirCaronaUsuario(int idCarona, int idUsuario) 
			throws SQLException {
		cutg.excluir(idCarona, idUsuario);
	}
}
