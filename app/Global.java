import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import models.*;
import models.dao.GenericDAOImpl;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;


public class Global extends GlobalSettings {

	private static GenericDAOImpl dao = new GenericDAOImpl();

	private List<User> usuarios = new ArrayList<>();
	private List<Disciplina> disciplinas = new ArrayList<>();
	
	@Override
	public void onStart(Application app) {
		Logger.info("Aplicação inicializada...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				if(dao.findAllByClassName(User.class.getName()).size() == 0) {
					criarUsuarios();
				}
				if(dao.findAllByClassName(Disciplina.class.getName()).size() == 0) {
					criarDisciplinaTemas();
				}
			}
		});
	}

	@Override
	public void onStop(Application app) {
	    JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Logger.info("Aplicação finalizando...");

				usuarios = dao.findAllByClassName("User");
				disciplinas = dao.findAllByClassName("Disciplina");

				for(User user: usuarios) {
					dao.removeById(User.class, user.getId());
				}
				for(Disciplina disciplina : disciplinas) {
					dao.removeById(Disciplina.class, disciplina.getId());
				}
			}
		});
	}

	private void criarUsuarios() {
		List<User> users = new ArrayList<>();

		users.add(new User("demo", "demo@example.com", "admin", "demo"));
		users.add(new User("admin", "admin@example.com", "admin", "admin"));
		users.add(new User("stack", "stack@example.com", "x37lw91", "stack"));
		users.add(new User("diego", "diegoado@gmail.com", "claymore", "diego"));
		users.add(new User("fulano", "fulano@example.com", "fulano", "fulano"));
		users.add(new User("default", "default@example.com", "default", "default"));
		users.add(new User("cicrano", "cicrano@example.com", "cicrano", "cicrano"));
		users.add(new User("igor", "igor.ataide@ccc.ufcg.edu.br", "ataide", "igor"));
		users.add(new User("nazareno", "nazareno@example.com", "default", "nazareno"));
		users.add(new User("beltrano", "beltrano@example.com", "beltrano", "beltrano"));

		users.stream().forEach(u -> dao.persist(u));
		dao.flush();
	}
	
	private void criarDisciplinaTemas() {
		Disciplina db = new Disciplina("Banco de Dados I");
		Disciplina es = new Disciplina("Engenharia de Software I");
		Disciplina si = new Disciplina("Sistemas de Informação I");

		db.addTema(new Tema("SQL", db));
		db.addTema(new Tema("JDBC", db));
		db.addTema(new Tema("Modelo Relacional", db));

		es.addTema(new Tema("Provas", es));
		es.addTema(new Tema("Android", es));
		es.addTema(new Tema("Metodos Formais", es));
		es.addTema(new Tema("Estudo dos Artigos", es));
		es.addTema(new Tema("Verificação e Validação", es));

		si.addTema(new Tema("Análise x Design", si));
		si.addTema(new Tema("Programação Orientação à Objetos", si));
		si.addTema(new Tema("GRASP", si));
		si.addTema(new Tema("GoF", si));
		si.addTema(new Tema("Arquitetura de Software", si));
		si.addTema(new Tema("Play Framework", si));
		si.addTema(new Tema("JavaScript", si));
		si.addTema(new Tema("HTML / CSS / Bootstrap", si));
		si.addTema(new Tema("Heroku", si));
		si.addTema(new Tema("LABs", si));
		si.addTema(new Tema("Minitestes", si));
		si.addTema(new Tema("Projeto", si));

		criarDicasTema(si);
		avaliarDicasTemas(si.getTemas());

		dao.persist(db, es, si);
		dao.flush();
	}

	private void criarDicasTema(Disciplina disciplina) {
		String urls[] = {
				"https://en.wikipedia.org/wiki/Object-oriented_programming",
				"http://www.wthreex.com/rup/process/workflow/ana_desi/co_swarch.htm",
				"http://www.w3schools.com/",
				"http://www.wthreex.com/rup/process/workflow/ana_desi/cos_and.htm",
				"https://www.playframework.com/documentation/2.4.x/Home"
		};
		List<Tema> temas = disciplina.getTemas();
		ListIterator<Tema> it = temas.listIterator(temas.size());

		for(String url : urls) {
			DicaMaterial dica = new DicaMaterial(url);
			if(it.hasPrevious()) {
				Tema tema = it.previous();

				tema.addDica(dica);
				dica.setTema(tema);
				dica.setUser("admin");
			} else {
				break;
			}
		}
	}

	private void avaliarDicasTemas(List<Tema> temas) {
		ListIterator<Tema> it = temas.listIterator(temas.size());

		while(it.hasPrevious()) {
			List<Dica> dicas = it.previous().getDicas();
			if(!dicas.isEmpty()) {
				Dica dica = dicas.get(0);

				dica.incrementaConcordancias();
				dica.addUsuarioQueVotou("demo");

				dica.incrementaDiscordancias();
				dica.addUsuarioQueVotou("stack");
				dica.addUserCommentary("stack",
						"Material indicado não está diretamente relacionado com o tema");
			} else {
				break;
			}
		}
	}
}
