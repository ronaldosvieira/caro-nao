package dominio;

import java.sql.SQLException;

import dados.GrupoTableGateway;
import excecoes.GrupoNaoExisteException;
import util.RecordSet;
import util.Row;

public class GrupoModule {
	private RecordSet dataset;
	private GrupoTableGateway gtg;
	
	public GrupoModule(RecordSet dataset) throws ClassNotFoundException, SQLException {
		this.dataset = dataset;
		this.gtg = new GrupoTableGateway();
	}
	
	public RecordSet obter(int id) throws SQLException {
		return this.gtg.obter(id);
	}
	
	public RecordSet obterVarios(String column) throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : this.dataset) {
			if (!row.containsKey(column)) continue;
			
			Row grupo = this.gtg.obter(row.getInt(column)).get(0);
			
			resultado.add(grupo);
		}
		
		return resultado;
	}
	
	public int inserirGrupo(int idUsuario, String nome, String descricao, String regras, int limite) throws SQLException, ClassNotFoundException {
		Row grupo = new Row();
		
		grupo.put("nome", nome);
		grupo.put("descricao", descricao);
		grupo.put("regras", regras);
		grupo.put("limite", limite);
		
		dataset.add(grupo);
		
		int idGrupo = gtg.inserir(nome, descricao, regras, limite, true);
		
		GrupoUsuarioModule gum = new GrupoUsuarioModule(new RecordSet());
		gum.inserirGrupoUsuario(idGrupo, idUsuario);
		
		return idGrupo;
	}
	
	public void atualizarGrupo(int id, String nome, String descricao, int limite) throws SQLException, GrupoNaoExisteException {
		RecordSet jaExiste = gtg.obter(id);
		
		if (jaExiste.isEmpty()) {
			throw new GrupoNaoExisteException();
		}
		
		Row grupo = jaExiste.get(0);
		
		grupo.put("nome", nome);
		grupo.put("descricao", descricao);
		grupo.put("limite", limite);
		
		if (dataset.contains("id", id)) {
			dataset.set(dataset.find("id", id), grupo);
		} else {
			dataset.add(grupo);
		}
		
		gtg.atualizar(id, nome, descricao, 
				grupo.getString("regras"), limite, 
				grupo.getBoolean("ativo"));
	}
}
