package dominio;

import java.sql.SQLException;

import dados.VeiculoTableGateway;
import excecoes.EmailJaCadastradoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class VeiculoModule {
	private RecordSet dataset;
	private VeiculoTableGateway vtg;
	
	public VeiculoModule(RecordSet dataset) throws ClassNotFoundException, SQLException {
		this.dataset = dataset;
		this.vtg = new VeiculoTableGateway();
	}
	
	public RecordSet obter(int id) throws SQLException {
		return this.vtg.obter(id);
	}
	
	public RecordSet obterVarios(String column) throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : this.dataset) {
			if (!row.containsKey(column)) continue;
			
			Row veiculo = this.vtg.obter(row.getInt(column)).get(0);
			
			resultado.add(veiculo);
		}
		
		return resultado;
	}
	
	public RecordSet obterVariosPorUsuario(String column) throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : this.dataset) {
			if (!row.containsKey(column)) continue;
			
			Row veiculo = this.vtg.obterPorUsuario(row.getInt(column)).get(0);
			
			resultado.add(veiculo);
		}
		
		return resultado;
	}
	
	public void inserirVeiculo(String modelo, String placa, 
			String cor, int vagas, int idUsuario) 
			throws EmailJaCadastradoException, SQLException {
		Row veiculo = new Row();
		
		veiculo.put("modelo", modelo);
		veiculo.put("placa", placa);
		veiculo.put("cor", cor);
		veiculo.put("vagas", vagas);
		veiculo.put("usuario_id", idUsuario);
		
		dataset.add(veiculo);
	}
	
	public void atualizarVeiculo(int id, String cor) 
			throws SQLException, VeiculoNaoExisteException {
		RecordSet jaExiste = vtg.obter(id);
		
		if (jaExiste.isEmpty()) {
			throw new VeiculoNaoExisteException();
		}
		
		Row veiculo = jaExiste.get(0);
		
		veiculo.put("nome", cor);
		
		if (dataset.contains("id", id)) {
			dataset.set(dataset.find("id", id), veiculo);
		} else {
			dataset.add(veiculo);
		}
	}
	
	public void armazenar() throws SQLException {
		for (int i = 0; i < dataset.size(); ++i) {
			Row veiculo = dataset.get(i);
			boolean jaExiste = veiculo.containsKey("id");
			
			if (jaExiste) {
				vtg.atualizar(veiculo.getInt("id"), 
						veiculo.getString("modelo"), 
						veiculo.getString("placa"), 
						veiculo.getString("cor"),
						veiculo.getInt("vagas"),
						veiculo.getInt("usuario_id"),
						veiculo.getBoolean("ativo"));
			} else {
				int id = vtg.inserir(veiculo.getString("modelo"), 
						veiculo.getString("placa"), 
						veiculo.getString("cor"),
						veiculo.getInt("vagas"),
						veiculo.getInt("usuario_id"),
						veiculo.getBoolean("ativo"));
				
				veiculo.put("id", id);
				dataset.set(i, veiculo);
			}
		}
	}
}
