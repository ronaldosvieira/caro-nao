package servico.perfil;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.UsuarioModule;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/perfil/ver")
public class VerPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public VerPerfil() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idUsuario = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			if (idUsuario == null) {
				request.setAttribute("usuario", usuario);
			} else {
				request.setAttribute("usuario", 
						um.obter(Integer.parseInt(idUsuario)));
			}
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/perfil/ver.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.getWriter().append("Erro ao acessar o banco de dados.");
		} catch (NumberFormatException | UsuarioNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}

}
