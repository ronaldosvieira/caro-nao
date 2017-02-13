package servico.perfil;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.UsuarioModule;
import excecoes.EmailJaCadastradoException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;
import util.Row;

@WebServlet("/perfil/editar")
public class EditarPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public EditarPerfil() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			request.setAttribute("usuario", usuario);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/perfil/editar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = (String) request.getParameter("nome");
		String telefone = (String) request.getParameter("telefone");
		
		try {
			UsuarioModule um = new UsuarioModule();
			
			Row usuario = Autenticacao.autenticar(request, response).get(0);
			
			um.atualizarUsuario(usuario.getInt("id"), nome, telefone);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoExisteException e) {
			request.setAttribute("erro", "N�o foi poss�vel editar o perfil.");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/perfil/editar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		}
	}

}
