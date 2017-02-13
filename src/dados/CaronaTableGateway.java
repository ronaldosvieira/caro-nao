package dados;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			row.put("dia_horario", rs.getTimestamp("dia_horario"));
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
			row.put("dia_horario", rs.getTimestamp("dia_horario"));
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
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "veiculo_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idVeiculo);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("veiculo_id", rs.getInt("veiculo_id"));
			row.put("dia_horario", rs.getTimestamp("dia_horario"));
			row.put("logradouro_origem_id", 
					rs.getInt("logradouro_origem_id"));
			row.put("logradouro_destino_id", 
					rs.getInt("logradouro_destino_id"));
			row.put("estado_carona_id", rs.getInt("estado_carona_id"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(int idVeiculo, Timestamp diaHorario, 
			int idLogradouroOrigem, int idLogradouroDestino) 
			throws SQLException {
		String sql = String.format(this.insert, 
				this.getTableName(),
				"veiculo_id, dia_horario, logradouro_origem_id,"
				+ " logradouro_destino_id",
				"?, ?, ?, ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS);

		stmt.setInt(1, idVeiculo);
		stmt.setTimestamp(2, diaHorario);
		stmt.setInt(3, idLogradouroOrigem);
		stmt.setInt(4, idLogradouroDestino);
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir carona.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir carona.");
            }
        }
	}

	public void atualizar(int id, int idVeiculo, Timestamp diaHorario, 
			int idLogradouroOrigem, int idLogradouroDestino, int idEstadoCarona) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, 
				this.getTableName(),
				"veiculo_id = ?, dia_horario = ?, logradouro_origem_id = ?, "
				+ "logradouro_destino_id = ?, estado_carona_id = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idVeiculo);
		stmt.setTimestamp(2, diaHorario);
		stmt.setInt(3, idLogradouroOrigem);
		stmt.setInt(4, idLogradouroDestino);
		stmt.setInt(5, idEstadoCarona);
		stmt.setInt(6, id);
		
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
