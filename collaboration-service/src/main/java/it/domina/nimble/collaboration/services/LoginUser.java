package it.domina.nimble.collaboration.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.Utils;
import it.domina.nimble.collaboration.auth.type.CredentialType;
import it.domina.nimble.collaboration.auth.type.IdentityUserType;
import it.domina.nimble.collaboration.exceptions.PermissionDenied;

/**
 * Servlet implementation class Collaborate
 */

@WebServlet(name = "login", description = "user login", urlPatterns = { "/login" })
public class LoginUser extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginUser.class);
     
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String content = request.getHeader("content-type");
	        if (content.equals("application/json")) {
	            String responseString = Utils.getInputStreamAsString(request.getInputStream());
	            logger.log(Level.INFO, responseString);
	            CredentialType params = CredentialType.mapJson(responseString);
	            
	            IdentityUserType user = ServiceConfig.getInstance().getAuth().userLogin(params.getUsername(), params.getPassword());
	            if (user!=null) {
	            	response.setStatus(HttpServletResponse.SC_OK);
	            	response.getWriter().write(user.getAccessToken());
	            }
	            else {
	            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            	response.getWriter().write(PermissionDenied.ERRCODE);
	            }
	        }
	        else {
	            String err = "The content-type should application/json";
	        	logger.log(Level.INFO, err);
	            response.getWriter().write(err);
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        }
		} catch (Exception e) {
        	logger.log(Level.INFO, e);
            response.getWriter().write(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
