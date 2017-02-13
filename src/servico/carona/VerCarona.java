package servico.carona;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.CaronaModule;
import dominio.UsuarioModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.DataInvalidaException;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoLogadoException;
import excecoes.VeiculoJaSelecionadoException;
import servico.autenticacao.Autenticacao;
import util.RecordSet;

@WebServlet("/carona/ver")
public class VerCarona extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public VerCarona() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCarona = (String) request.getParameter("id");
		
		try {
			RecordSet usuario = Autenticacao.autenticar(request, response);
			
			UsuarioModule um = new UsuarioModule();
			
			RecordSet carona = um.validarCarona(usuario.get(0).getInt("id"), 
					Integer.parseInt(idCarona));
			
			RecordSet veiculos = um.listarVeiculos(usuario.get(0).getInt("id"));
			
			boolean dono = veiculos.contains("id", carona.get(0).getInt("veiculo_id"));
			
			request.setAttribute("usuario", usuario);
			request.setAttribute("carona", carona);
			request.setAttribute("dono", dono);
			
			RequestDispatcher rd = 
					request.getRequestDispatcher("../views/carona/ver.jsp");
			rd.forward(request, response);
		} catch (UsuarioNaoLogadoException e) {
			response.sendRedirect(request.getContextPath() + "");
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Erro ao acessar o banco de dados");
			e.printStackTrace();
		} catch (NumberFormatException | CaronaNaoAutorizadaException e) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
		}
	}
}
