package models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class DicaAssunto extends Dica{
	@Column
	private String assunto;
	
	public DicaAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
}
