package models.strategy;

import models.Dica;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity(name="PesquisadorPorTempo")
public class PesquisarPorTempo extends PesquisadorComposto {

    @Override
    public List<Dica> pesquisar(List<Dica> dicas) {
        Collections.sort(dicas, new Comparator<Dica>() {
            @Override
            public int compare(Dica d1, Dica d2) {
                if(d1.getId() > d2.getId()) {
                    return 1;
                }
                return -1;
            }
        });

        return dicas;
    }
}
