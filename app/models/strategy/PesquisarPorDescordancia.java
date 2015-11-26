package models.strategy;

import models.Dica;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity(name="PesquisadorPorDescordancia")
public class PesquisarPorDescordancia extends PesquisadorComposto {

    @Override
    public List<Dica> pesquisar(List<Dica> dicas) {
        Collections.sort(dicas, new Comparator<Dica>() {
            @Override
            public int compare(Dica d1, Dica d2) {
                if(d1.getDiscordancias() > d2.getDiscordancias()) {
                    return -1;
                } else if(d1.getDiscordancias() < d2.getDiscordancias()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        return dicas;
    }
}
