package controllers;

import static play.data.Form.form;

import java.util.List;

import models.User;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

public class Login extends Controller {
	
	private static GenericDAO dao = new GenericDAOImpl();
	private static Form<User> loginForm = form(User.class).bindFromRequest();	

	@Transactional
    public static Result show() {
		if (session().get("username") != null) {
			return redirect(controllers.routes.Application.index());
		}
        return ok(login.render());
    }
	
	@Transactional
	public static Result logout() {
		session().clear();
		return show();
	}
    
	@Transactional
	public static Result authenticate() {		
		DynamicForm requestData = Form.form().bindFromRequest();
		String nick = requestData.get("login");
		String pass = requestData.get("pass");
		List<User> usuarioNoBD = dao.findByAttributeName("User", "login", nick);
		
        if (loginForm.hasErrors() || !validate(usuarioNoBD, pass)) {
        	flash("fail", "Login ou Senha Inválidos");
        	return unauthorized(login.render());
        } else {
        	User user = usuarioNoBD.get(0);
            session().clear();
            session("username", user.getNome());
            session("login", user.getLogin());
            
            return redirect(
                controllers.routes.Application.index()
            );
        }
    }
	
	private static boolean validate(List<User> listaUsuario, String pass) {
		try {
			User usuarioLogado = listaUsuario.get(0);
			
			if (usuarioLogado.checkPass(pass)) {
				return true;
			} else {
				return false;
			}
		} catch (IndexOutOfBoundsException e) {
			Logger.debug("Usuário não cadastrado");			
			return false;
		}
	}
}