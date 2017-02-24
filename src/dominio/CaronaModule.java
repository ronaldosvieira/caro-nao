package dominio;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import dados.CaronaTableGateway;
import excecoes.CEPInvalidoException;
import excecoes.CaronaJaContemPassageirosException;
import excecoes.CaronaNaoAtivaException;
import excecoes.CaronaNaoExisteException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.ErroDeValidacao;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoComMenosVagasException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class CaronaModule {
	private CaronaTableGateway ctg;
	private enum EstadoCarona {
		Ativa(1), Cancelada(2), Concluida(3);
		private int id;
		
		private EstadoCarona(int id) {this.id = id;}
		public int getId() {return this.id;}
	};

	private static long intervaloMinEntreCaronas = 
			1 /* h */ * 60 /* m */ * 60 /* s */ * 1000 /* ms */;
	
	public CaronaModule() throws ClassNotFoundException, SQLException {
		this.ctg = new CaronaTableGateway();
	}
	
	public RecordSet obter(int id) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.ctg.obter(id);
		
		if (carona.isEmpty()) throw new CaronaNaoExisteException();
		
		return carona;
	}
	
	public RecordSet obterPeloVeiculo(int idVeiculo) throws SQLException {
		return this.ctg.obterPorVeiculo(idVeiculo);
	}
	
	public RecordSet obterDono(int id) 
			throws ClassNotFoundException, SQLException, 
			CaronaNaoExisteException, VeiculoNaoExisteException, 
			UsuarioNaoExisteException {
		VeiculoModule vm = new VeiculoModule();
		
		RecordSet carona = this.obter(id);
		
		return vm.obterDono(carona.get(0).getInt("veiculo_id"));
	}
	
	public RecordSet listarUsuarios(int id) throws SQLException, ClassNotFoundException, LogradouroNaoExisteException {
		CaronaUsuarioModule gum = new CaronaUsuarioModule();
		
		return gum.listarUsuariosPorCarona(id);
	}
	
	public int inserirCarona(int idVeiculo, String data, String horario, 
			String cepOrigem, String numeroOrigem, String cepDestino,
			String numeroDestino) 
			throws SQLException, VeiculoJaSelecionadoException, 
			ClassNotFoundException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, 
			VeiculoNaoExisteException, CaronaUsuarioJaExisteException, 
			UsuarioNaoExisteException, ErroDeValidacao {
		String[] dataSep = data.split("-");
		String[] horarioSep = horario.split(":");
		
		if (dataSep.length != 3 || horarioSep.length != 2) 
			throw new DataInvalidaException(data + " " + horario);
		
		Timestamp diaHorario;
		
		try {
			diaHorario = Timestamp.valueOf(
				LocalDateTime.of(Integer.parseInt(dataSep[0]), 
					Integer.parseInt(dataSep[1]), 
					Integer.parseInt(dataSep[2]), 
					Integer.parseInt(horarioSep[0]), 
					Integer.parseInt(horarioSep[1])));
		} catch (NumberFormatException e) {
			throw new DataInvalidaException(data + " " + horario);
		}
		
		if (cepOrigem == null) 
			throw new ErroDeValidacao("É necessário inserir o cep de origem.");
		if (cepDestino == null) 
			throw new ErroDeValidacao("É necessário inserir o cep de destino.");
		
		if (numeroOrigem == null) 
			throw new ErroDeValidacao("É necessário inserir o número de origem.");
		if (numeroDestino == null) 
			throw new ErroDeValidacao("É necessário inserir o número de destino.");
		
		cepOrigem = cepOrigem.replace("-", "");
		cepDestino = cepDestino.replace("-", "");
		
		if (cepOrigem.length() != 8) 
			throw new CEPInvalidoException(cepOrigem);
		if (cepDestino.length() != 8) 
			throw new CEPInvalidoException(cepDestino);
		
		try {Integer.parseInt(cepOrigem);} 
		catch (NumberFormatException e) {
			throw new CEPInvalidoException(cepOrigem);
		}
		
		try {Integer.parseInt(cepDestino);} 
		catch (NumberFormatException e) {
			throw new CEPInvalidoException(cepDestino);
		}
		
		VeiculoModule vm = new VeiculoModule();
		
		vm.obter(idVeiculo);
		
		RecordSet caronas = this.obterPeloVeiculo(idVeiculo);
		
		for (Row carona: caronas) {
			if (carona.getInt("estado_carona_id") 
					!= EstadoCarona.Ativa.getId()) {
				continue;
			}
			
			if (Math.abs(carona.getTimestamp("dia_horario").getTime()
					- diaHorario.getTime()) <= intervaloMinEntreCaronas) {
				throw new VeiculoJaSelecionadoException();
			}
		}
		
		LogradouroModule lm = new LogradouroModule();
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		RecordSet criador = vm.obterDono(idVeiculo);
		
		int idLogradouroOrigem = 
				lm.inserirLogradouro(cepOrigem, numeroOrigem);
		int idLogradouroDestino =
				lm.inserirLogradouro(cepDestino, numeroDestino);
		
		int idCarona = ctg.inserir(idVeiculo, diaHorario, 
				idLogradouroOrigem, idLogradouroDestino);
		
		cum.inserirCaronaUsuario(idCarona, 
				criador.get(0).getInt("id"), idLogradouroDestino,
				true);
		
		return idCarona;
	}
	
	public void atualizarCarona(int id, int idVeiculo, 
			String cepOrigem, String numeroOrigem, 
			String cepDestino, String numeroDestino) 
			throws SQLException, VeiculoNaoExisteException, 
			VeiculoJaSelecionadoException, CaronaNaoExisteException, 
			ClassNotFoundException, VeiculoComMenosVagasException, 
			CaronaJaContemPassageirosException, LogradouroNaoExisteException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioNaoExisteException, ErroDeValidacao {
		VeiculoModule vm = new VeiculoModule();
		CaronaModule cm = new CaronaModule();
		LogradouroModule lm = new LogradouroModule();
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		if (cepOrigem == null) 
			throw new ErroDeValidacao("É necessário inserir o cep de origem.");
		if (cepDestino == null) 
			throw new ErroDeValidacao("É necessário inserir o cep de destino.");
		
		if (numeroOrigem == null) 
			throw new ErroDeValidacao("É necessário inserir o número de origem.");
		if (numeroDestino == null) 
			throw new ErroDeValidacao("É necessário inserir o número de destino.");
		
		cepOrigem = cepOrigem.replace("-", "");
		cepDestino = cepDestino.replace("-", "");
		
		if (cepOrigem.length() != 8) 
			throw new CEPInvalidoException(cepOrigem);
		if (cepDestino.length() != 8) 
			throw new CEPInvalidoException(cepDestino);
		
		try {Integer.parseInt(cepOrigem);} 
		catch (NumberFormatException e) {
			throw new CEPInvalidoException(cepOrigem);
		}
		
		try {Integer.parseInt(cepDestino);} 
		catch (NumberFormatException e) {
			throw new CEPInvalidoException(cepDestino);
		}
		
		RecordSet carona = this.obter(id);
		RecordSet novoVeiculo = vm.obter(idVeiculo);
		int idVeiculoAtual = carona.get(0).getInt("veiculo_id");
		RecordSet veiculoAtual = vm.obter(idVeiculoAtual);

		if (novoVeiculo.get(0).getInt("vagas")
				< veiculoAtual.get(0).getInt("vagas")) {
			throw new VeiculoComMenosVagasException();
		}
		
		RecordSet caronasVeiculo = this.obterPeloVeiculo(idVeiculo);
		
		Row caronaRow = carona.get(0);
		
		for (Row caronaVeiculo: caronasVeiculo) {
			if (caronaVeiculo.getInt("id") == caronaRow.getInt("id")) {
				continue;
			}
			
			if (caronaVeiculo.getInt("estado_carona_id") 
					!= EstadoCarona.Ativa.getId()) {
				continue;
			}
			
			if (Math.abs(caronaVeiculo.getTimestamp("dia_horario").getTime()
					- caronaRow.getTimestamp("dia_horario").getTime())
					<= intervaloMinEntreCaronas) {
				throw new VeiculoJaSelecionadoException();
			}
		}
		
		RecordSet origem = lm.obter(caronaRow.getInt("logradouro_origem_id"));
		RecordSet destino = lm.obter(caronaRow.getInt("logradouro_destino_id"));
		
		int idLogradouroOrigem = origem.get(0).getInt("id");
		int idLogradouroDestino = destino.get(0).getInt("id");
		
		if (!cepOrigem.equals(origem.get(0).getString("cep"))
				|| !numeroOrigem.equals(origem.get(0).getString("numero"))) {
			if (cm.listarUsuarios(id).size() > 1) {
				throw new CaronaJaContemPassageirosException();
			}
			
			idLogradouroOrigem = 
					lm.inserirLogradouro(cepOrigem, numeroOrigem);
		}
		
		if (!cepDestino.equals(destino.get(0).getString("cep"))
				|| !numeroDestino.equals(destino.get(0).getString("numero"))) {
			if (cm.listarUsuarios(id).size() > 1) {
				throw new CaronaJaContemPassageirosException();
			}
			
			idLogradouroDestino = 
					lm.inserirLogradouro(cepDestino, numeroDestino);
			
			RecordSet caronaUsuario = cum.obter(id, 
					veiculoAtual.get(0).getInt("usuario_id"));
			cum.atualizarCaronaUsuario(
					id, veiculoAtual.get(0).getInt("usuario_id"), 
					idLogradouroDestino, 
					caronaUsuario.get(0).getBoolean("ativo"));
		}
		
		ctg.atualizar(id, idVeiculo, caronaRow.getTimestamp("dia_horario"), 
				idLogradouroOrigem, 
				idLogradouroDestino, 
				caronaRow.getInt("estado_carona_id"));
	}
	
	private void atualizarEstadoDaCarona(int id, int idEstadoCarona) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.obter(id);
		
		this.ctg.atualizar(id, carona.get(0).getInt("veiculo_id"), 
				carona.get(0).getTimestamp("dia_horario"), 
				carona.get(0).getInt("logradouro_origem_id"), 
				carona.get(0).getInt("logradouro_destino_id"), 
				idEstadoCarona);
	}

	public void cancelarCarona(int id) throws SQLException, CaronaNaoExisteException {
		RecordSet carona = this.obter(id);
		Row caronaRow = carona.get(0);
		
		boolean estaAtiva = caronaRow.getInt("estado_carona_id")
				== EstadoCarona.Ativa.getId();
		boolean jaComecou = caronaRow.getTimestamp("dia_horario").getTime()
				< new Timestamp(System.currentTimeMillis()).getTime();
		
		if (estaAtiva && !jaComecou) {
			this.atualizarEstadoDaCarona(id, 
					EstadoCarona.Cancelada.getId());
		}
	}

	public void concluirCarona(int id) throws SQLException, CaronaNaoExisteException, ClassNotFoundException {
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		RecordSet carona = this.obter(id);
		Row caronaRow = carona.get(0);
		
		boolean estaAtiva = caronaRow.getInt("estado_carona_id")
				== EstadoCarona.Ativa.getId();
		boolean jaComecou = caronaRow.getTimestamp("dia_horario").getTime()
				< new Timestamp(System.currentTimeMillis()).getTime();
		
		cum.removerConvitesPendentes(id);
		
		if (estaAtiva && jaComecou) {
			this.atualizarEstadoDaCarona(id, 
					EstadoCarona.Concluida.getId());
		}
	}
	
	public void convidarUsuario(int id, int idUsuario, String cep) 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioJaExisteException, ErroDeValidacao {
		this.inserirUsuarioNaCarona(id, idUsuario, 
				cep, false);
	}
	
	public void convidarUsuario(int id, int idUsuario, int idLogradouro) 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioJaExisteException {
		this.inserirUsuarioNaCarona(id, idUsuario, 
				idLogradouro, false);
	}
	
	private void inserirUsuarioNaCarona(int id, int idUsuario,
			String cep, boolean ativo) 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, CaronaUsuarioJaExisteException, 
			ErroDeValidacao {
		LogradouroModule lm = new LogradouroModule();
		
		cep = cep.replace("-", "");
		
		if (cep == null) 
			throw new ErroDeValidacao("É necessário inserir o CEP.");
		if (cep.length() != 8)
			throw new CEPInvalidoException(cep);
		
		try {Integer.parseInt(cep);}
		catch (NumberFormatException e) {
			throw new CEPInvalidoException(cep);
		}
		
		int idLogradouro = lm.inserirLogradouro(cep, "" /* TODO */);
		
		this.inserirUsuarioNaCarona(id, idUsuario, idLogradouro, ativo);
	}

	private void inserirUsuarioNaCarona(int id, int idUsuario, 
			int idLogradouro, boolean ativo) 
			throws ClassNotFoundException, SQLException, 
			CaronaUsuarioJaExisteException {
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
	
		// TODO validar se é possível adicionar usuario na carona
		
		cum.inserirCaronaUsuario(id, idUsuario, idLogradouro, ativo);
	}

	public void removerUsuarioDaCarona(int id, int idUsuario) 
			throws SQLException, ClassNotFoundException, 
			CaronaUsuarioNaoExisteException, CaronaNaoAtivaException, 
			CaronaNaoExisteException {
		CaronaUsuarioModule cum = new CaronaUsuarioModule();
		
		RecordSet carona = this.obter(id);
		
		if (carona.get(0).getInt("estado_carona_id") 
				!= EstadoCarona.Ativa.getId()) {
			throw new CaronaNaoAtivaException();
		}
		
		RecordSet caronaUsuario = cum.obter(id, idUsuario);
	
		cum.excluirCaronaUsuario(id, idUsuario);
	}
}
