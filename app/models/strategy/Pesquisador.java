package models.strategy;

import models.Dica;

import java.util.List;

public interface Pesquisador {

    boolean hasNext();

    Pesquisador getNext();

    void setNext(Pesquisador next);

    void addNext(Pesquisador next);

    List<Dica> getDicas(List<Dica> dicas);
}
