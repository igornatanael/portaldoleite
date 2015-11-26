package models.strategy;

import models.Dica;

import javax.persistence.*;
import java.util.List;

@Table(name="pesquisador")
@Entity(name="Pesquisador")
public abstract class PesquisadorComposto implements Pesquisador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @OneToOne(targetEntity = PesquisadorComposto.class, cascade = CascadeType.ALL)
    protected Pesquisador next;

    public PesquisadorComposto() {
    }

    public PesquisadorComposto(Pesquisador next) {
        this.next = next;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Pesquisador getNext() {
        return next;
    }

    @Override
    public void setNext(Pesquisador next) {
        this.next = next;
    }

    @Override
    public List<Dica> getDicas(List<Dica> dicas) {
        if(hasNext()) {
            return pesquisar(next.getDicas(dicas));
        }
        return pesquisar(dicas);
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    abstract public List<Dica> pesquisar(List<Dica> dicas);

    @Override
    public void addNext(Pesquisador next) {
        Pesquisador actual = this;

        while(actual.hasNext()) {
            actual = actual.getNext();
        }
        actual.setNext(next);
    }
}
