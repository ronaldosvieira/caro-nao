package dominio;

import java.sql.SQLException;

import dados.CaronaUsuarioTableGateway;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.UsuarioJaEstaNaCaronaException;
import util.RecordSet;
import util.Row;

public class CaronaUsuarioModule {
	private CaronaUsuarioTableGateway cutg;
	
	public CaronaUsuarioModule() throws ClassNotFoundException, SQLException {
		this.cutg = new CaronaUsuarioTableGateway();
	}
	
	public RecordSet obter(int idCarona, int idUsuario) throws SQLException, CaronaUsuarioNaoExisteException {
		RecordSet caronaUsuario = cutg.obter(idCarona, idUsuario);
		
		if (caronaUsuario.isEmpty()) throw new CaronaUsuarioNaoExisteException();
		
		return caronaUsuario;
	}
	
	public RecordSet obterCaronaUsuarioPorUsuario(int idUsuario) throws SQLException {
		return cutg.obterPorUsuario(idUsuario);
	}
	
	public RecordSet obterCaronaUsuarioPorCarona(int idCarona) throws SQLException {
		return cutg.obterPorCarona(idCarona);
	}
	
	public RecordSet listarUsuariosPorCarona(int idCarona) throws SQLException, ClassNotFoundException, LogradouroNaoExisteException {
		RecordSet usuariosCarona = cutg.obterPorCarona(idCarona);
		
		UsuarioModule um = new UsuarioModule();
		LogradouroModule lm = new LogradouroModule();
		
		RecordSet usuarios = um.obterVarios("usuario_id", usuariosCarona);
		
		for (int i = 0; i < usuarios.size(); ++i) {
			Row usuarioCarona = usuariosCarona.get(usuariosCarona.find("usuario_id", 
					usuarios.get(i).getInt("id")));
			RecordSet logradouro = lm.obter(usuarioCarona.getInt("logradouro_id"));
			Row usuario = usuarios.get(i);
			
			usuario.put("logradouro", 
					LogradouroModule.formatarLogradouro(logradouro));
			usuario.put("logradouro_id", usuarioCarona.getInt("logradouro_id"));
			usuario.put("ativo", usuarioCarona.getBoolean("ativo"));
			
			usuarios.set(i, usuario);
		}
		
		return usuarios;
	}
	
	public int inserirCaronaUsuario(int idCarona, int idUsuario, int idLogradouro, boolean ativo) 
			throws SQLException, CaronaUsuarioJaExisteException {
		RecordSet jaExiste = cutg.obterPorUsuario(idUsuario);
		
		for (Row caronaUsuario : jaExiste) {
			if (caronaUsuario.getInt("carona_id")
					== idCarona) {
				throw new CaronaUsuarioJaExisteException();
			}
		}
		
		return cutg.inserir(idCarona, idUsuario, idLogradouro, ativo);
	}

	public void atualizarCaronaUsuario(int idCarona, int idUsuario, 
			int idLogradouro, boolean ativo) 
			throws SQLException, CaronaUsuarioNaoExisteException {
		RecordSet caronaUsuario = cutg.obter(idCarona, idUsuario);
		
		if (caronaUsuario.isEmpty()) {
			throw new CaronaUsuarioNaoExisteException();
		}
		
		cutg.atualizar(idCarona, idUsuario, idLogradouro, ativo);
	}
	
	public void aceitarConvite(int idCarona, int idUsuario) 
			throws SQLException, CaronaUsuarioNaoExisteException, 
			UsuarioJaEstaNaCaronaException {
		RecordSet caronaUsuario = cutg.obter(idCarona, idUsuario);
		
		if (caronaUsuario.isEmpty()) {
			throw new CaronaUsuarioNaoExisteException();
		}
		
		if (caronaUsuario.get(0).getBoolean("ativo")) {
			throw new UsuarioJaEstaNaCaronaException();
		}
		
		cutg.atualizar(idCarona, idUsuario, 
				caronaUsuario.get(0).getInt("logradouro_id"), true);
	}
	
	public void removerConvitesPendentes(int id) throws SQLException {
		RecordSet usuariosCarona = cutg.obterPorCarona(id);
		
		for (Row usuarioCarona : usuariosCarona) {
			if (!usuarioCarona.getBoolean("ativo")) {
				cutg.excluir(id, usuarioCarona.getInt("usuario_id"));
			}
		}
	}
	
	public void excluirCaronaUsuario(int idCarona, int idUsuario) 
			throws SQLException {
		cutg.excluir(idCarona, idUsuario);
	}
}
