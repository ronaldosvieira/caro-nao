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
			
			RecordSet veiculos = this.vtg.obterPorUsuario(row.getInt(column));
			
			for (Row veiculo : veiculos) {
				resultado.add(veiculo);
			}
		}
		
		return resultado;
	}
	
	public int inserirVeiculo(String modelo, String placa, 
			String cor, int vagas, int idUsuario) 
			throws SQLException {
		Row veiculo = new Row();
		
		veiculo.put("modelo", modelo);
		veiculo.put("placa", placa);
		veiculo.put("cor", cor);
		veiculo.put("vagas", vagas);
		veiculo.put("usuario_id", idUsuario);
		
		dataset.add(veiculo);
		
		return vtg.inserir(modelo, placa, cor, vagas, idUsuario, true);
	}
	
	public void atualizarVeiculo(int id, String cor) 
			throws SQLException, VeiculoNaoExisteException {
		RecordSet jaExiste = vtg.obter(id);
		
		if (jaExiste.isEmpty()) {
			throw new VeiculoNaoExisteException();
		}
		
		Row veiculo = jaExiste.get(0);
		
		veiculo.put("cor", cor);
		
		if (dataset.contains("id", id)) {
			dataset.set(dataset.find("id", id), veiculo);
		} else {
			dataset.add(veiculo);
		}
		
		vtg.atualizar(id, veiculo.getString("modelo"), 
				veiculo.getString("placa"), cor, 
				veiculo.getInt("vagas"), veiculo.getInt("usuario_id"),
				veiculo.getBoolean("ativo"));
	}
}
