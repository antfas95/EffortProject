package Bean;

public class Modello {
	
	//Dichiarazione delle variabili di istanza della classe
	private int k;
	private String tipologia_distanza;
	private float effort; 
	private String tipologia_analisi;
	private float distance;
	
	public Modello(int k, String s1, String s2, float effort) {
		this.k= k;
		this.tipologia_distanza= s1;
		this.tipologia_analisi= s2;
		this.effort= effort;
	}
	
	public Modello(int k, String s1, String s2, float distanza, float effort) {
		this.k= k;
		this.tipologia_distanza= s1;
		this.tipologia_analisi= s2;
		this.distance= distanza;
		this.effort= effort;
	}
	
	public Modello(int k, String distanza, String analisi) {
		this.k= k;
		this.tipologia_distanza= distanza;
		this.tipologia_analisi= analisi;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getTipologia_distanza() {
		return tipologia_distanza;
	}

	public void setTipologia_distanza(String tipologia_distanza) {
		this.tipologia_distanza = tipologia_distanza;
	}

	public String getTipologia_analisi() {
		return tipologia_analisi;
	}

	public void setTipologia_analisi(String tipologia_analisi) {
		this.tipologia_analisi = tipologia_analisi;
	}

	public float getEffort() {
		return effort;
	}

	public void setEffort(float effort) {
		this.effort = effort;
	}
	
	public String toString() {
		String ritorno= "Modello --> k: " + getK() + " Analisi: " + getTipologia_analisi() + " Distanza: " + getDistance() + " Tipo.Distanza: " + getTipologia_distanza() + " Effort: " + getEffort();
		return ritorno;
	}
}