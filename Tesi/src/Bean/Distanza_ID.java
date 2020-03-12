package Bean;

public class Distanza_ID {
	
	//Dichiarazione delle variabili di istanza della classe
	private String identificativo;
	private float distance;
	private int index_project;
	private int accoppiata;
	private Progetto validate;
	
	public Distanza_ID(String i, float dist, int ind, int acc) {
		identificativo= i;
		distance= dist;
		index_project= ind;
		this.accoppiata= acc;
	}
	
	public Distanza_ID(String i, float dist, Progetto pV) {
		this.identificativo= i;
		this.distance= dist;
		this.validate= pV;
	}

	public Progetto getValidate() {
		return validate;
	}

	public void setValidate(Progetto validate) {
		this.validate = validate;
	}

	public int getAccoppiata() {
		return accoppiata;
	}

	public void setAccoppiata(int accoppiata) {
		this.accoppiata = accoppiata;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	//Calcolo del metodo toString
	public String toStrin() {
		String s= "Identificativo distanza: " + getIdentificativo() + " Distanza: " + getDistance() + " Indice: " + getIndex_project();
		return s;
	}

	public int getIndex_project() {
		return index_project;
	}

	public void setIndex_project(int index_project) {
		this.index_project = index_project;
	}
}