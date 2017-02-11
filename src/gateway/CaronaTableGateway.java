package gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CaronaTableGateway extends TableGateway {

	public CaronaTableGateway() throws ClassNotFoundException, SQLException {
		super("carona");
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
	
	public ResultSet obterPorVeiculo(int idVeiculo) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectColumn);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "veiculo_id");
		stmt.setInt(3, idVeiculo);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(int idVeiculo, Date horario, 
			int idLogradouroOrigem, int idLogradouroDestino) 
			throws SQLException {
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "veiculo_id, horario, logradouro_origem_id, logradouro_destino_id");
		
		StringBuilder data = new StringBuilder();
		
		data.append(idVeiculo); data.append(", ");
		data.append(horario); data.append(", ");
		data.append(idLogradouroOrigem); data.append(", ");
		data.append(idLogradouroDestino);
		
		stmt.setString(3, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, int idVeiculo, Date horario, 
			int idLogradouroOrigem, int idLogradouroDestino, int idEstadoCarona) 
			throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.updateId);
		
		stmt.setString(1, this.getTableName());
		
		StringBuilder data = new StringBuilder();
		
		data.append("veiculo_id = "); data.append(idVeiculo); data.append(", ");
		data.append("horario = "); data.append(horario); data.append(", ");
		data.append("logradouro_origem_id = "); data.append(idLogradouroOrigem); data.append(", ");
		data.append("logradouro_destino_id = "); data.append(idLogradouroDestino); data.append(", ");
		data.append("estado_carona_id = "); data.append(idEstadoCarona);
		
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
