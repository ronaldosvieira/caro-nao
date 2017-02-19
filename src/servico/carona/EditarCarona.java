package servico.carona;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dados.LogradouroTableGateway;
import dominio.CaronaModule;
import dominio.LogradouroModule;
import dominio.UsuarioModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaJaContemPassageirosException;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaNaoExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoComMenosVagasException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/editar")
public class EditarCarona extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EditarCarona() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		
		try (LogradouroTableGateway ltg = new LogradouroTableGateway()) {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			um.validarDonoCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));
		
			RecordSet origem = ltg.obter(
					carona.get(0).getInt("logradouro_origem_id"));
			RecordSet destino = ltg.obter(
					carona.get(0).getInt("logradouro_destino_id"));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("veiculos", veiculos);
			request.setAttribute("origem", origem);
			request.setAttribute("destino", destino);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/editar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | CaronaNaoAutorizadaException
				| VeiculoNaoExisteException | CaronaNaoExisteException
				| UsuarioNaoExisteException | LogradouroNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		String idVeiculo = (String) request.getParameter("veiculo_id");
		String cepOrigem = request.getParameter("cep_origem");
		String cepDestino = request.getParameter("cep_destino");
		String numeroOrigem = request.getParameter("numero_origem");
		String numeroDestino = request.getParameter("numero_destino");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			LogradouroModule lm = new LogradouroModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			um.validarDonoCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("veiculos", veiculos);
	
			cm.atualizarCarona(Integer.parseInt(idCarona), 
					Integer.parseInt(idVeiculo),
					cepOrigem, numeroOrigem, 
					cepDestino, numeroDestino);
			
			response.sendRedirect(request.getContextPath() + "/carona/ver?id=" + idCarona);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | CaronaNaoAutorizadaException
				| VeiculoNaoExisteException | CaronaNaoExisteException
				| UsuarioNaoExisteException | LogradouroNaoExisteException 
				| CaronaUsuarioNaoExisteException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
			e.printStackTrace();
		} catch (VeiculoJaSelecionadoException e) {
			request.setAttribute("erro", 
					"O veículo informado já foi selecionado para uma "
					+ "carona neste dia e horário.");
			
			doGet(request, response);
		} catch (VeiculoComMenosVagasException e) {
			request.setAttribute("erro", 
					"Não é possível trocar para um veículo "
					+ "com menos vagas.");
			
			doGet(request, response);
		} catch (ServicoDeEnderecosInacessivelException e) {
			response.getWriter().append(
					"Erro ao acessar o serviço de endereços.");
			e.printStackTrace();
		} catch (CEPInvalidoException e) {
			request.setAttribute("erro", 
					"CEP inválido: " + e.getCep() + ".");
			
			doGet(request, response);
		} catch (CaronaJaContemPassageirosException e) {
			request.setAttribute("erro", 
					"Não é possível editar a origem ou destino: "
					+ "já existem passageiros na carona.");
			
			doGet(request, response);
		}
	}
}
