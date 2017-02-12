package servico.usuario;

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

@WebServlet("/cadastrar")
public class CadastrarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CadastrarUsuario() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (UsuarioNaoLogadoException e) {
			RequestDispatcher rd = 
					request.getRequestDispatcher("views/cadastrar.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome = (String) request.getParameter("nome");
		String email = (String) request.getParameter("email");
		String telefone = (String) request.getParameter("telefone");
		
		try {
			UsuarioModule um = new UsuarioModule(new RecordSet());
			
			um.inserirUsuario(nome, email, telefone);
			
			Cookie cookie = new Cookie("caronao-login", email);
			cookie.setMaxAge(24 * 60 * 60);
			
			response.addCookie(cookie);
			
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (EmailJaCadastradoException e) {
			request.setAttribute("erro", "O email informado já foi cadastrado.");
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("views/cadastrar.jsp");
			rd.forward(request, response);
		}
	}

}
