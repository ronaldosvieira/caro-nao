package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogradouroTableGateway extends TableGateway {

	public LogradouroTableGateway() throws ClassNotFoundException, SQLException {
		super("logradouro");
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
	
	public ResultSet obterPorCEP(String cep) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "cep");
		stmt.setString(3, cep);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(String cep, String estado, String distrito, 
			String endereco, String numero) 
			throws SQLException {
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "cep, estado, distrito, endereco, numero");
		
		StringBuilder data = new StringBuilder();
		
		data.append(cep); data.append(", ");
		data.append(estado); data.append(", ");
		data.append(distrito); data.append(", ");
		data.append(endereco); data.append(", ");
		data.append(numero);
		
		stmt.setString(3, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String cep, String estado, 
			String distrito, String endereco, String numero) 
			throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.updateId);
		
		stmt.setString(1, this.getTableName());
		
		StringBuilder data = new StringBuilder();
		
		data.append("cep = "); data.append(cep); data.append(", ");
		data.append("estado = "); data.append(estado); data.append(", ");
		data.append("distrito = "); data.append(distrito); data.append(", ");
		data.append("endereco = "); data.append(endereco); data.append(", ");
		data.append("numero = "); data.append(numero);
		
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
