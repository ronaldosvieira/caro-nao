package servico.carona;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.CaronaModule;
import dominio.UsuarioModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.GrupoNaoAutorizadoException;
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
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			um.validarDonoCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));
		
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("veiculos", veiculos);
			
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
				| UsuarioNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		String idVeiculo = (String) request.getParameter("veiculo_id");

		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			um.validarDonoCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("veiculos", veiculos);
	
			cm.atualizarVeiculoDaCarona(Integer.parseInt(idCarona),
					Integer.parseInt(idVeiculo));
			
			response.sendRedirect(request.getContextPath() + "/carona/ver?id=" + idCarona);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | CaronaNaoAutorizadaException
				| VeiculoNaoExisteException | CaronaNaoExisteException
				| UsuarioNaoExisteException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (VeiculoJaSelecionadoException e) {
			request.setAttribute("erro", 
					"O veículo informado já foi selecionado para uma "
					+ "carona neste dia e horário.");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/editar.jsp");
			rd.forward(request, response);
		} catch (VeiculoComMenosVagasException e) {
			request.setAttribute("erro", 
					"Não é possível trocar para um veículo "
					+ "com menos vagas.");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/editar.jsp");
			rd.forward(request, response);
		}
	}
}
