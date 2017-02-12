package servico;

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
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import util.RecordSet;

/**
 * Servlet implementation class Entrar
 */
@WebServlet("/entrar")
public class Entrar extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Entrar() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			request.setAttribute("usuario", usuario);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (UsuarioNaoLogadoException e) {
			RequestDispatcher rd = request.getRequestDispatcher("entrar.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = (String) request.getParameter("email");
		
		try {
			UsuarioModule um = new UsuarioModule(new RecordSet());
			
			RecordSet usuario = um.autenticar(email);
			
			Cookie cookie = new Cookie("caronao-login", email);
			cookie.setMaxAge(24 * 60 * 60);
			
			response.addCookie(cookie);
			
			request.setAttribute("usuario", usuario);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
			
		} catch (UsuarioNaoExisteException e) {
			request.setAttribute("erro", "Email não cadastrado.");
			
			RequestDispatcher rd = request.getRequestDispatcher("entrar.jsp");
			rd.forward(request, response);
			
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados.");
			e.printStackTrace();
		}
	}

}
