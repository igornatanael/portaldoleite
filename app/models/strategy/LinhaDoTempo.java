package models.strategy;

import models.Dica;
import models.dao.GenericDAOImpl;

import javax.persistence.*;
import java.util.List;

@Entity(name="LinhaDoTempo")
@Table(name="linha_do_tempo")
public class LinhaDoTempo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @Transient
    private List<Dica> dicas;

    @OneToOne(targetEntity = PesquisadorComposto.class, cascade = CascadeType.ALL)
    private Pesquisador pequisador;

    @Transient
    private GenericDAOImpl dao = new GenericDAOImpl();

    public LinhaDoTempo() {
    }

    public LinhaDoTempo(Pesquisador pequisador) {
        this.pequisador = pequisador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pesquisador getPequisador() {
        return pequisador;
    }

    public void setPequisador(Pesquisador pequisador) {
        this.pequisador = pequisador;
    }

    public List<Dica> getDicas() {
        dicas = dao.findAllByClassName(Dica.class.getName());
        return pequisador.getDicas(dicas);
    }
}
