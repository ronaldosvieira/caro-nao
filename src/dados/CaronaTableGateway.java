package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import util.RecordSet;
import util.Row;

public class CaronaTableGateway extends TableGateway {

	public CaronaTableGateway() throws ClassNotFoundException, SQLException {
		super("carona");
	}
	
	public RecordSet obterTodos() throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.select, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("veiculo_id", rs.getInt("veiculo_id"));
			row.put("horario", rs.getDate("horario"));
			row.put("logradouro_origem_id", 
					rs.getInt("logradouro_origem_id"));
			row.put("logradouro_destino_id", 
					rs.getInt("logradouro_destino_id"));
			row.put("estado_carona_id", rs.getInt("estado_carona_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("veiculo_id", rs.getInt("veiculo_id"));
			row.put("horario", rs.getDate("horario"));
			row.put("logradouro_origem_id", 
					rs.getInt("logradouro_origem_id"));
			row.put("logradouro_destino_id", 
					rs.getInt("logradouro_destino_id"));
			row.put("estado_carona_id", rs.getInt("estado_carona_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorVeiculo(int idVeiculo) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "veiculo_id");
		stmt.setInt(2, idVeiculo);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("veiculo_id", rs.getInt("veiculo_id"));
			row.put("horario", rs.getDate("horario"));
			row.put("logradouro_origem_id", 
					rs.getInt("logradouro_origem_id"));
			row.put("logradouro_destino_id", 
					rs.getInt("logradouro_destino_id"));
			row.put("estado_carona_id", rs.getInt("estado_carona_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public void inserir(int idVeiculo, Date horario, 
			int idLogradouroOrigem, int idLogradouroDestino) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "veiculo_id, horario, logradouro_origem_id, logradouro_destino_id");
		
		StringBuilder data = new StringBuilder();
		
		data.append(idVeiculo); data.append(", ");
		data.append(horario); data.append(", ");
		data.append(idLogradouroOrigem); data.append(", ");
		data.append(idLogradouroDestino);
		
		stmt.setString(2, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, int idVeiculo, Date horario, 
			int idLogradouroOrigem, int idLogradouroDestino, int idEstadoCarona) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("veiculo_id = "); data.append(idVeiculo); data.append(", ");
		data.append("horario = "); data.append(horario); data.append(", ");
		data.append("logradouro_origem_id = "); data.append(idLogradouroOrigem); data.append(", ");
		data.append("logradouro_destino_id = "); data.append(idLogradouroDestino); data.append(", ");
		data.append("estado_carona_id = "); data.append(idEstadoCarona);
		
		stmt.setString(1, data.toString());
		stmt.setInt(2, id);
		
		stmt.executeUpdate();
	}

	public void excluir(int id) throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.deleteId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		stmt.executeUpdate();
	}
}
