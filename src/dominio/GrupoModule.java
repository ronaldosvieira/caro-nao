package dominio;

import java.sql.SQLException;

import dados.GrupoTableGateway;
import excecoes.DesativacaoGrupoInvalidaException;
import excecoes.ErroDeValidacao;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class GrupoModule {
	private GrupoTableGateway gtg;
	
	public GrupoModule() throws ClassNotFoundException, SQLException {
		this.gtg = new GrupoTableGateway();
	}
	
	public RecordSet obter(int id) throws SQLException, GrupoNaoExisteException {
		RecordSet grupo = this.gtg.obter(id);
		
		if (grupo.isEmpty()) throw new GrupoNaoExisteException();
		
		return grupo;
	}
	
	public RecordSet obterVarios(String column, RecordSet dataset) 
			throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : dataset) {
			if (!row.containsKey(column)) continue;
			
			Row grupo;
			try {
				grupo = this.obter(row.getInt(column)).get(0);
				resultado.add(grupo);
			} catch (GrupoNaoExisteException e) {continue;}
		}
		
		return resultado;
	}
	
	public RecordSet listarUsuarios(int id) throws SQLException, ClassNotFoundException {
		GrupoUsuarioModule gum = new GrupoUsuarioModule();
		
		return gum.listarUsuariosPorGrupo(id);
	}
	
	public int inserirGrupo(int idUsuario, String nome, 
			String descricao, String regras, int limite) 
				throws SQLException, ClassNotFoundException, 
				GrupoUsuarioJaExisteException, ErroDeValidacao {
		Row grupo = new Row();
		
		if (nome == null || nome.length() == 0)
			throw new ErroDeValidacao("É necessário informar o "
					+ "nome do grupo.");
		if (descricao == null || descricao.length() == 0)
			throw new ErroDeValidacao("É necessário informar a "
					+ "descricao do grupo.");
		if (regras == null || regras.length() == 0)
			throw new ErroDeValidacao("É necessário informar as "
					+ "regras do grupo.");
		if (limite < 1 || limite > 1000)
			throw new ErroDeValidacao("Limite de avaliações "
					+ "negativas inválido");
		
		int idGrupo = gtg.inserir(nome, descricao, regras, limite, true);
		
		GrupoUsuarioModule gum = new GrupoUsuarioModule();
		gum.inserirGrupoUsuario(idGrupo, idUsuario);
		
		return idGrupo;
	}
	
	public void atualizarGrupo(int id, String nome, String descricao, 
			int limite) 
				throws SQLException, GrupoNaoExisteException, 
				ErroDeValidacao {
		RecordSet jaExiste = gtg.obter(id);
		
		if (jaExiste.isEmpty()) {
			throw new GrupoNaoExisteException();
		}
		
		Row grupo = jaExiste.get(0);
		
		if (nome == null || nome.length() == 0)
			throw new ErroDeValidacao("É necessário informar o "
					+ "nome do grupo.");
		if (descricao == null || descricao.length() == 0)
			throw new ErroDeValidacao("É necessário informar a "
					+ "descricao do grupo.");
		if (limite < 1 || limite > 1000)
			throw new ErroDeValidacao("Limite de avaliações "
					+ "negativas inválido");
		
		gtg.atualizar(id, nome, descricao, 
				grupo.getString("regras"), limite, 
				grupo.getBoolean("ativo"));
	}
	
	public void desativarGrupo(int id) throws ClassNotFoundException, SQLException, DesativacaoGrupoInvalidaException, GrupoNaoExisteException, GrupoUsuarioNaoExisteException {
		GrupoUsuarioModule gum = new GrupoUsuarioModule();
		
		RecordSet usuariosGrupo = gum.obterGrupoUsuarioPorGrupo(id);
		
		if (usuariosGrupo.size() > 1) {
			throw new DesativacaoGrupoInvalidaException();
		} else {
			Row grupo = this.obter(id).get(0);
			
			for (Row usuarioGrupo : usuariosGrupo) {
				gum.desativarGrupoUsuario(usuarioGrupo.getInt("id"));
			}
			
			gtg.atualizar(id, grupo.getString("nome"), 
					grupo.getString("descricao"), grupo.getString("regras"), 
					grupo.getInt("limite_avaliacoes_negativas"), 
					false);
		}
	}
}
