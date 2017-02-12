package servico.veiculo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.UsuarioModule;
import dominio.VeiculoModule;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoAutorizadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/veiculo/editar")
public class EditarVeiculo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EditarVeiculo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idVeiculo = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule(new RecordSet());
			
			RecordSet veiculo = um.validarVeiculo(
					usuario.get(0).getInt("id"), 
					Integer.parseInt(idVeiculo));
			
			request.setAttribute("veiculo", veiculo);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/veiculo/editar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | VeiculoNaoAutorizadoException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idVeiculo = (String) request.getParameter("id");
		String cor = (String) request.getParameter("cor");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule(new RecordSet());
			um.validarVeiculo(
				usuario.get(0).getInt("id"), 
				Integer.parseInt(idVeiculo));
			
			VeiculoModule vm = new VeiculoModule(new RecordSet());
			
			vm.atualizarVeiculo(Integer.parseInt(idVeiculo), cor);
			vm.armazenar();
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | VeiculoNaoExisteException 
				| VeiculoNaoAutorizadoException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

}
