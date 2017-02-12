package dominio;

import java.sql.SQLException;

import dados.GrupoTableGateway;
import excecoes.EmailJaCadastradoException;
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
	
	public void inserirGrupo(String nome, String descricao, String regras, int limite) throws SQLException {
		Row grupo = new Row();
		
		grupo.put("nome", nome);
		grupo.put("descricao", descricao);
		grupo.put("regras", regras);
		grupo.put("limite", limite);
		
		dataset.add(grupo);
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
	}
	
	public void armazenar() throws SQLException {
		for (int i = 0; i < dataset.size(); ++i) {
			Row grupo = dataset.get(i);
			boolean jaExiste = grupo.containsKey("id");
			
			if (jaExiste) {
				gtg.atualizar(grupo.getInt("id"), 
						grupo.getString("nome"), 
						grupo.getString("descricao"), 
						grupo.getString("regras"),
						grupo.getInt("limite"),
						grupo.getBoolean("ativo"));
			} else {
				int id = gtg.inserir(grupo.getString("nome"), 
						grupo.getString("descricao"), 
						grupo.getString("regras"),
						grupo.getInt("limite"),
						grupo.getBoolean("ativo"));
				
				grupo.put("id", id);
				dataset.set(i, grupo);
			}
		}
	}
}
