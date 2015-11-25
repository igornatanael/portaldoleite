package unidade;

import models.*;
import org.junit.Before;
import org.junit.Test;

public class LinhaDoTempoTest {

    User user;
    Tema tema;
    Disciplina disciplina;
    Dica dica1, dica2, dica3, dica4;

    @Before
    public void before() {
        disciplina = new Disciplina("Álgebra Linear");
        tema = new Tema("Transformações lineares");

        dica1 = new DicaAssunto("La Place");
        dica2 = new DicaConselho("Não subestime a última prova");
        dica3 = new DicaMaterial("https://www.youtube.com/watch?v=NyAp-3QXdC0");
        dica4 = new DicaDisciplina("Álgebra Vetorial", "Pré-requisito importante");

        user = new User("schopenh@uer.com", "lifesucks", "schops");
    }

    @Test
    public void deveEstarOrdenadoPorTempo() {

    }
}
