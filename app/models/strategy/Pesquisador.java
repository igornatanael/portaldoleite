package models.strategy;

import models.Dica;

import java.util.List;

public interface Pesquisador {

    List<Dica> getDicas(List<Dica> dicas);

    boolean hasNext();

    Pesquisador getNext();

    void setNext(Pesquisador next);
}
