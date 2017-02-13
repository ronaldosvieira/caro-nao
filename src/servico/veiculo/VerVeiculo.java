package servico.veiculo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.VeiculoModule;
import excecoes.EmailJaCadastradoException;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/veiculo/ver")
public class VerVeiculo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public VerVeiculo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idVeiculo = (String) request.getParameter("id"); 
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			VeiculoModule vm = new VeiculoModule();
			RecordSet veiculo = vm.obter(Integer.parseInt(idVeiculo));
			
			request.setAttribute("veiculo", veiculo);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/veiculo/ver.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		}
	}
}
