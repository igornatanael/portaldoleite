package models.strategy;

import models.Dica;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;

@Entity(name="PesquisadorPorConcordancia")
public class PesquisarPorConcordancia extends PesquisadorComposto {

    @Override
    public List<Dica> pesquisar(List<Dica> dicas) {
        Collections.sort(dicas);
        return dicas;
    }
}
