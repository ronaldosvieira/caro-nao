package gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VeiculoTableGateway extends TableGateway {

	public VeiculoTableGateway() throws ClassNotFoundException, SQLException {
		super("veiculo");
	}

	public ResultSet obterTodos() throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.select);
		
		stmt.setString(1, this.getTableName());
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		return rs;
	}

	public ResultSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectId);
		
		stmt.setString(1, this.getTableName());
		stmt.setInt(2, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}
	
	public ResultSet obterPorUsuario(int idUsuario) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "usuario_id");
		stmt.setInt(3, idUsuario);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(String modelo, String placa, int vagas, int idUsuario, boolean ativo) 
			throws SQLException {
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "modelo, placa, vagas, usuario_id, ativo");
		
		StringBuilder data = new StringBuilder();
		
		data.append(modelo); data.append(", ");
		data.append(placa); data.append(", ");
		data.append(vagas); data.append(", ");
		data.append(idUsuario); data.append(", ");
		data.append(ativo);
		
		stmt.setString(3, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String modelo, String placa, int vagas, 
			int idUsuario, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.updateId);
		
		stmt.setString(1, this.getTableName());
		
		StringBuilder data = new StringBuilder();
		
		data.append("modelo = "); data.append(modelo); data.append(", ");
		data.append("placa = "); data.append(placa); data.append(", ");
		data.append("vagas = "); data.append(vagas); data.append(", ");
		data.append("idUsuario = "); data.append(idUsuario); data.append(", ");
		data.append("ativo = "); data.append(ativo);
		
		stmt.setString(2, data.toString());
		stmt.setInt(3, id);
		
		stmt.executeUpdate();
	}

	public void excluir(int id) throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.deleteId);
		
		stmt.setString(1, this.getTableName());
		stmt.setInt(2, id);
		
		stmt.executeUpdate();
	}
}
