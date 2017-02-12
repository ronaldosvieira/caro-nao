package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class VeiculoTableGateway extends TableGateway {

	public VeiculoTableGateway() throws ClassNotFoundException, SQLException {
		super("veiculo");
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
			row.put("modelo", rs.getString("modelo"));
			row.put("placa", rs.getString("placa"));
			row.put("vagas", rs.getInt("vagas"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
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
			row.put("modelo", rs.getString("modelo"));
			row.put("placa", rs.getString("placa"));
			row.put("vagas", rs.getInt("vagas"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorUsuario(int idUsuario) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "usuario_id");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, idUsuario);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("modelo", rs.getString("modelo"));
			row.put("placa", rs.getString("placa"));
			row.put("vagas", rs.getInt("vagas"));
			row.put("usuario_id", rs.getInt("usuario_id"));
			row.put("ativo", rs.getBoolean("ativo"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(String modelo, String placa, int vagas, int idUsuario, boolean ativo) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, "modelo, placa, vagas, usuario_id, ativo");
		
		StringBuilder data = new StringBuilder();
		
		data.append(modelo); data.append(", ");
		data.append(placa); data.append(", ");
		data.append(vagas); data.append(", ");
		data.append(idUsuario); data.append(", ");
		data.append(ativo);
		
		stmt.setString(2, data.toString());
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir veículo.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir veículo.");
            }
        }
	}

	public void atualizar(int id, String modelo, String placa, int vagas, 
			int idUsuario, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, 
				this.getTableName(),
				"modelo = ?, placa = ?, vagas = ?,"
				+ " usuario_id = ?, ativo = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, modelo);
		stmt.setString(2, placa);
		stmt.setInt(3, vagas);
		stmt.setInt(4, idUsuario);
		stmt.setBoolean(5, ativo);
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
