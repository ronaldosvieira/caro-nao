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
import excecoes.EmailJaCadastradoException;
import util.RecordSet;

/**
 * Servlet implementation class CriacaoUsuario
 */
@WebServlet("/usuario/criar")
public class CriacaoUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CriacaoUsuario() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = (String) request.getAttribute("nome");
		String email = (String) request.getAttribute("email");
		String telefone = (String) request.getAttribute("telefone");
		
		UsuarioModule usuario = new UsuarioModule(new RecordSet());
		
		try {
			usuario.inserirUsuario(nome, email, telefone);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (EmailJaCadastradoException e) {
			request.setAttribute("erro", "O email informado já foi cadastrado.");
			
			// todo: colocar pagina
			RequestDispatcher rd = request.getRequestDispatcher("pagina1");
			rd.forward(request, response);
		}
		
		// todo: colocar pagina
		RequestDispatcher rd = request.getRequestDispatcher("pagina2");
		rd.forward(request, response);
	}

}
