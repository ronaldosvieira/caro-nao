package dominio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

import dados.LogradouroTableGateway;
import excecoes.CEPInvalidoException;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import util.RecordSet;
import util.Row;

public class LogradouroModule {
	private LogradouroTableGateway ltg;
	
	public LogradouroModule() throws ClassNotFoundException, SQLException {
		this.ltg = new LogradouroTableGateway();
	}
	
	public static String formatarLogradouro(RecordSet logradouro) {
		return logradouro.get(0).getString("endereco") +
				", " + logradouro.get(0).getString("numero") +
				". " + logradouro.get(0).getString("distrito") +
				". " + logradouro.get(0).getString("cidade") +
				"/" + logradouro.get(0).getString("estado");
	}
	
	public RecordSet obter(int id) throws SQLException {
		return this.ltg.obter(id);
	}
	
	public RecordSet obterPorCep(String cep) throws LogradouroNaoExisteException, SQLException {
		RecordSet logradouro = this.ltg.obterPorCEP(cep);
		
		if (logradouro.isEmpty()) {
			throw new LogradouroNaoExisteException();
		}
		
		return logradouro;
	}
	
	public int inserirLogradouro(String cep, String numero) 
			throws SQLException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException {
		cep = cep.replace("-", "");
		RecordSet logradouro = this.obterLogradouroPeloCEP(cep);
		Row logr = logradouro.get(0);
		
		logr.put("numero", numero);
		
		logradouro.set(0, logr);
		
		return ltg.inserir(cep, logr.getString("estado"), 
				logr.getString("cidade"), logr.getString("distrito"), 
				logr.getString("endereco"), numero);
	}
	
	private RecordSet obterLogradouroPeloCEP(String cep) 
			throws ServicoDeEnderecosInacessivelException, CEPInvalidoException {
		RecordSet logradouro = new RecordSet();
		cep = cep.replace("-", "");
		
		URLConnection con;
		try {
			con = new URL("http://viacep.com.br/ws/" + 
					cep + "/piped/")
			.openConnection();
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							con.getInputStream()));
			
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
			
			String[] logr = response.toString().split("\\|");
			Row row = new Row();
			
			if (logr.length < 6) throw new CEPInvalidoException(cep);
			
			row.put("cep", logr[0].replace("cep:", ""));
			row.put("estado", logr[5].replace("uf:", ""));
			row.put("cidade", logr[4].replace("localidade:", ""));
			row.put("distrito", logr[3].replace("bairro:", ""));
			row.put("endereco", logr[1].replace("logradouro:", ""));
			
			logradouro.add(row);
			
			return logradouro;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServicoDeEnderecosInacessivelException();
		}
		
	}
}
