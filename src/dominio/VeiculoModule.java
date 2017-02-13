package dominio;

import java.sql.SQLException;

import dados.VeiculoTableGateway;
import excecoes.EmailJaCadastradoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class VeiculoModule {
	private VeiculoTableGateway vtg;
	
	public VeiculoModule() throws ClassNotFoundException, SQLException {
		this.vtg = new VeiculoTableGateway();
	}
	
	public RecordSet obter(int id) throws SQLException {
		return this.vtg.obter(id);
	}
	
	public RecordSet obterVarios(String column, RecordSet dataset) 
			throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : dataset) {
			if (!row.containsKey(column)) continue;
			
			Row veiculo = this.vtg.obter(row.getInt(column)).get(0);
			
			resultado.add(veiculo);
		}
		
		return resultado;
	}
	
	public RecordSet obterVariosPorUsuario(String column, RecordSet dataset) 
			throws SQLException {
		RecordSet resultado = new RecordSet();
		
		for (Row row : dataset) {
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
		
		vtg.atualizar(id, veiculo.getString("modelo"), 
				veiculo.getString("placa"), cor, 
				veiculo.getInt("vagas"), veiculo.getInt("usuario_id"),
				veiculo.getBoolean("ativo"));
	}
}
