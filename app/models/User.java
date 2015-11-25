package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.mindrot.jbcrypt.BCrypt;

@Table(name="user_table")
@Entity(name="User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private Long id;

	@Column
	private String email;
	@Column
	private String pass;
	@Column
	private String nome;
	@Column
	private String login;
	
	public User() {
	}
	
	public User(String email, String pass, String login) {
		this.email = email;		
		this.pass = BCrypt.hashpw(pass, BCrypt.gensalt());
		this.login = login;
	}

	public User(String nome, String email, String pass, String login) {
		this(email, pass, login);
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean checkPass(String pass) {
		return BCrypt.checkpw(pass, this.pass);
	}

	public void setPass(String pass) {
		this.pass = BCrypt.hashpw(pass, BCrypt.gensalt());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof User)){
			return false;
		}
		User other = (User) obj;
		return other.hashCode()==this.hashCode();
	}	

	public Long getId() {
		return id;
	}
}
