package Bean;

import java.util.ArrayList;

public class Variabile {
	
	//Dichiarazione delle variabili di istanza della classe
	private String nome;
	private float valore;
	
	//Inserimento dei costruttori all'interno della classe
	public Variabile (String name, float value) {
		this.nome= name;
		this.valore= value;
	}
	
	//Dichiarazione dei metodi getter e setter
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getValore() {
		return valore;
	}

	public void setValore(float valore) {
		this.valore = valore;
	}
}