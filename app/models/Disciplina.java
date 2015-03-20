package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name="Disciplina")
public class Disciplina {
	@Id
	@GeneratedValue
	private long id;
	
	private String nome;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Tema> temas;

	@OneToMany(cascade=CascadeType.ALL)
	private List<MetaDica> metaDicas;
	
	public Disciplina() {
	}
	
	public Disciplina(String nome) {
		this.nome = nome;
		this.temas = new ArrayList<Tema>();
		this.metaDicas = new ArrayList<MetaDica>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Tema> getTemas() {
		return temas;
	}

	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}
	
	public void addTema(Tema tema){
		this.temas.add(tema);
	}
	
	public Tema getTemaByNome(String nome){
		for (Tema tema: temas) {
			if(tema.getName().equals(nome)){
				return tema;
			}
		}
		return null;
	}

	public List<MetaDica> getMetaDicas() {
		return this.metaDicas;
	}

	public void addMetaDica(MetaDica metaDica) {
		this.metaDicas.add(metaDica);
	}
}
