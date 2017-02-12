package servico;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Sair
 */
@WebServlet("/sair")
public class Sair extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Sair() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("caronao-login")) {
					cookie.setValue(null);
					cookie.setMaxAge(0);
					
					response.addCookie(cookie);
					break;
				}
			}
		}
		
		response.sendRedirect(request.getContextPath() + "");
	}

}
