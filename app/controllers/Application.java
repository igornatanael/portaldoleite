package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.Dica;
import models.DicaAssunto;
import models.DicaConselho;
import models.DicaDisciplina;
import models.DicaMaterial;
import models.Disciplina;
import models.Tema;
import models.MetaDica;
import models.dao.GenericDAOImpl;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Application extends Controller {
	private static GenericDAOImpl dao = new GenericDAOImpl();
	
	@Transactional
	@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(views.html.index.render("Home Page"));
    }
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result tema(long id) {
		List<Disciplina> listaDisciplina = dao.findAllByClassName(Disciplina.class.getName());
		Tema tema = dao.findByEntityId(Tema.class, id);
		if(tema == null){
			return badRequest(views.html.index.render("Tema não existe"));
		}
		return ok(views.html.tema.render(listaDisciplina, tema));
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result cadastrarDica(long idTema) {
		
		DynamicForm filledForm = Form.form().bindFromRequest();
		
		Map<String,String> formMap = filledForm.data();
		
		//long idTema = Long.parseLong(formMap.get("idTema"));
		
		Tema tema = dao.findByEntityId(Tema.class, idTema);
		String userName = session("username");
		
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render("Home Page")); //mudar
		} else {
			String tipoKey = formMap.get("tipo");
			switch (tipoKey) {
				case "assunto":
					String assunto = formMap.get("assunto");
					DicaAssunto dicaAssunto = new DicaAssunto(assunto);
					
					tema.addDica(dicaAssunto);
					dicaAssunto.setTema(tema);
					dicaAssunto.setUser(userName);
					dao.persist(dicaAssunto);				
					break;
				case "conselho":
					String conselho = formMap.get("conselho");
					DicaConselho dicaConselho = new DicaConselho(conselho);
					
					tema.addDica(dicaConselho);
					dicaConselho.setTema(tema);
					dicaConselho.setUser(userName);
					dao.persist(dicaConselho);				
					break;
				case "disciplina":
					String disciplinas = formMap.get("disciplinas");
					String razao = formMap.get("razao");
					
					DicaDisciplina dicaDisciplina = new DicaDisciplina(disciplinas, razao);
					
					tema.addDica(dicaDisciplina);
					dicaDisciplina.setTema(tema);
					dicaDisciplina.setUser(userName);
					dao.persist(dicaDisciplina);
					break;
				case "material":
					String url = formMap.get("url");
					DicaMaterial dicaMaterial = new DicaMaterial(url);
									
					tema.addDica(dicaMaterial);
					dicaMaterial.setTema(tema);
					dicaMaterial.setUser(userName);
					dao.persist(dicaMaterial);				
					break;
				default:
					break;
			}
			
			dao.merge(tema);
			
			dao.flush();			
			
			return redirect(routes.Application.tema(idTema));
		}
	}
	
	@Transactional
	public static Result avaliarDificuldadeTema(long idTema) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render("Home Page")); //mudar
		} else {
			Map<String, String> formMap = filledForm.data();
			int dificuldade = Integer.parseInt(formMap.get("dificuldade"));	
			String userLogin = session("login");
			Tema tema = dao.findByEntityId(Tema.class, idTema);
			
			//Tema tema = dao.findByEntityId(Tema.class, id)(Tema) dao.findByAttributeName("Tema", "name", nomeTema).get(0);
			tema.incrementarDificuldade(userLogin, dificuldade);
			dao.merge(tema);
			dao.flush();
			return tema(idTema);
		}
	}
	
	@Transactional
	public static Result addDiscordanciaEmDica(long idDica) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render("Home Page")); //mudar
		} else {
			Map<String, String> formMap = filledForm.data();
			String username = session("username");
			String login = session("login");
			String discordancia = formMap.get("discordancia");
			Dica dica = dao.findByEntityId(Dica.class, idDica);
			
			dica.addUsuarioQueVotou(login);
			dica.addUserCommentary(username, discordancia);
			dica.incrementaDiscordancias();
			dao.merge(dica);
			dao.flush();
			
			return tema(dica.getTema().getId());
		}
	}
	
	@Transactional
	public static Result upVoteDica(long idDica) {
		Dica dica = dao.findByEntityId(Dica.class, idDica);
		String login = session("login");
		if(!dica.wasVotedByUser(login)){
			dica.addUsuarioQueVotou(login);
			dica.incrementaConcordancias();
			dao.merge(dica);
			dao.flush();
		}
		
		return tema(dica.getTema().getId());
	}

	@Transactional
	public static Result downVoteMetaDica(long idMetaDica) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render("Home Page")); //mudar
		} else {
			Map<String, String> formMap = filledForm.data();
			String username = session("username");
			String login = session("login");
			String discordancia = formMap.get("discordancia");
			MetaDica metaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
			
			metaDica.addUsuarioQueVotou(login);
			metaDica.addUserCommentary(username, discordancia);
			metaDica.incrementaDiscordancias();
			dao.merge(metaDica);
			dao.flush();
			
			return index(); //FIXME Mudar para onde é para ir realmente.
		}
	}
	
	@Transactional
	public static Result upVoteMetaDica(long idMetaDica) {
		MetaDica metaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
		String login = session("login");
		if(!metaDica.wasVotedByUser(login)){
			metaDica.addUsuarioQueVotou(login);
			metaDica.incrementaConcordancias();
			dao.merge(metaDica);
			dao.flush();
		}
		return index(); //FIXME Mudar para onde é para ir realmente.
	}
	
	/**
	 * Action para o cadastro de uma metadica em uma disciplina.
	 * 
	 * @param idDisciplina
	 *           O id da {@code Disciplina}.
	 * @return
	 *           O Result do POST, redirecionando para a página da Disciplina caso o POST
	 *           tenha sido concluído com sucesso.
	 */
	@Transactional
	public static Result cadastrarMetaDica(long idDisciplina) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		
		Map<String,String> formMap = filledForm.data();
		
		String comment = formMap.get("comment");
		
		Disciplina disciplina = dao.findByEntityId(Disciplina.class, idDisciplina);
		String userName = session("username");
		
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render("Home Page")); //mudar
		} else {
			MetaDica metaDica = new MetaDica(disciplina, userName, comment);			
			
			Map<String,String[]> map = request().body().asFormUrlEncoded();
			
			String[] checkedDicas = map.get("dica"); //Cada dica que vai ser selecionada num checkbox deve ter como
       			                                    //atributo name "dica", e como value o id da dica. O mesmo com as metadicas.
			String[] checkedMetaDicas = map.get("metadica");
			
			List<String> listaIdDicas = Arrays.asList(checkedDicas);
			List<String> listaIdMetaDicas = Arrays.asList(checkedMetaDicas);
			
			for (String id : listaIdDicas) {
				Long idDica = Long.parseLong(id);
				
				Dica checkedDica = dao.findByEntityId(Dica.class, idDica);
				
				metaDica.addDica(checkedDica);
			}
			
			for (String id : listaIdMetaDicas) {
				Long idMetaDica = Long.parseLong(id);
				
				MetaDica checkedMetaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
				
				metaDica.addMetaDica(checkedMetaDica);
			}
			
			disciplina.addMetaDica(metaDica);
			
			dao.persist(metaDica);
			dao.merge(disciplina);
			dao.flush();
			
			return index(); //FIXME Mudar para onde é para ir realmente.
		}
	}
	
	/**
	 * Action usada para a denúncia de uma Dica considerada como imprópria pelo usuário.
	 * 
	 * @param idDica
	 *           O id da {@code Dica} denunciada.
	 * @return
	 *           O Result do POST, redirecionando para a página do {@code Tema} caso o POST
	 *           tenha sido concluído com sucesso.
	 */
	@Transactional
	public static Result denunciarDica(Long idDica) {
		Dica dica = dao.findByEntityId(Dica.class, idDica);
		Tema tema = dica.getTema();
		
		String login = session("login");
		if (!dica.wasFlaggedByUser(login)) {
			dica.addUsuarioFlag(login);
			dica.incrementaFlag();
			
			if (dica.getFlag()==3) {
				dao.removeById(Dica.class, idDica);
				dao.merge(tema);
			} else {
				dao.merge(dica);
				//dao.merge(tema);
			}
		} else {
			flash("fail", "Usuário já denunciou a dica.");
		}
		
		dao.flush();
		
		return tema(tema.getId());
	}
	
	/**
	 * Action usada para a denúncia de uma MetaDica considerada como imprópria pelo usuário.
	 * 
	 * @param idDica
	 *           O id da {@code MetaDica} denunciada.
	 * @return
	 *           O Result do POST, redirecionando para a página da {@code Disciplina} caso o POST
	 *           tenha sido concluído com sucesso.
	 */
	@Transactional
	public static Result denunciarMetaDica(Long idMetaDica) {
		MetaDica metaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
		Disciplina disciplina = metaDica.getDisciplina();
		
		String login = session("login");
		if (!metaDica.wasFlaggedByUser(login)) {
			metaDica.addUsuarioFlag(login);
			metaDica.incrementaFlag();
			
			if (metaDica.getFlag()==3) {
				dao.removeById(Dica.class, idMetaDica);
				dao.merge(disciplina);
			} else {
				dao.merge(metaDica);
				//dao.merge(tema);
			}
		} else {
			flash("fail", "Usuário já denunciou a dica.");
		}
		
		dao.flush();
		
		return index(); //FIXME Mudar para onde é para ir realmente.
	}
}