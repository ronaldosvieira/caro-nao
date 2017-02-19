package dominio;

import java.sql.SQLException;

import dados.UsuarioTableGateway;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaNaoExisteException;
import excecoes.EmailJaCadastradoException;
import excecoes.ErroDeValidacao;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoNaoAutorizadoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class UsuarioModule {
	private UsuarioTableGateway utg;
	
	public UsuarioModule() throws ClassNotFoundException, SQLException {
		this.utg = new UsuarioTableGateway();
	}
	
	public void validarExistencia(RecordSet usuario) throws SQLException, UsuarioNaoExisteException {
		if (usuario.isEmpty()) {
			throw new UsuarioNaoExisteException();
		}
	}
	
	public RecordSet obter(int id) throws SQLException, UsuarioNaoExisteException {
		RecordSet usuario = this.utg.obter(id);
		
		if (usuario.isEmpty()) throw new UsuarioNaoExisteException();
		
		return usuario;
	}
	
	public RecordSet obterPeloEmail(String email) throws SQLException, UsuarioNaoExisteException {
		RecordSet usuario = this.utg.obterPeloEmail(email);
		
		if (usuario.isEmpty()) throw new UsuarioNaoExisteException();
		
		return usuario;
	}
	
	public RecordSet obterVarios(String column, RecordSet dataset) 
			throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : dataset) {
			if (!row.containsKey(column)) continue;
			
			Row usuario;
			try {
				usuario = this.obter(row.getInt(column)).get(0);
				resultado.add(usuario);
			} catch (UsuarioNaoExisteException e) {continue;}
		}
		
		return resultado;
	}
	
	public RecordSet listarGrupos(int id) throws SQLException, ClassNotFoundException, UsuarioNaoExisteException {
		RecordSet usuario = this.obter(id);
		
		GrupoUsuarioModule gum = new GrupoUsuarioModule();
		
		return gum.listarGruposPorUsuario(id);
	}
	
	public RecordSet listarVeiculos(int id) throws SQLException, ClassNotFoundException, UsuarioNaoExisteException {
		RecordSet usuario = this.obter(id);
		
		VeiculoModule vm = new VeiculoModule();
		
		return vm.obterVariosPorUsuario("id", usuario);
	}
	
	public RecordSet listarCaronas(int id) throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		GrupoModule gm = new GrupoModule();
		VeiculoModule vm = new VeiculoModule();
		CaronaModule cm = new CaronaModule();
		LogradouroModule lm = new LogradouroModule();
		
		RecordSet grupos = this.listarGrupos(id);
		RecordSet amigos = new RecordSet();
		RecordSet caronas = new RecordSet();
		
		for (Row grupo : grupos) {
			if (grupo.getBoolean("usuario_ativo") &&
					grupo.getBoolean("aceitou_regras")) {
				RecordSet usuariosGrupo = 
						gm.listarUsuarios(grupo.getInt("id"));
				
				for (Row usuarioGrupo : usuariosGrupo) {
					if (!amigos.contains("id", usuarioGrupo.getInt("id"))) {
						amigos.add(usuarioGrupo);
					}
				}
			}
		}
		
		RecordSet amigosVeiculos = vm.obterVariosPorUsuario("id", amigos);
		
		for (Row amigoVeiculo : amigosVeiculos) {
			RecordSet caronasAmigo = 
					cm.obterPeloVeiculo(amigoVeiculo.getInt("id"));
			
			for (Row caronaAmigo : caronasAmigo) {
				if (!caronas.contains("id", caronaAmigo.getInt("id"))) {
					RecordSet origem = lm.obter(caronaAmigo.getInt("logradouro_origem_id"));
					RecordSet destino = lm.obter(caronaAmigo.getInt("logradouro_destino_id"));
					RecordSet usuariosCarona = 
							cm.listarUsuarios(caronaAmigo.getInt("id"));
					
					int vagasRestantes = amigoVeiculo.getInt("vagas") 
							- usuariosCarona.size();
					
					caronaAmigo.put("veiculo", 
							amigoVeiculo.getString("modelo"));
					caronaAmigo.put("origem", 
							LogradouroModule.formatarLogradouro(origem));
					caronaAmigo.put("destino", 
							LogradouroModule.formatarLogradouro(destino));
					caronaAmigo.put("vagas", 
							amigoVeiculo.getInt("vagas"));
					caronaAmigo.put("vagas_restantes", vagasRestantes);
					caronas.add(caronaAmigo);
				}
			}
		}
		
		return caronas;
	}
	
	public void validarInsercaoUsuario(String nome, String email, String telefone) 
			throws EmailJaCadastradoException, SQLException, 
			ErroDeValidacao {
		RecordSet jaExiste = utg.obterPeloEmail(email);
		
		if (!jaExiste.isEmpty()) {
			throw new EmailJaCadastradoException();
		}
		
		if (nome == null || nome.equals("")) 
			throw new ErroDeValidacao("Nome não pode ser nulo.");
		if (email == null || email.equals(""))
			throw new ErroDeValidacao("Email não pode ser nulo.");
		if (telefone == null || telefone.equals(""))
			throw new ErroDeValidacao("Telefone não pode ser nulo.");
		
		if (!email.contains("@"));
			throw new ErroDeValidacao("Email inválido.");
	}
	
	public void atualizarUsuario(int id, String nome, String telefone) throws SQLException, UsuarioNaoExisteException {
		RecordSet jaExiste = utg.obter(id);
		
		if (jaExiste.isEmpty()) {
			throw new UsuarioNaoExisteException();
		}
		
		Row usuario = jaExiste.get(0);
		
		usuario.put("nome", nome);
		usuario.put("telefone", telefone);
		
		utg.atualizar(id, nome, usuario.getString("email"), telefone);
	}
	
	public RecordSet validarVeiculo(int id, int idVeiculo) throws ClassNotFoundException, SQLException, VeiculoNaoAutorizadoException, UsuarioNaoExisteException {
		RecordSet veiculosDoUsuario = this.listarVeiculos(id);
		RecordSet veiculo = new RecordSet();
		
		for (Row veiculoDoUsuario : veiculosDoUsuario) {
			if (veiculoDoUsuario.getInt("id") == idVeiculo) {
				veiculo.add(veiculoDoUsuario);
				
				break;
			}
		}
		
		if (veiculo.isEmpty()) throw new VeiculoNaoAutorizadoException();
		else return veiculo;
	}
	
	public RecordSet validarGrupo(int id, int idGrupo) throws GrupoNaoAutorizadoException, ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		RecordSet gruposDoUsuario = this.listarGrupos(id);
		RecordSet grupo = new RecordSet();
		
		for (Row grupoDoUsuario : gruposDoUsuario) {
			if (grupoDoUsuario.getInt("id") == idGrupo) {
				grupo.add(grupoDoUsuario);
				
				break;
			}
		}
		
		if (grupo.isEmpty()) throw new GrupoNaoAutorizadoException();
		else return grupo;
	}

	public RecordSet validarDonoCarona(int id, int idCarona) 
			throws SQLException, ClassNotFoundException, 
			CaronaNaoExisteException, VeiculoNaoExisteException, 
			CaronaNaoAutorizadaException, UsuarioNaoExisteException {
		CaronaModule cm = new CaronaModule();
		
		RecordSet usuario = this.obter(id);
		RecordSet dono = cm.obterDono(idCarona);
		
		if (dono.get(0).getInt("id") != usuario.get(0).getInt("id")) {
			throw new CaronaNaoAutorizadaException();
		} 

		return dono;
	}
	
	public RecordSet validarCarona(int id, int idCarona) 
			throws ClassNotFoundException, SQLException, 
			CaronaNaoAutorizadaException, VeiculoNaoExisteException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		VeiculoModule vm = new VeiculoModule();
		LogradouroModule lm = new LogradouroModule();
		
		RecordSet caronasDoUsuario = this.listarCaronas(id);
		RecordSet carona = new RecordSet();
		
		for (Row caronaDoUsuario : caronasDoUsuario) {
			if (caronaDoUsuario.getInt("id") == idCarona) {
				carona.add(caronaDoUsuario);
				
				break;
			}
		}
		
		if (carona.isEmpty()) throw new CaronaNaoAutorizadaException();
		
		Row caronaRow = carona.get(0);
		
		RecordSet veiculo = vm.obter(caronaRow.getInt("veiculo_id"));
		RecordSet origem = lm.obter(caronaRow.getInt("logradouro_origem_id"));
		RecordSet destino = lm.obter(caronaRow.getInt("logradouro_destino_id"));
		
		caronaRow.put("veiculo", 
				veiculo.get(0).getString("modelo"));
		caronaRow.put("origem", 
				LogradouroModule.formatarLogradouro(origem));
		caronaRow.put("destino", 
				LogradouroModule.formatarLogradouro(destino));
		caronaRow.put("vagas", 
				veiculo.get(0).getInt("vagas"));
		
		carona.set(0, caronaRow);
		
		return carona;
	}
	
	public boolean isMotorista(int id) throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		RecordSet veiculos = listarVeiculos(id);
		
		return !veiculos.isEmpty();
	}
	
	public void convidarUsuario(String email, int idGrupo) 
			throws SQLException, UsuarioNaoExisteException, 
			ClassNotFoundException, GrupoUsuarioJaExisteException, GrupoNaoExisteException {
		RecordSet usuario = utg.obterPeloEmail(email);
		
		if (usuario.isEmpty()) {
			throw new UsuarioNaoExisteException();
		}
		
		GrupoUsuarioModule gum = new GrupoUsuarioModule();
		GrupoModule gm = new GrupoModule();
		
		gm.obter(idGrupo);
		
		gum.inserirGrupoUsuario(idGrupo, 
				usuario.get(0).getInt("id"));
	}
}
