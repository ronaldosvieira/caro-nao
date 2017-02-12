package servico;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.UsuarioModule;
import excecoes.UsuarioNaoLogadoException;
import util.RecordSet;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Dashboard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			request.setAttribute("usuario", usuario);
			
			UsuarioModule um = new UsuarioModule(new RecordSet());
			RecordSet grupos = um.listarGrupos(usuario.get(0).getInt("id"));
			
			request.setAttribute("grupos", grupos);
			
			RequestDispatcher rd = request.getRequestDispatcher("views/dashboard.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados.");
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
