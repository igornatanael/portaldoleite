package models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

@Entity(name="Tema")
public class Tema implements Comparable<Tema> {

	@Id
	@GeneratedValue
	@Column
	private long id;
		
	@Column
	private String name;
	
	@ManyToOne
	private Disciplina disciplina;
	
	@OneToMany(mappedBy="tema", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private List<Dica> dicas;
	
	@ElementCollection
    @MapKeyColumn(name="user_tema")
    @Column(name="dificuldade")
    @CollectionTable(name="users_diff", joinColumns=@JoinColumn(name="tema_id"))
	private Map<String, String> usersDifficulty;
	
	@Column(name = "total_diff")
	private int dificuldade;
	
	public Tema() {
	}
	
	public Tema(String name) {
		this.name = name;
		this.dicas = new ArrayList<Dica>();
		this.usersDifficulty = new HashMap<String, String>();
	}
	
	public void incrementarDificuldade(String userLogin, int dificuldade) {
		if (usersDifficulty.containsKey(userLogin)) {
			String previousDiff = usersDifficulty.get(userLogin);
			
			this.dificuldade -= Integer.valueOf(previousDiff);
		}
		
		this.dificuldade += dificuldade;
		
		usersDifficulty.put(userLogin, String.valueOf(dificuldade));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public int getDificuldade() {
		return dificuldade;
	}

	public void setDificuldade(int dificuldade) {
		this.dificuldade = dificuldade;
	}

	public long getId() {
		return id;
	}

	public List<Dica> getDicas() {
		Collections.sort(dicas);
		
		for (Dica dica : dicas) {
			dica.checaTipoDica();
		}
		
		return dicas;
	}
		
	public void addDica(Dica dica) {
		this.dicas.add(dica);
	}
	
	public Map<String, String> getUsersDifficulty() {
		return usersDifficulty;
	}

	public String getMediana() {
		List<String> difficultyVotes = new ArrayList<String>(this.usersDifficulty.values());
		
		Collections.sort(difficultyVotes);
		int totalVotes = difficultyVotes.size();
		if (totalVotes == 0) {
			return "0";
		}
		else if (totalVotes % 2 == 1) {
			String mediana = difficultyVotes.get(totalVotes/2);
			
			return mediana;
		}
		else {
			String primeiraMediana = difficultyVotes.get(totalVotes/2);
			String segundaMediana = difficultyVotes.get((totalVotes/2)-1);
			
			return String.format("%.2f", (Integer.parseInt(primeiraMediana) + Integer.parseInt(segundaMediana))/2.0);
		}
	}
	
	public String getMedia() {
		List<String> difficultyVotes = new ArrayList<String>(this.usersDifficulty.values());
		
		int totalVotes = difficultyVotes.size();
		if(totalVotes == 0){
			return "0";
		}
		
		double votesCount = 0;
		
		for (String vote : difficultyVotes) {
			votesCount = votesCount + Double.parseDouble(vote);
		}
		
		return String.format("%.2f", votesCount/totalVotes);
	}

	@Override
	public int compareTo(Tema other) {
		if(this.getName().length() > other.getName().length()) {
			return 1;
		} else if(this.getName().length() < other.getName().length()) {
			return -1;
		} else {
			return this.getName().compareTo(other.getName());
		}
	}
}
