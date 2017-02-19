package servico.carona;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dados.AvaliacaoTableGateway;
import dados.UsuarioTableGateway;
import dominio.AvaliacaoModule;
import dominio.CaronaModule;
import dominio.GrupoUsuarioModule;
import dominio.UsuarioModule;
import excecoes.AvaliacaoJaExisteException;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.LogradouroNaoExisteException;
import excecoes.UsuarioNaoExisteException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoNaoExisteException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/avaliar")
public class AvaliarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AvaliarUsuario() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		String idAvaliado = (String) request.getParameter("usuario_id");
		
		try (UsuarioTableGateway utg = new UsuarioTableGateway()) {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet avaliado = utg.obter(Integer.parseInt(idAvaliado));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("avaliado", avaliado);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/avaliar.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | CaronaNaoAutorizadaException 
				| VeiculoNaoExisteException | UsuarioNaoExisteException 
				| LogradouroNaoExisteException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		String idAvaliado = (String) request.getParameter("usuario_id");
		String nota = (String) request.getParameter("nota");

		try (AvaliacaoTableGateway atg = new AvaliacaoTableGateway()) {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			CaronaModule cm = new CaronaModule();
			UsuarioModule um = new UsuarioModule();
			AvaliacaoModule am = new AvaliacaoModule();
			GrupoUsuarioModule gum = new GrupoUsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			am.validarInsercao(Integer.parseInt(idCarona),
			 		usuario.get(0).getInt("id"),
			 		Integer.parseInt(idAvaliado),
			 		Integer.parseInt(nota));
			
			atg.inserir(Integer.parseInt(idCarona), 
					usuario.get(0).getInt("id"), 
					Integer.parseInt(idAvaliado), 
					Integer.parseInt(nota));
			
			gum.checarAvaliacoes(Integer.parseInt(idAvaliado));
			
			response.sendRedirect(request.getContextPath() + "/carona/ver?id=" + idCarona);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (NumberFormatException | CaronaNaoAutorizadaException
				| VeiculoNaoExisteException | UsuarioNaoExisteException 
				| LogradouroNaoExisteException | AvaliacaoJaExisteException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
			e.printStackTrace();
		}
	}
}