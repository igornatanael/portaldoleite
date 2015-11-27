package unidade;

import static org.fest.assertions.Assertions.assertThat;

import models.*;
import models.strategy.PesquisarPorTempo;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class LinhaDoTempoTest {

    User user;
    Tema tema;
    Disciplina disciplina;
    List<Dica> dicas = new ArrayList<>();

    private static int NUM_DICAS = 4;

    @Before
    public void before() {
        tema = new Tema("Transformações lineares");
        disciplina = new Disciplina("Álgebra Linear");
        user = new User("schopenh@uer.com", "lifesucks", "schops");

        criarDicasTema();
        avaliarDicasTemas();
        disciplina.addTema(tema);

    }

    @Test
    public void deveEstarOrdenadoPorTempo() {
        user.getLinhaDoTempo().setDicas(dicas);
        user.getLinhaDoTempo().setPequisador(new PesquisarPorTempo());

        List<Dica> dicas = user.getLinhaDoTempo().getDicas();
        assertThat(dicas.get(0).getTexto()).isEqualTo("Álgebra Vetorial");
    }

    private void criarDicasTema() {
        Dica[] arrayDicas = {
                new DicaAssunto("La Place"),
                new DicaConselho("Não subestime a última prova"),
                new DicaMaterial("https://www.youtube.com/watch?v=NyAp-3QXdC0"),
                new DicaDisciplina("Álgebra Vetorial", "Pré-requisito importante")
        };

        dicas.addAll(Arrays.asList(arrayDicas));

        for(Dica actualDica : dicas) {
            tema.addDica(actualDica);
            actualDica.setTema(tema);
            actualDica.setUser("admin");
            actualDica.setDateCreate(Calendar.getInstance());
        }
    }

    private void avaliarDicasTemas() {
        List<Dica> dicas = tema.getDicas();
        for (int i=0; i<NUM_DICAS-1; i++) {
            Dica dica = dicas.get(i);
            dica.incrementaConcordancias();
            dica.addUsuarioQueVotou("demo");
        }
        for (int i=NUM_DICAS-1; i>1; i--) {
            Dica dica = dicas.get(i);
            dica.incrementaDiscordancias();
            dica.addUsuarioQueVotou("stack");
            dica.addUserCommentary("stack",
                    "Material indicado não está diretamente relacionado com o tema");
        }

        dicas.get(NUM_DICAS-1).incrementaDiscordancias();
        dicas.get(NUM_DICAS-1).addUsuarioQueVotou("demo");
        dicas.get(NUM_DICAS-1).addUserCommentary("stack",
                "Material indicado não está diretamente relacionado com o tema");

        dicas.get(0).getConcordancias();
        dicas.get(0).addUsuarioQueVotou("stack");
    }
}
