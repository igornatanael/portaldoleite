package controllers;

import java.util.List;

import models.User;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

public class Register extends Controller {
	
	private static GenericDAO dao = new GenericDAOImpl();

	@Transactional
    public static Result show() {
        return ok(login.render(flash()));
    }
    
	@Transactional
	public static Result register() {
		DynamicForm requestData = Form.form().bindFromRequest();
		
		String nome = requestData.get("nome");
		String email = requestData.get("email");
		String nick = requestData.get("login");
		String pass = requestData.get("pass");
		
		if(checkDBForUser(email)) {
        	flash("fail", "E-mail em uso");
        	return badRequest(login.render(flash()));
        } else if(checkDBForName(nick)) {
        	flash("fail", "Login em uso");
        	return badRequest(login.render(flash()));
        }
		else {
			User usuario = new User(email, pass, nick);
			usuario.setNome(nome);
			
        	dao.persist(usuario);
            return redirect(
                controllers.routes.Login.show()
            );
        }
    }

	private static boolean checkDBForName(String login) {
		List<User> userInDB = dao.findByAttributeName("User", "login", login);
		
		if (userInDB == null || userInDB.size() == 0) {
			return false;
		}
		return true;
	}

	private static boolean checkDBForUser(String email) {
		List<User> userInDB = dao.findByAttributeName("User", "email", email);
		
		if (userInDB == null || userInDB.size()==0) {
			return false;
		}
		return true;
	}
}
