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
import util.RecordSet;

/**
 * Servlet implementation class Inicial
 */
@WebServlet("")
public class Inicial extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Inicial() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		int idUsuarioLogado = 0;
		
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("caronao-login")) {
				idUsuarioLogado = Integer.parseInt(cookie.getValue());
				break;
			}
		}
		
		try {
			UsuarioModule um = new UsuarioModule(new RecordSet());
			
			RecordSet usuario = um.autenticar(idUsuarioLogado);
			
			request.setAttribute("usuario", usuario);
			
			// redireciona para dashboard do usuário
			RequestDispatcher rd = request.getRequestDispatcher("dashboard.jsp");
			rd.forward(request, response);
			
		} catch (UsuarioNaoExisteException e) { // redireciona para página de login
			RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
			rd.forward(request, response);
			
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados.");
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
