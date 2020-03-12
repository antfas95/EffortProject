package Bean;

import java.awt.DisplayMode;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JOptionPane;
//import org.omg.CORBA.portable.ApplicationException;

/*
 * Questa classe mantiene l'insieme di tutti i progetti che sono presenti all'interno del file
 * di lettura selezionato, con la possibilità di svolgere le varie operazioni sui progetti.
 */
public class GestoreStruttura {

	//Dichiarazione delle variabili di istanza della classe
	private ArrayList<Progetto> progetti_presenti;
	private ArrayList<String> nomi_variabili;
	private ArrayList<Variabile> variabili_indicate;
	private ArrayList<Variabile> variabili_x_progetto;

	//Inserimento delle variabili di istanza che permettono di effettuare la validazione dei progetti presenti
	private ArrayList<Progetto> progetti_daTestare;
	private ArrayList<Progetto> progetti_Validation;
	//Questi due parametri successivi rappresentano i due array sul quale calcolare i vari modelli
	private ArrayList<Progetto> progetti_daTestare_Def;
	private ArrayList<Progetto> progetti_Validation_Def;
	private ArrayList<Integer> pesi_analisi;
	private int variabile_Minkowski;

	//Inserimento delle variabile che permettono di elaborare e fornire le varie distanze
	private ArrayList<Float> distanze_progetto;
	private ArrayList<Progetto> progetti_ritorno;
	
	//Variabili di istanza per la divisione dei progetti in set
	private FileOutputStream output;
	private PrintStream print;

	//Costruttore della classe
	public GestoreStruttura() {
		progetti_presenti= new ArrayList<>();
		nomi_variabili= new ArrayList<>();
		variabili_indicate= new ArrayList<>();
		progetti_daTestare_Def= new ArrayList<>();
		progetti_Validation_Def= new ArrayList<>();
	}

	//Metodo che ritorna il nome delle variabili, cioè il suo insieme
	public ArrayList<String> nomi_variabili(){
		return nomi_variabili;
	}

	//Metodo che permette di ritornare il set di progetti definitivi sul quale applicare la funzionalità di testing
	public ArrayList<Progetto> ritorna_Progetti_Testing_Definitivi(){
		effettuaSeconda_divisione_Progetti();
		return progetti_daTestare_Def;
	}
	
	//Metodo che ritorna i pesi che sono stati inseriti per il calcolo del modello
	public ArrayList<Integer> ritorna_pesi_Analisi(){
		return pesi_analisi;
	}
	
	//Metodo che ritorna il valore di p in Minkowski inserito per il calcolo del modello
	public int ritorna_variabile_Minkowski() {
		return variabile_Minkowski;
	}

	//Metodo che ritorna il set di progetti utile per la costruzione del modello
	public ArrayList<Progetto> ritorna_Progetti_Definitivi_Validation(){
		return progetti_Validation_Def;
	}

	//Metodo che permette di ritornare l'insieme dei progetti presenti all'interno dei progetti da testare
	public ArrayList<Progetto> ritorna_Progetto_Testing(){
		effettua_divisione_Progetti();
		return progetti_daTestare;
	}

	public ArrayList<Progetto> get_Progetti_Test(){
		return progetti_daTestare;
	}

	//Metodo che permette di settare i metodo da Testare
	public void set_Progetti_Tester(ArrayList<Progetto> progetti) {
		this.progetti_daTestare= progetti;
	}

	//Metodo che permette di ritornare l'insieme dei progetti all'interno dei progetti di validazione
	public ArrayList<Progetto> ritorna_Progetto_Validation(){
		return progetti_Validation;
	}

	//Metodo che permette di ritornare un progetto secondo il suo identificativo
	public Progetto ritorna_Progetto_Secondo_Id(int id) {
		return progetti_presenti.get(id);
	}
	
	//Metodo che permette di ritornare i progetti presenti all'interno della Struttura
	public ArrayList<Progetto> ritorna_Progetti_Presenti(){
		return progetti_presenti;
	}

	//Dichiarazione del metodo che permette di leggere qualsiasi File! Purchè esso sia un arttff
	public void carica_struttura() {
		String nome_file= JOptionPane.showInputDialog("Inserisci il nome del file", "progettiRif");
		System.out.println(nome_file);

		try {
			//Leggo il file che viene dato in input dall'utente
			FileReader reader= new FileReader(nome_file);
			//Inserisco il riferimento che consente l'operazione di lettura
			Scanner in= new Scanner(reader);

			//Inizio la lettura del file (sapendo che è un file formato: ARFF)
			while (in.hasNextLine()) {
				//Contino la lettura fin quando non incontro @data ogni nome rappresenterà il nome della variabile
				String valore= in.nextLine();
				System.out.println(valore);

				if(valore.equals("@data")) {
					//Finisco l'attività del while
					break;
				}else {
					if(valore.equals("")) {
						//nulla
					}else {
						int minimo= 11;
						valore= valore.substring(minimo, valore.length());
						String[] tokens= valore.split(" ");
						System.out.println("Da tokens: " + tokens[0]);
						nomi_variabili.add(tokens[0]);
					}
				}
			}

			//Da questo punto in questa variabile saranno presenti tutti gli attributi che possiede un determinato progetto
			nomi_variabili.remove(0);

			//Da questo punto in poi devo assegnare il valore per ogni variabile presente all'interno di nomi_variabili
			while(in.hasNextLine()) {
				variabili_x_progetto= new ArrayList<>();
				String variabile_studio= in.nextLine();
				System.out.println("Variabile di studio: " + variabile_studio);
				int minimo= 0;
				int massimo= 0;
				int contatore= 0;

				Progetto p= new Progetto();
				for (int i= 0; i<variabile_studio.length(); i++) {
					if (variabile_studio.charAt(i)==',') {
						int esempio= nomi_variabili.size()-1;
						String riferimento= variabile_studio.substring(minimo, massimo);
						float valore_variabile= Float.parseFloat(riferimento);
						Variabile nuova= new Variabile(nomi_variabili.get(contatore), valore_variabile);
						variabili_indicate.add(nuova);
						variabili_x_progetto.add(nuova);
						contatore++;
						if(contatore==nomi_variabili.size()-1) {
							//In questo caso devo leggere anche l'ultima variabile all'interno del file
							minimo= i+1;
							massimo= variabile_studio.length();
							String valore= variabile_studio.substring(minimo, massimo);
							float valore_finale= Float.parseFloat(valore);
							Variabile finale= new Variabile("Effort", valore_finale);
							variabili_indicate.add(finale);
							variabili_x_progetto.add(finale);
							System.out.println("Valore finale nome: " + finale.getNome() + " " + finale.getValore());
						}
						minimo= i+1;
						massimo= i+1;
					}else {
						massimo= i+1;
					}
				}

				p.setVariabili_progetto(variabili_x_progetto);
				progetti_presenti.add(p);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Progetto> getProgetti_presenti() {
		return progetti_presenti;
	}

	public void setProgetti_presenti(ArrayList<Progetto> progetti_presenti) {
		this.progetti_presenti = progetti_presenti;
	}

	public ArrayList<String> getNomi_variabili() {
		return nomi_variabili;
	}

	public void setNomi_variabili(ArrayList<String> nomi_variabili) {
		this.nomi_variabili = nomi_variabili;
	}

	public ArrayList<Variabile> getVariabili_indicate() {
		return variabili_indicate;
	}

	public void setVariabili_indicate(ArrayList<Variabile> variabili_indicate) {
		this.variabili_indicate = variabili_indicate;
	}

	public ArrayList<Variabile> ritornaProgetto_By_Id(int id) {
		return progetti_presenti.get(id).getVariabili_progetto();
	}

	//Metodo che mi ritorna l'array di tutte le distanze presenti nel progetto
	public ArrayList<Float> ritorna_distanze(){
		ArrayList<Float> inter_dist= new ArrayList<>();
		for (Progetto p: progetti_presenti) {
			float valore= p.getDistance();
			inter_dist.add(valore);
		}
		return inter_dist;
	}

	public ArrayList<Float> ritorna_distanze(ArrayList<Progetto> passati){
		ArrayList<Float> inter_dist= new ArrayList<>();
		for (Progetto p: passati) {
			float valore= p.getDistance();
			inter_dist.add(valore);
		}
		return inter_dist;
	}

	//Metodo che mi ritorna l'array di tutte le distanze presenti nel progetto
	public ArrayList<Float> ritorna_distanze_daTestare(){
		ArrayList<Float> inter_dist= new ArrayList<>();
		for (Progetto p: progetti_daTestare_Def) {
			float valore= p.getDistance();
			inter_dist.add(valore);
		}
		return inter_dist;
	}

	//Metodo che permette di ritornare la media di un set di progetti passati come parametro
	public float calcola_media(ArrayList<Progetto> progetti) {
		float media= 0;
		ArrayList<Variabile> vars;
		for (Progetto p: progetti) {
			vars= new ArrayList<>();
			vars= p.getVariabili_progetto();
			for (Variabile v: vars) {
				if (v.getNome().equals("Effort")) {
					media+= v.getValore();
				}
			}
		}
		return media/progetti.size();
	}

	//Metodo che permette di ritornare la mediana di un set di progetti passati come paramentro
	public float calcola_Mediana (ArrayList<Progetto> progetti) {

		ArrayList<Float> valori_effort= new ArrayList<>();
		ArrayList<Variabile> vars1= new ArrayList<>();

		for (Progetto p: progetti) {
			vars1= p.getVariabili_progetto();
			for (Variabile v: vars1) {
				if (v.getNome().equals("Effort")) {
					valori_effort.add(v.getValore());
				}
			}
		}

		Collections.sort(valori_effort);

		float mediana= 0;
		if (progetti.size()%2==0) {
			//Verifico se il risultato è pari in questo caso la media dei progetti centrali
			float variabile1= valori_effort.get(valori_effort.size()/2-1);
			float vaiabile2= valori_effort.get(valori_effort.size()/2);

			mediana= vaiabile2+variabile1;

			return mediana/2;
		}else {
			System.out.println("Il caso è dispari");
			mediana= valori_effort.get((int) (valori_effort.size()/2+0.5));
			return mediana;
		}
	}

	/*
	 * Metodo che decreta l'attività dell'Inverse Rank Mean, rappresenta un calcolo dell'effort dove viene preso il progetto che 
	 * possiede un analogia più vicina al progetto selezionato e viene dato peso k (numero delle analogie) al successivo
	 * peso k-1, fino add arrivare all'ultimo che avrà peso 1.
	 */
	public float calcola_Inverse_Rank_Mean(ArrayList<Progetto> progetti) {

		//Variabile di ritorno del metodo
		float i_r_m= 0;

		/*
		 * I progetti che sono passati come argomento a questo metodo già risultano essere in ordine secondo la loro analogia
		 * per questo motivo basta applicare direttamente il peso per ogni progetto sull'effort del progetto stesso
		 */

		int pesi_progetto= progetti.size();
		int divisore= 0;

		for (Progetto p: progetti) {
			ArrayList<Variabile> vars= new ArrayList<>();
			vars= p.getVariabili_progetto();
			for (Variabile v: vars) {
				if (v.getNome().equals("Effort")) {
					float valore= v.getValore()*pesi_progetto;
					divisore+=pesi_progetto;
					pesi_progetto--;
					i_r_m+=valore;
				}
			}
		}

		return i_r_m/divisore;
	}

	/*
	 * Metodo che permette di ritornare un array di interi rappresentato da numeri casuali in base alla size dei progetti che sono
	 * presenti all'interno del file, utili poi per la divisione dei progetti in due Array validazione e tester
	 */
	public ArrayList<Integer> array_Random(){

		ArrayList<Integer> array_random= new ArrayList<>();
		int grandezza= progetti_presenti.size();
		int contatore= 0;

		while (contatore<grandezza) {
			int valore= (int) (grandezza*Math.random());
			boolean controllo=false;

			if (array_random.size()==0) {
				System.out.println("Aggiunto un nuovo valore: " + valore);
				array_random.add(valore);
				contatore++;
			}else {
				for (int i=0; i<array_random.size(); i++) {
					if (array_random.get(i)==valore) {
						controllo=true;
					}
				}
			}
			if (controllo==false) {
				System.out.println("Aggiunto un nuovo valore: " + valore);
				array_random.add(valore);
				contatore++;
			}
		}

		return array_random;
	}

	/*
	 * Metodo che permette di ritornare un array di interi rappresentato da numeri casuali in base alla size dei progetti che sono
	 * presenti all'interno del file, utili poi per la divisione dei progetti in due Array validazione e tester
	 */
	public ArrayList<Integer> array_RandomSecondaIterazione(){

		ArrayList<Integer> array_random= new ArrayList<>();
		int grandezza= progetti_daTestare.size();
		int contatore= 0;

		while (contatore<grandezza) {
			int valore= (int) (grandezza*Math.random());
			boolean controllo=false;

			if (array_random.size()==0) {
				System.out.println("Aggiunto un nuovo valore: " + valore);
				array_random.add(valore);
				contatore++;
			}else {
				for (int i=0; i<array_random.size(); i++) {
					if (array_random.get(i)==valore) {
						controllo=true;
					}
				}
			}
			if (controllo==false) {
				System.out.println("Aggiunto un nuovo valore: " + valore);
				array_random.add(valore);
				contatore++;
			}
		}

		return array_random;
	}

	/*
	 * Creato il metodo che permette di effettuare il Random dei progetti adesso serve un altro che permetta di dividere il set
	 * dei progetti in due set di validazione e per il testing con la possibilià all'utente di poter scegliere la percentuale
	 * per la divisione dei progetti
	 */
	public void effettua_divisione_Progetti() {
		progetti_daTestare= new ArrayList<>();
		progetti_Validation= new ArrayList<>();

		String valore= JOptionPane.showInputDialog("Digita la percentuale che permetterà la divisione del set di progetti: ");
		float percentuale= Float.parseFloat(valore);
		int indice_considerare= (int) (progetti_presenti.size()*percentuale/100);
		System.out.println("Indice ottenuto: " + indice_considerare);
		ArrayList<Integer> interi= array_Random();
		int i=0;
		for (; i<indice_considerare; i++) {
			Progetto progetto= ritorna_Progetto_Secondo_Id(interi.get(i));
			progetti_daTestare.add(progetto);
		}

		for (; i<progetti_presenti.size(); i++) {
			Progetto progetto= ritorna_Progetto_Secondo_Id(interi.get(i));
			progetti_Validation.add(progetto);
		}
	}

	/*
	 * Metodo che permette di effettuare una seconda divizione dei paramentri ovviamente non farà più riferimento all'intero
	 * set dei progetti che sono presenti all'interno della struttura, ma farà riferimento a un parametro rappresentato da un array
	 * di progetti che devono essere divisi in altri due set, appunto il primo dedicato proprio per la validazione definitiva,
	 * il secondo invece per la validazione.
	 */
	public void effettuaSeconda_divisione_Progetti() {
		progetti_daTestare_Def= new ArrayList<>();
		progetti_Validation_Def= new ArrayList<>();

		String valore= JOptionPane.showInputDialog("Digita la percentuale che permetterà la divisione del set di progetti: ");
		float percentuale= Float.parseFloat(valore);
		int indice_considerare= (int) (progetti_daTestare.size()*percentuale/100);
		System.out.println("Indice ottenuto: " + indice_considerare);
		ArrayList<Integer> interi= array_RandomSecondaIterazione();
		int i=0;
		for (; i<indice_considerare; i++) {
			Progetto progetto= ritorna_Progetto_Secondo_Id(interi.get(i));
			progetti_daTestare_Def.add(progetto);
		}

		for (; i<progetti_daTestare.size(); i++) {
			Progetto progetto= ritorna_Progetto_Secondo_Id(interi.get(i));
			progetti_Validation_Def.add(progetto);
		}
	}

	/*
	 * Inserimento all'interno della classe del metodo che permette di effettuare il calcolo di un determinato modello, noi abbiamo
	 * due set di progetti (training, validation), bisogna iniziare con un k=1, trovare per ogni progetto presente nel set 
	 * di validation quello che si avvicina maggiormente, che garantirà nella costruzione del nostro modello
	 */
	public ArrayList<Modello> calcola_Modello(ArrayList<Progetto> testing, ArrayList<Progetto> validate) {
		ArrayList<Modello> modelli_risultanti= new ArrayList<>();
		float effort_progetto = 0;
		float effort_media = 0;
		float effort_mediana = 0;
		float effort_IRM = 0;

		//Inizio del calcolo dei modelli per la distanza di Manhattan
		for (int i=1; i<=5; i++) {
			//Questo for permette di prendere singolarmente un progetto che è presente all'interno dei progetti validation
			for (Progetto p: validate) {
				ArrayList<Variabile> vars= p.getVariabili_progetto();
				for (Variabile v: vars) {
					if (v.getNome().equals("Effort")) {
						effort_progetto= v.getValore();
					}
				}
				//Nell'array di ritorno saranno presenti i progetti con una distanza più simile
				ArrayList<Progetto> ritorno= calcola_distanza_Manhattan(vars, i, testing);
				//Requisiti fondamentali per la costruzione del modello
				float media= calcola_media(ritorno);
				float mediana= calcola_Mediana(ritorno);
				float IRM= calcola_Inverse_Rank_Mean(ritorno);
				//Effetto solo comulativo poi si deve dividere l'effort selezionato per il numero dei progetti ottenuti
				effort_media+= Math.abs(effort_progetto-media);
				effort_mediana+= Math.abs(effort_progetto-mediana);
				effort_IRM+= Math.abs(effort_progetto-IRM);
			}
			Modello m= new Modello(i, "Manhattan", "Media", effort_media/progetti_Validation_Def.size());
			Modello m1= new Modello(i, "Manhattan", "Mediana", effort_mediana/progetti_Validation_Def.size());
			Modello m2= new Modello(i, "Manhattan", "IRM", effort_IRM/progetti_Validation_Def.size());
			modelli_risultanti.add(m);
			modelli_risultanti.add(m1);
			modelli_risultanti.add(m2);
		}

		//Il for più esterno serve ad incrementare il valore del k ad ogni iterazione (calcolo i modelli per distanza euclidea)
		for (int i=1; i<=5; i++) {
			//Questo for permette di prendere singolarmente un progetto che è presente all'interno dei progetti validation
			for (Progetto p: validate) {
				ArrayList<Variabile> vars= p.getVariabili_progetto();
				for (Variabile v: vars) {
					if (v.getNome().equals("Effort")) {
						effort_progetto= v.getValore();
					}
				}
				//Nell'array di ritorno saranno presenti i progetti con una distanza più simile
				ArrayList<Progetto> ritorno= distanza_euclidea_semplice(vars, i, testing);
				//Requisiti fondamentali per la costruzione del modello
				float media= calcola_media(ritorno);
				float mediana= calcola_Mediana(ritorno);
				float IRM= calcola_Inverse_Rank_Mean(ritorno);
				//Effetto solo comulativo poi si deve dividere l'effort selezionato per il numero dei progetti ottenuti
				effort_media+= Math.abs(effort_progetto-media);
				effort_mediana+= Math.abs(effort_progetto-mediana);
				effort_IRM+= Math.abs(effort_progetto-IRM);
			}
			Modello m= new Modello(i, "Euclidea", "Media", effort_media/testing.size());
			Modello m1= new Modello(i, "Euclidea", "Mediana", effort_mediana/testing.size());
			Modello m2= new Modello(i, "Euclidea", "IRM", effort_IRM/testing.size());
			modelli_risultanti.add(m);
			modelli_risultanti.add(m1);
			modelli_risultanti.add(m2);
		}


		//Inizio del calcolo dei modelli per la distanza euclidea Ponderata
		ArrayList<Integer> pesi= new ArrayList<>();
		for(Variabile v: progetti_daTestare.get(0).getVariabili_progetto()) {
			int valore= Integer.parseInt(JOptionPane.showInputDialog("Digita il peso della variabile: " + v.getNome()));
			pesi.add(valore);
		}
		for (int i=1; i<=5; i++) {
			//Questo for permette di prendere singolarmente un progetto che è presente all'interno dei progetti validation
			for (Progetto p: progetti_Validation_Def) {
				ArrayList<Variabile> vars= p.getVariabili_progetto();
				for (Variabile v: vars) {
					if (v.getNome().equals("Effort")) {
						effort_progetto= v.getValore();
					}
				}
				//Nell'array di ritorno saranno presenti i progetti con una distanza più simile
				ArrayList<Progetto> ritorno= distanza_euclidea_ponderata(vars, i, pesi, testing);
				//Requisiti fondamentali per la costruzione del modello
				float media= calcola_media(ritorno);
				float mediana= calcola_Mediana(ritorno);
				float IRM= calcola_Inverse_Rank_Mean(ritorno);
				//Effetto solo comulativo poi si deve dividere l'effort selezionato per il numero dei progetti ottenuti
				effort_media+= Math.abs(effort_progetto-media);
				effort_mediana+= Math.abs(effort_progetto-mediana);
				effort_IRM+= Math.abs(effort_progetto-IRM);
			}
			Modello m= new Modello(i, "Euclidea Ponderata", "Media", effort_media/progetti_Validation_Def.size());
			Modello m1= new Modello(i, "Euclidea Ponderata", "Mediana", effort_mediana/progetti_Validation_Def.size());
			Modello m2= new Modello(i, "Euclidea Ponderata", "IRM", effort_IRM/progetti_Validation_Def.size());
			modelli_risultanti.add(m);
			modelli_risultanti.add(m1);
			modelli_risultanti.add(m2);
		}

		//Inizio del calcolo dei modelli per la distanza di Minkowski
		int p_mink= Integer.parseInt(JOptionPane.showInputDialog("Digita p (parametro Minkowski): "));

		for (int i=1; i<=5; i++) {
			//Questo for permette di prendere singolarmente un progetto che è presente all'interno dei progetti validation
			for (Progetto p: progetti_Validation_Def) {
				ArrayList<Variabile> vars= p.getVariabili_progetto();
				for (Variabile v: vars) {
					if (v.getNome().equals("Effort")) {
						effort_progetto= v.getValore();
					}
				}
				//Nell'array di ritorno saranno presenti i progetti con una distanza più simile
				ArrayList<Progetto> ritorno= distanza_Minkowski(vars, i, p_mink, testing);
				//Requisiti fondamentali per la costruzione del modello
				float media= calcola_media(ritorno);
				float mediana= calcola_Mediana(ritorno);
				float IRM= calcola_Inverse_Rank_Mean(ritorno);
				//Effetto solo comulativo poi si deve dividere l'effort selezionato per il numero dei progetti ottenuti
				effort_media+= Math.abs(effort_progetto-media);
				effort_mediana+= Math.abs(effort_progetto-mediana);
				effort_IRM+= Math.abs(effort_progetto-IRM);
			}
			Modello m= new Modello(i, "Minkowski", "Media", effort_media/progetti_Validation_Def.size());
			Modello m1= new Modello(i, "Minkowski", "Mediana", effort_mediana/progetti_Validation_Def.size());
			Modello m2= new Modello(i, "Minkowski", "IRM", effort_IRM/progetti_Validation_Def.size());
			modelli_risultanti.add(m);
			modelli_risultanti.add(m1);
			modelli_risultanti.add(m2);
		}

		for (Modello m: modelli_risultanti) {
			System.out.println(m.toString());
		}
		return modelli_risultanti;
	}

	//Metodo che permette di svolgere l'operazione della distanza euclidea semplice
	public ArrayList<Progetto> distanza_euclidea_semplice (ArrayList<Variabile> selezionate_utente, int k, ArrayList<Progetto> testing){

		progetti_ritorno= new ArrayList<>();
		distanze_progetto= new ArrayList<>();

		System.out.println("Inizio la procedura calcolo della distanza Euclidea");
		/*
		 * Prendo per ogni progetto le sue variabili di riferimento e salvo in un array ausiliario le variabili selezionate
		 * con quelle riscontrate in ogni progetto.
		 * 
		 * Tramite l'operazione di indice riesco a trovare i valore per ogni progetto e l'assegno alla variabile di istanza 
		 * distance che è presente all'interno del file
		 */

		for (Progetto p: testing) {
			float distanza= 0;
			ArrayList<Variabile> vars_progetto= new ArrayList<>();
			vars_progetto= p.getVariabili_progetto();

			int index_fine= selezionate_utente.size();
			for (int i=0; i<index_fine; i++) {
				distanza+= (float) Math.pow(vars_progetto.get(i).getValore()-selezionate_utente.get(i).getValore(), 2);
			}

			distanza= (float) Math.sqrt(distanza);
			p.setDistance(distanza);
			distanze_progetto.add(distanza);
			System.out.println(testing.indexOf(p) + " Valore Distanza associata: " + distanza);
		}

		//Ritorno l'insieme delle distanze che si sono calcolate e inizio l'ordinamento dell'array
		//distanze_progetto= ritorna_distanze(progetti_daTestare_Def);

		for (Float f: distanze_progetto) {
			System.out.println("Ordinata: " + f);
		}

		Collections.sort(distanze_progetto);

		//Devo prendere solo i primi k elementi dell'arrayOrdinato
		ArrayList<Float> solo_k= new ArrayList<>();

		for (int i= 0; i<k; i++) {
			solo_k.add(distanze_progetto.get(i));
			System.out.println("Solo k: " + distanze_progetto.get(i));
		}

		//Adesso posso ritornare i progetti che trovano riscontro con la distanza
		for (int f=0; f<solo_k.size(); f++) {
			float valore_confronto= solo_k.get(f);
			System.out.println("Esamino i valori float presenti nell'array solo k");
			for (Progetto p: testing) {
				float distance= p.getDistance();
				//System.out.println(distance);

				if (distance==valore_confronto) {
					System.out.println("Aggiungo il progetto agli elementi di ritorno");
					progetti_ritorno.add(p);
					//appoggio.remove(p);
					break;
				}
			}
		}

		//appoggio= progetti_daTestare_Def;

		for (Progetto pi: progetti_ritorno) {
			ArrayList<Variabile> esempio= new ArrayList<>();
			esempio= pi.getVariabili_progetto();
			System.out.println(esempio + "Distanza: " + pi.getDistance());
		}

		return progetti_ritorno;
	}

	//Metodo che permette di svolgere l'operazione della distanza euclidea semplice
	public ArrayList<Progetto> distanza_euclidea_ponderata (ArrayList<Variabile> selezionate_utente, int k, ArrayList<Integer> pesi, ArrayList<Progetto> testing){

		progetti_ritorno= new ArrayList<>();
		distanze_progetto= new ArrayList<>();

		System.out.println("Inizio la procedura calcolo della distanza Euclidea Ponderata");
		/*
		 * Prendo per ogni progetto le sue variabili di riferimento e salvo in un array ausiliario le variabili selezionate
		 * con quelle riscontrate in ogni progetto.
		 * 
		 * Tramite l'operazione di indice riesco a trovare i valore per ogni progetto e l'assegno alla variabile di istanza 
		 * distance che è presente all'interno del file
		 */

		for (Progetto p: testing) {
			float distanza= 0;
			ArrayList<Variabile> vars_progetto= new ArrayList<>();
			vars_progetto= p.getVariabili_progetto();
			ArrayList<Variabile> iterate= new ArrayList<>();
			for (Variabile vara: vars_progetto) {
				for (Variabile sel: selezionate_utente) {
					if (sel.getNome().equals(vara.getNome())) {
						iterate.add(vara);
					}
				}
			}

			int index_fine= selezionate_utente.size();
			for (int i=0; i<index_fine; i++) {
				distanza+= (float) pesi.get(i)*(Math.pow(iterate.get(i).getValore()-selezionate_utente.get(i).getValore(), 2));
			}

			distanza= (float) Math.sqrt(distanza);
			p.setDistance(distanza);
			distanze_progetto.add(distanza);
			System.out.println(testing.indexOf(p) + " Valore Distanza associata: " + distanza);
		}

		//Ritorno l'insieme delle distanze che si sono calcolate e inizio l'ordinamento dell'array
		//distanze_progetto= ritorna_distanze(progetti_daTestare_Def);

		Collections.sort(distanze_progetto);

		for (Float f: distanze_progetto) {
			System.out.println("Ordinata: " + f);
		}

		//Devo prendere solo i primi k elementi dell'arrayOrdinato
		ArrayList<Float> solo_k= new ArrayList<>();

		for (int i= 0; i<k; i++) {
			solo_k.add(distanze_progetto.get(i));
			System.out.println("Solo k: " + distanze_progetto.get(i));
		}

		//appoggio= progetti_daTestare_Def;

		//Adesso posso ritornare i progetti che trovano riscontro con la distanza
		for (int f=0; f<solo_k.size(); f++) {
			float valore_confronto= solo_k.get(f);
			System.out.println("Esamino i valori float presenti nell'array solo k");
			for (Progetto p: testing) {
				float distance= p.getDistance();
				//System.out.println(distance);

				if (distance==valore_confronto) {
					System.out.println("Aggiungo il progetto agli elementi di ritorno");
					progetti_ritorno.add(p);
					//appoggio.remove(p);
					break;
				}
			}
		}

		//appoggio= progetti_daTestare_Def;

		for (Progetto pi: progetti_ritorno) {
			ArrayList<Variabile> esempio= new ArrayList<>();
			esempio= pi.getVariabili_progetto();
			System.out.println(esempio + "Distanza: " + pi.getDistance());
		}

		return progetti_ritorno;
	}

	//Metodo che permette di svolgere l'operazione della distanza di Manhattan
	public ArrayList<Progetto> calcola_distanza_Manhattan (ArrayList<Variabile> selezionate_utente, int k, ArrayList<Progetto> testing){

		progetti_ritorno= new ArrayList<>();
		distanze_progetto= new ArrayList<>();

		System.out.println("Inizio la procedura calcolo della distanza Euclidea Ponderata");
		/*
		 * Prendo per ogni progetto le sue variabili di riferimento e salvo in un array ausiliario le variabili selezionate
		 * con quelle riscontrate in ogni progetto.
		 * 
		 * Tramite l'operazione di indice riesco a trovare i valore per ogni progetto e l'assegno alla variabile di istanza 
		 * distance che è presente all'interno del file
		 */

		for (Progetto p: testing) {
			float distanza= 0;
			ArrayList<Variabile> vars_progetto= new ArrayList<>();
			vars_progetto= p.getVariabili_progetto();
			ArrayList<Variabile> iterate= new ArrayList<>();
			for (Variabile vara: vars_progetto) {
				for (Variabile sel: selezionate_utente) {
					if (sel.getNome().equals(vara.getNome())) {
						iterate.add(vara);
					}
				}
			}

			int index_fine= selezionate_utente.size();
			for (int i=0; i<index_fine; i++) {
				distanza+= Math.abs((float) iterate.get(i).getValore()-selezionate_utente.get(i).getValore());
			}

			p.setDistance(distanza);
			System.out.println(testing.indexOf(p) + " Valore Distanza associata: " + distanza);
		}

		//Ritorno l'insieme delle distanze che si sono calcolate e inizio l'ordinamento dell'array
		distanze_progetto= ritorna_distanze(testing);

		Collections.sort(distanze_progetto);

		for (Float f: distanze_progetto) {
			System.out.println("Ordinata: " + f);
		}

		//Devo prendere solo i primi k elementi dell'arrayOrdinato
		ArrayList<Float> solo_k= new ArrayList<>();

		for (int i= 0; i<k; i++) {
			solo_k.add(distanze_progetto.get(i));
			System.out.println("Solo k: " + distanze_progetto.get(i));
		}

		//appoggio= progetti_daTestare_Def;

		//Adesso posso ritornare i progetti che trovano riscontro con la distanza
		for (int f=0; f<solo_k.size(); f++) {
			float valore_confronto= solo_k.get(f);
			System.out.println("Esamino i valori float presenti nell'array solo k");
			for (Progetto p: testing) {
				float distance= p.getDistance();
				//System.out.println(distance);

				if (distance==valore_confronto) {
					System.out.println("Aggiungo il progetto agli elementi di ritorno");
					progetti_ritorno.add(p);
					//appoggio.remove(p);
					break;
				}
			}
		}

		//appoggio= progetti_daTestare_Def;

		for (Progetto pi: progetti_ritorno) {
			ArrayList<Variabile> esempio= new ArrayList<>();
			esempio= pi.getVariabili_progetto();
			System.out.println(esempio + "Distanza: " + pi.getDistance());
		}

		return progetti_ritorno;
	}

	//Metodo che permette di svolgere l'operazione della distanza di Minkowski
	public ArrayList<Progetto> distanza_Minkowski (ArrayList<Variabile> selezionate_utente, int k, int p_mink, ArrayList<Progetto> testing){

		progetti_ritorno= new ArrayList<>();
		distanze_progetto= new ArrayList<>();

		System.out.println("Inizio la procedura calcolo della distanza Euclidea Ponderata");
		/*
		 * Prendo per ogni progetto le sue variabili di riferimento e salvo in un array ausiliario le variabili selezionate
		 * con quelle riscontrate in ogni progetto.
		 * 
		 * Tramite l'operazione di indice riesco a trovare i valore per ogni progetto e l'assegno alla variabile di istanza 
		 * distance che è presente all'interno del file
		 */

		for (Progetto p: testing) {
			float distanza= 0;
			ArrayList<Variabile> vars_progetto= new ArrayList<>();
			vars_progetto= p.getVariabili_progetto();
			ArrayList<Variabile> iterate= new ArrayList<>();
			for (Variabile vara: vars_progetto) {
				for (Variabile sel: selezionate_utente) {
					if (sel.getNome().equals(vara.getNome())) {
						iterate.add(vara);
					}
				}
			}

			int index_fine= selezionate_utente.size();
			for (int i=0; i<index_fine; i++) {
				distanza+= Math.pow(Math.abs(iterate.get(i).getValore()-selezionate_utente.get(i).getValore()), p_mink);
			}

			distanza= (float) Math.pow(distanza, (double) 1/p_mink);
			p.setDistance(distanza);
			System.out.println(testing.indexOf(p) + " Valore Distanza associata: " + distanza);
		}

		//Ritorno l'insieme delle distanze che si sono calcolate e inizio l'ordinamento dell'array
		distanze_progetto= ritorna_distanze(testing);

		Collections.sort(distanze_progetto);

		for (Float f: distanze_progetto) {
			System.out.println("Ordinata: " + f);
		}

		//Devo prendere solo i primi k elementi dell'arrayOrdinato
		ArrayList<Float> solo_k= new ArrayList<>();

		for (int i= 0; i<k; i++) {
			solo_k.add(distanze_progetto.get(i));
			System.out.println("Solo k: " + distanze_progetto.get(i));
		}

		//appoggio= progetti_daTestare_Def;

		//Adesso posso ritornare i progetti che trovano riscontro con la distanza
		for (int f=0; f<solo_k.size(); f++) {
			float valore_confronto= solo_k.get(f);
			System.out.println("Esamino i valori float presenti nell'array solo k");
			for (Progetto p: testing) {
				float distance= p.getDistance();
				//System.out.println(distance);

				if (distance==valore_confronto) {
					System.out.println("Aggiungo il progetto agli elementi di ritorno");
					progetti_ritorno.add(p);
					//appoggio.remove(p);
					break;
				}
			}
		}

		//appoggio= progetti_daTestare_Def;

		for (Progetto pi: progetti_ritorno) {
			ArrayList<Variabile> esempio= new ArrayList<>();
			esempio= pi.getVariabili_progetto();
			System.out.println(esempio + "Distanza: " + pi.getDistance());
		}

		return progetti_ritorno;
	}
	
	//Metodo che permette di calcolare l'MRE dalla lettura del file SalvataggioRealePredetto
	public float calcola_Mre_By_File() {
		float f= 0;
		return f;
	}

	//Metodo che permette di calcolare l'MRE
	public float calcola_MRE(int indice, float effort_predetto) {
		float ritorno;
		float effort_progetto = 0;
		Progetto effort= progetti_Validation.get(indice-1);
		ArrayList<Variabile> vars= effort.getVariabili_progetto();
		for (Variabile v: vars) {
			if(v.getNome().equals("Effort")) {
				effort_progetto= v.getValore();
				System.out.println("Valore del progetto considerato: " + effort_progetto);
			}
		}
		ritorno= Math.abs(effort_progetto-effort_predetto)/effort_progetto;
		return ritorno;
	}

	//Metodo che permette il calcolo del MdRe
	public float calcola_MdRE(float predetto) {
		float ritorno = 0;
		for (Progetto p: progetti_Validation) {
			ArrayList<Variabile> vars= new ArrayList<>();
			vars= p.getVariabili_progetto();
			for (Variabile c: vars) {
				if (c.getNome().equals("Effort")) {
					float effort_progetto= c.getValore();
					ritorno+= Math.abs(effort_progetto-predetto)/effort_progetto;
				}
			}
		}
		return ritorno/progetti_Validation.size();
	}

	//Metodo che permette il calcolo del modello
	public ArrayList<Modello> calcola_modello1(ArrayList<Progetto> testing, ArrayList<Progetto> validate, ArrayList<Integer> pesi, int mink){
		ArrayList<Modello> modelli_risultato= new ArrayList<>();

		variabile_Minkowski= mink;
		
		for (int j=0; j<testing.size(); j++) {
			ArrayList<Variabile> vars_T= testing.get(j).getVariabili_progetto();
			int indice_validate= 0;
			for (Progetto pV: validate) {
				float distanza = 0;
				float distanzaE_p= 0;
				float distanzaMan= 0;
				float distanzaP_mink= 0;
				ArrayList<Variabile> vars_V= pV.getVariabili_progetto();
				for (int i=0; i<vars_V.size(); i++) {
					distanza+= (float) Math.pow(vars_T.get(i).getValore()-vars_V.get(i).getValore(), 2);
					distanzaE_p+= (float) pesi.get(i)*(Math.pow(vars_T.get(i).getValore()-vars_V.get(i).getValore(), 2));
					distanzaMan+=  Math.abs((float) vars_T.get(i).getValore()-vars_V.get(i).getValore()); 
					distanzaP_mink+= Math.pow(Math.abs(vars_T.get(i).getValore()-vars_V.get(i).getValore()), variabile_Minkowski);
				}
				distanza= (float) Math.sqrt(distanza);
				distanzaE_p= (float) Math.sqrt(distanzaE_p);
				distanzaP_mink= (float) Math.pow(distanzaP_mink, (double) 1/variabile_Minkowski);
				Distanza_ID dist= new Distanza_ID("Euclide", distanza, pV);
				Distanza_ID distEp= new Distanza_ID("Euclide Ponderata", distanzaE_p, pV);
				Distanza_ID distMan= new Distanza_ID("Manhattan", distanzaMan, pV);
				Distanza_ID distMink= new Distanza_ID("Minkowski", distanzaP_mink, pV);
				indice_validate++;
				Progetto p= testing.get(j);
				p.add_Distanza_ID(dist);
				p.add_Distanza_ID(distEp);
				p.add_Distanza_ID(distMan);
				p.add_Distanza_ID(distMink);
			}
		}

		System.out.println("Grandezza dell'array di validazione di riferimento: " + validate.size());
		int contatore= 0;
		Progetto esempio= testing.get(0);
		System.out.println(esempio.miaToString());
		ArrayList<Distanza_ID> distanze= new ArrayList<>();
		distanze= esempio.get_Distanze_ID_progetto();
		for (Distanza_ID i: distanze) {
			contatore++;
			System.out.println("Distanza: " + i.getIdentificativo() + "Valore: " + i.getAccoppiata());
		}
		System.out.println("Numero delle distanze ID per progetto: " + contatore);
		//Adesso trovare un metdo per poter trovare le distanze minima per ogni k e poi soprattutto iterare per ogni elemento
		//Per poi ritornare il modello più efficiente
		for (int k=1; k<=5; k++) {
			ArrayList<Float> array_euclide = new ArrayList<>();
			ArrayList<Float> array_euclide_ponderato= new ArrayList<>();
			ArrayList<Float> array_manh= new ArrayList<>();
			ArrayList<Float> array_mink= new ArrayList<>();
			ArrayList<Progetto> progetti_euclide= new ArrayList<>();
			ArrayList<Progetto> progetti_euclide_ponderato= new ArrayList<>();
			ArrayList<Progetto> progetti_Manhattan= new ArrayList<>();
			ArrayList<Progetto> progetti_Minkowski= new ArrayList<>();
			float media_cumulativa_euclide= 0;
			float media_cumulativa_euclide_ponderata= 0;
			float media_cumulativa_manhattan= 0;
			float media_cumulativa_minko= 0;
			float mediana_cumulativa_euclide= 0;
			float mediana_cumulativa_euclide_ponderata= 0;
			float mediana_cumulativa_manhattan= 0;
			float mediana_cumulativa_minko= 0;
			float IRM_cumulativa_euclide= 0;
			float IRM_cumulativa_euclide_ponderata= 0;
			float IRM_cumulativa_manhattan= 0;
			float IRM_cumulativa_minko= 0;
			float valore_effort_progetto= 0;
			for (Progetto p: testing) {
				ArrayList<Variabile> vars= new ArrayList<>();
				vars= p.getVariabili_progetto();
				for (Variabile v: vars) {
					if (v.getNome().equals("Effort")) {
						valore_effort_progetto= v.getValore();
					}
				}
				ArrayList<Distanza_ID> distanze_id= new ArrayList<>();
				distanze_id= p.get_Distanze_ID_progetto();
				for (Distanza_ID d: distanze_id) {
					if (d.getIdentificativo().equals("Euclide")) {
						array_euclide.add(d.getDistance());
					}else if(d.getIdentificativo().equals("Euclide Ponderata")) {
						array_euclide_ponderato.add(d.getDistance());
					}else if (d.getIdentificativo().equals("Manhattan")) {
						array_manh.add(d.getDistance());
					}else {
						array_mink.add(d.getDistance());
					}
				}
				
				
				//Effettuo l'ordinamente degli array che contengono al loro interno le distanze di riferimento
				Collections.sort(array_euclide);
				Collections.sort(array_euclide_ponderato);
				Collections.sort(array_manh);
				Collections.sort(array_mink);

				
				for (int i=0; i<k; i++) {
					float distanza_euclide= array_euclide.get(i);
					float distanza_euclide_ponderata= array_euclide_ponderato.get(i);
					float distanza_manhattan= array_manh.get(i);
					float distanza_minkowski= array_mink.get(i);

					for (Distanza_ID d: distanze_id) {
						if (d.getDistance()==distanza_euclide && d.getIdentificativo().equals("Euclide")) {
							progetti_euclide.add(d.getValidate());
						}else if (d.getDistance()==distanza_euclide_ponderata && d.getIdentificativo().equals("Euclide Ponderata")) {
							progetti_euclide_ponderato.add(d.getValidate());
						}else if (d.getDistance()==distanza_manhattan && d.getIdentificativo().equals("Manhattan")) {
							progetti_Manhattan.add(d.getValidate());
						}else if (d.getDistance()==distanza_minkowski && d.getIdentificativo().equals("Minkowski")){
							progetti_Minkowski.add(d.getValidate());
						}
					}
				}
				
				//Adesso per ogni array mantengo i progetti di riferimento sul quale calcolare adesso la media
				float media_euclidea= calcola_media(progetti_euclide);
				float media_euclidea_ponderata= calcola_media(progetti_euclide_ponderato);
				float media_Manhattan= calcola_media(progetti_Manhattan);
				float media_Minkowski= calcola_media(progetti_Minkowski);
				float mediana_euclidea= calcola_Mediana(progetti_euclide);
				float mediana_euclidea_ponderata= calcola_Mediana(progetti_euclide_ponderato);
				float mediana_manhattan= calcola_Mediana(progetti_Manhattan);
				float mediana_minko= calcola_Mediana(progetti_Minkowski);
				float IRM_euclidea= calcola_Inverse_Rank_Mean(progetti_euclide);
				float IRM_euclidea_ponderata= calcola_Inverse_Rank_Mean(progetti_euclide_ponderato);
				float IRM_manhattan= calcola_Inverse_Rank_Mean(progetti_Manhattan);
				float IRM_minko= calcola_Inverse_Rank_Mean(progetti_Minkowski);
				
				//Devo mantenere i risultati di queste medie comulativi
				media_cumulativa_euclide+= Math.abs(media_euclidea-valore_effort_progetto);
				media_cumulativa_euclide_ponderata+= Math.abs(media_euclidea_ponderata-valore_effort_progetto);
				media_cumulativa_manhattan+= Math.abs(media_Manhattan-valore_effort_progetto);
				media_cumulativa_minko+= Math.abs(media_Minkowski-valore_effort_progetto);
				mediana_cumulativa_euclide+= Math.abs(mediana_euclidea-valore_effort_progetto);
				mediana_cumulativa_euclide_ponderata+= Math.abs(mediana_euclidea_ponderata-valore_effort_progetto);
				mediana_cumulativa_manhattan+= Math.abs(mediana_manhattan-valore_effort_progetto);
				mediana_cumulativa_minko+= Math.abs(mediana_minko-valore_effort_progetto);
				IRM_cumulativa_euclide+= Math.abs(IRM_euclidea-valore_effort_progetto);
				IRM_cumulativa_euclide_ponderata+= Math.abs(IRM_euclidea_ponderata-valore_effort_progetto);
				IRM_cumulativa_manhattan+= Math.abs(IRM_manhattan-valore_effort_progetto);
				IRM_cumulativa_minko+= Math.abs(IRM_minko-valore_effort_progetto);
			}
			
			Modello m= new Modello(k, "Euclidea", "Media" , media_cumulativa_euclide/testing.size());
			Modello m1= new Modello(k, "Euclidea", "Mediana" , mediana_cumulativa_euclide/testing.size());
			Modello m2= new Modello(k, "Euclidea", "IRM" , IRM_cumulativa_euclide/testing.size());
			Modello m3= new Modello(k, "Euclidea Ponderata", "Media" , media_cumulativa_euclide_ponderata/testing.size());
			Modello m4= new Modello(k, "Euclidea Ponderata", "Mediana" , mediana_cumulativa_euclide_ponderata/testing.size());
			Modello m5= new Modello(k, "Euclidea Ponderata", "IRM" , IRM_cumulativa_euclide_ponderata/testing.size());
			Modello m6= new Modello(k, "Manhattan", "Media" , media_cumulativa_manhattan/testing.size());
			Modello m7= new Modello(k, "Manhattan", "Mediana" , mediana_cumulativa_manhattan/testing.size());
			Modello m8= new Modello(k, "Manhattan", "IRM" , IRM_cumulativa_manhattan/testing.size());
			Modello m9= new Modello(k, "Minkowski", "Media" , media_cumulativa_minko/testing.size());
			Modello m10= new Modello(k, "Minkowski", "Mediana" , mediana_cumulativa_minko/testing.size());
			Modello m11= new Modello(k, "Minkowski", "IRM" , IRM_cumulativa_minko/testing.size());
			modelli_risultato.add(m);
			modelli_risultato.add(m1);
			modelli_risultato.add(m2);
			modelli_risultato.add(m3);
			modelli_risultato.add(m4);
			modelli_risultato.add(m5);
			modelli_risultato.add(m6);
			modelli_risultato.add(m7);
			modelli_risultato.add(m8);
			modelli_risultato.add(m9);
			modelli_risultato.add(m10);
			modelli_risultato.add(m11);	
		}
		/*
		float somma_euclide_finale= 0;
		float somma_euclide_finale_Ponderata= 0;
		ArrayList<Float> distanze_Euclide= new ArrayList<>();
		ArrayList<Float> distanza_EuclideP= new ArrayList<>();
		for (Progetto p: testing) {
			ArrayList<Distanza_ID> valori= new ArrayList<>();
			valori= p.get_Distanze_ID_progetto();
			for (Distanza_ID dist1: valori) {
				if (dist1.getIdentificativo().equals("Euclide")) {
					distanze_Euclide.add(dist1.getDistance());
				}else if(dist1.getIdentificativo().equals("Euclide Ponderata")) {
					distanza_EuclideP.add(dist1.getDistance());
				}
			}

			//Effettuo l'ordinamento delle distanze del progetto
			Collections.sort(distanze_Euclide);
			Collections.sort(distanza_EuclideP);

			float elemento= distanze_Euclide.get(0);
			float elementoP= distanza_EuclideP.get(0); 

			int indice= 0;
			int indiceP= 0;
			for (Distanza_ID d: valori) {
				if (d.getIdentificativo().equals("Euclide") && d.getDistance()==elemento) {
					indice= d.getAccoppiata();
				}

				if (d.getIdentificativo().equals("Euclide Ponderata") && d.getDistance()==elementoP) {
					indiceP= d.getAccoppiata();
				}
			}

			Progetto vali= validate.get(indice);
			Progetto valiP= validate.get(indiceP);
			ArrayList<Variabile> vars= vali.getVariabili_progetto();
			ArrayList<Variabile> vars1= valiP.getVariabili_progetto();
			for (Variabile vs: vars) {
				if (vs.getNome().equals("Effort")) {
					somma_euclide_finale+= vs.getValore();
				}
			}

			for (Variabile vs: vars1) {
				if (vs.getNome().equals("Effort")) {
					somma_euclide_finale_Ponderata+= vs.getValore();
				}
			}
		}
		Modello m= new Modello(1, "Euclidea Ponderata", "Media", somma_euclide_finale/testing.size());
		Modello m1= new Modello(1, "Euclidea", "Mediana", somma_euclide_finale/testing.size());
		Modello m2= new Modello(1, "Euclidea", "IRM", somma_euclide_finale/testing.size());
		Modello m3= new Modello(1, "Euclidea Ponderata", "Media", somma_euclide_finale_Ponderata/testing.size());
		Modello m4= new Modello(1, "Euclidea Ponderata", "Mediana", somma_euclide_finale_Ponderata/testing.size());
		Modello m5= new Modello(1, "Euclidea Ponderata", "IRM", somma_euclide_finale_Ponderata/testing.size());
		modelli_risultato.add(m);
		modelli_risultato.add(m1);
		modelli_risultato.add(m2);
		modelli_risultato.add(m3);
		modelli_risultato.add(m4);
		modelli_risultato.add(m5);
		

		for (int k=1; k<=5; k++) {

		}
		*/
		/*
		float somma_euclide_finale_media= 0;
		float somma_euclide_finale_mediana= 0;
		float somma_euclide_finale_IRM= 0;
		float sommaEP= 0;
		float somma_medianaEP= 0;
		float somma_IRMP=0;
		ArrayList<Float> distanze_Euclide= new ArrayList<>();
		ArrayList<Float> distanza_EuclideP= new ArrayList<>();
		for (Progetto p: testing) {
			ArrayList<Distanza_ID> valori= new ArrayList<>();
			valori= p.get_Distanze_ID_progetto();
			for (Distanza_ID dist1: valori) {
				if (dist1.getIdentificativo().equals("Euclide")) {
					distanze_Euclide.add(dist1.getDistance());
				}else if(dist1.getIdentificativo().equals("Euclide Ponderata")) {
					distanza_EuclideP.add(dist1.getDistance());
				}
			}

			//Effettuo l'ordinamento delle distanze del progetto
			Collections.sort(distanze_Euclide);
			Collections.sort(distanza_EuclideP);

			ArrayList<Float> solo_ke= new ArrayList<>();
			ArrayList<Float> solo_kep= new ArrayList<>();
			for (int k=0; k<2; k++) {
				solo_ke.add(distanze_Euclide.get(k));
				solo_kep.add(distanza_EuclideP.get(k));
			}

			ArrayList<Progetto> euclide_prog= new ArrayList<>();
			ArrayList<Progetto> euclideP_prog= new ArrayList<>();
			for (Distanza_ID d: valori) {
				for (Float f: solo_ke) {
					if (d.getIdentificativo().equals("Euclide")&&d.getDistance()==f) {
						euclide_prog.add(validate.get(d.getIndex_project()));
					}
				}

				for (Float f: solo_kep) {
					if (d.getIdentificativo().equals("Euclide Ponderata")&&d.getDistance()==f) {
						euclideP_prog.add(validate.get(d.getIndex_project()));
					}
				}
			}

			float media_E= calcola_media(euclide_prog);
			float mediana_E= calcola_Mediana(euclide_prog);
			float IRM_E= calcola_Inverse_Rank_Mean(euclide_prog);
			float mediaEP= calcola_media(euclideP_prog);
			float medianaP= calcola_Mediana(euclideP_prog);
			float irmP= calcola_Inverse_Rank_Mean(euclideP_prog);
			somma_euclide_finale_media+= media_E;
			somma_euclide_finale_mediana+= mediana_E;
			somma_euclide_finale_IRM+= IRM_E;
			sommaEP+= mediaEP;
			somma_medianaEP+= medianaP;
			somma_IRMP+= irmP;
		}
		Modello m6= new Modello(1, "Euclidea Ponderata", "Media", somma_euclide_finale_media/testing.size());
		Modello m7= new Modello(1, "Euclidea", "Mediana", somma_euclide_finale_mediana/testing.size());
		Modello m8= new Modello(1, "Euclidea", "IRM", somma_euclide_finale_IRM/testing.size());
		Modello m9= new Modello(1, "Euclidea Ponderata", "Media", sommaEP/testing.size());
		Modello m10= new Modello(1, "Euclidea Ponderata", "Mediana", somma_medianaEP/testing.size());
		Modello m11= new Modello(1, "Euclidea Ponderata", "IRM", somma_IRMP/testing.size());
		modelli_risultato.add(m6);
		modelli_risultato.add(m7);
		modelli_risultato.add(m8);
		modelli_risultato.add(m9);
		modelli_risultato.add(m10);
		modelli_risultato.add(m11);
		 */
		/*
		//Adesso ho bisogno di prendere tutti i valori di euclide per un determinato progetto in testing
		float somma_euclide_finale= 0;
		ArrayList<Float> distanze= new ArrayList<>();
		for (Progetto p: testing) {
			ArrayList<Distanza_ID> valori= new ArrayList<>();
			valori= p.get_Distanze_ID_progetto();
			for (Distanza_ID dist1: valori) {
				if (dist1.getIdentificativo().equals("Euclide")) {
					distanze.add(dist1.getDistance());
				}
			}

			//Effettuo l'ordinamento delle distanze del progetto
			Collections.sort(distanze);

			float elemento= distanze.get(0);

			int indice= 0;
			for (Distanza_ID d: valori) {
				if (d.getDistance()==elemento) {
					indice= d.getAccoppiata();
				}
			}

			Progetto vali= validate.get(indice);
			ArrayList<Variabile> vars= vali.getVariabili_progetto();
			for (Variabile vs: vars) {
				if (vs.getNome().equals("Effort")) {
					somma_euclide_finale+= vs.getValore();
				}
			}
		}
		Modello m= new Modello(1, "Euclidea", "Media", somma_euclide_finale/testing.size());
		modelli_risultato.add(m);
		 */

		/*
		for (int i=2; i<testing.size(); i++) {
			array_euclide= new ArrayList<>();
			Progetto p= testing.get(i);
			ArrayList<Variabile> vars= p.getVariabili_progetto();
			ArrayList<Distanza_ID> distanze= p.get_Distanze_ID_progetto();
			for (Distanza_ID d: distanze) {
				if (d.getIdentificativo().equals("Euclide")) {
					array_euclide.add(d.getDistance());
				}
			}

			Collections.sort(array_euclide);

			System.out.println("Stampo il valore di effort massimo per il primo progetto: " + array_euclide.get(0));

			ArrayList<Float> solo_k= new ArrayList<>();

			for (int j=1; j<=5; i++) {
				for (int r=1; r<=j; r++) {
					solo_k.add(array_euclide.get(r-1));
				}


				ArrayList<Float> appoggio= new ArrayList<>();
				ArrayList<Progetto> valori= new ArrayList<>();
				appoggio= array_euclide;
				for (Float f: appoggio) {
					for (Distanza_ID id: distanze) {
						if (f==id.getDistance()) {
							Progetto riferimento= id.getAccoppiata();
							valori.add(riferimento);
						}
					}
				}

				float media= calcola_media(valori);
				float mediana= calcola_Mediana(valori);
				float IRM= calcola_Inverse_Rank_Mean(valori);
				comulativa_media+= media;
				comulativa_mediana+= mediana;
				comulativa_IRM+= IRM;
			}
		}
		 */
		//Devo prendere i riferimenti per ogni array creato e in base al k selezionato calcolare Media, Mediana, IRM
		/*
		float media= 0;
		for (int i=1; i<=5; i++) {
			float media1= 0; 
			float mediana1= 0;
			float IRM= 0;
			int avanzamento= 0;
			for (Progetto p: testing) {
				ArrayList<Distanza_ID> distanze= new ArrayList<>();
				distanze= p.get_Distanze_ID_progetto();
				for (Distanza_ID i1: distanze) {
					if(i1.getIdentificativo().equals("Euclide")) {
						array_euclide.add(i1.getDistance());
					}
				}

				Collections.sort(array_euclide);

				float somma_cumulativa= 0;
				ArrayList<Float> solo_k= new ArrayList<>();
				for (int j= 0; j<i; j++) {
					somma_cumulativa+= array_euclide.get(j);
					solo_k.add(array_euclide.get(j));
				}

				ArrayList<Progetto> appoggio= new ArrayList<>();
				appoggio= testing;
				ArrayList<Progetto> selezionati= new ArrayList<>();
				for (int x= 0; x<appoggio.size(); x++) {
					ArrayList<Distanza_ID> dist= new ArrayList<>();
					dist= testing.get(x).get_Distanze_ID_progetto();
					for (Distanza_ID d: dist) {
						float distanza= d.getDistance();
						for (Float f: solo_k) {
							if (distanza==f) {
								Progetto aggiungere= appoggio.get(x);
								appoggio.remove(x);
								selezionati.add(aggiungere);
							}
						}
					}
				}

				media1+= calcola_media(selezionati);
				mediana1+= calcola_Mediana(selezionati);
				IRM+= calcola_Inverse_Rank_Mean(selezionati);
			}

			Modello m1= new Modello(i, "Euclidea", "Media", media1/testing.size());
			Modello m2= new Modello(i, "Euclidea", "Mediana", mediana1/testing.size());
			Modello m3= new Modello(i, "Euclidea", "IRM", IRM/testing.size());
			modelli_risultato.add(m1);
			modelli_risultato.add(m2);
			modelli_risultato.add(m3);
		}

		for (Progetto p: testing) {
			ArrayList<Distanza_ID> distanze= new ArrayList<>();
			for (Distanza_ID i: distanze) {
				if (i.getIdentificativo().equals("Euclide")) {
					array_euclide.add(i.getDistance());
				}else if(i.getIdentificativo().equals("Euclide Ponderata")) {
					array_euclide_ponderato.add(i.getDistance());
				}else if (i.getIdentificativo().equals("Manhattan")) {
					array_manh.add(i.getDistance());
				}else {
					array_mink.add(i.getDistance());
				}
			}
		}
		//Inseriti i valori nei rispettivi array adesso effettuo il loro ordinamento
		 */
		return modelli_risultato;
	}
	
	/*
	 * Metodo che permette il calcolo di un determinato modello tenendo in considerazione la divisione del set dei progetti 
	 * di testing in più sets al quale viene applicato questo determinato metodo
	 */
	public ArrayList<Modello> calcola_modello_By_Sets(ArrayList<Progetto> testing, ArrayList<Progetto> validate, ArrayList<Integer> pesi, int p_mink){
		ArrayList<Modello> modelli_risultato= new ArrayList<>();

		for (int j=0; j<testing.size(); j++) {
			ArrayList<Variabile> vars_T= testing.get(j).getVariabili_progetto();
			int indice_validate= 0;
			for (Progetto pV: validate) {
				float distanza = 0;
				float distanzaE_p= 0;
				float distanzaMan= 0;
				float distanzaP_mink= 0;
				ArrayList<Variabile> vars_V= pV.getVariabili_progetto();
				for (int i=0; i<vars_V.size(); i++) {
					distanza+= (float) Math.pow(vars_T.get(i).getValore()-vars_V.get(i).getValore(), 2);
					distanzaE_p+= (float) pesi.get(i)*(Math.pow(vars_T.get(i).getValore()-vars_V.get(i).getValore(), 2));
					distanzaMan+=  Math.abs((float) vars_T.get(i).getValore()-vars_V.get(i).getValore()); 
					distanzaP_mink+= Math.pow(Math.abs(vars_T.get(i).getValore()-vars_V.get(i).getValore()), p_mink);
				}
				distanza= (float) Math.sqrt(distanza);
				distanzaE_p= (float) Math.sqrt(distanzaE_p);
				distanzaP_mink= (float) Math.pow(distanzaP_mink, (double) 1/p_mink);
				Distanza_ID dist= new Distanza_ID("Euclide", distanza, indice_validate, validate.indexOf(pV));
				Distanza_ID distEp= new Distanza_ID("Euclide Ponderata", distanzaE_p, indice_validate, validate.indexOf(pV));
				Distanza_ID distMan= new Distanza_ID("Manhattan", distanzaMan, indice_validate, validate.indexOf(pV));
				Distanza_ID distMink= new Distanza_ID("Minkowski", distanzaP_mink, indice_validate, validate.indexOf(pV));
				indice_validate++;
				Progetto p= testing.get(j);
				p.add_Distanza_ID(dist);
				p.add_Distanza_ID(distEp);
				p.add_Distanza_ID(distMan);
				p.add_Distanza_ID(distMink);
			}
		}

		//Adesso trovare un metdo per poter trovare le distanze minima per ogni k e poi soprattutto iterare per ogni elemento
		//Per poi ritornare il modello più efficiente
		for (int k=1; k<=5; k++) {
			ArrayList<Float> array_euclide = new ArrayList<>();
			ArrayList<Float> array_euclide_ponderato= new ArrayList<>();
			ArrayList<Float> array_manh= new ArrayList<>();
			ArrayList<Float> array_mink= new ArrayList<>();
			ArrayList<Progetto> progetti_euclide= new ArrayList<>();
			ArrayList<Progetto> progetti_euclide_ponderato= new ArrayList<>();
			ArrayList<Progetto> progetti_Manhattan= new ArrayList<>();
			ArrayList<Progetto> progetti_Minkowski= new ArrayList<>();
			ArrayList<Distanza_ID> distanze_id= new ArrayList<>();
			float media_cumulativa_euclide= 0;
			float media_cumulativa_euclide_ponderata= 0;
			float media_cumulativa_manhattan= 0;
			float media_cumulativa_minko= 0;
			float mediana_cumulativa_euclide= 0;
			float mediana_cumulativa_euclide_ponderata= 0;
			float mediana_cumulativa_manhattan= 0;
			float mediana_cumulativa_minko= 0;
			float IRM_cumulativa_euclide= 0;
			float IRM_cumulativa_euclide_ponderata= 0;
			float IRM_cumulativa_manhattan= 0;
			float IRM_cumulativa_minko= 0;
			float valore_effort_progetto= 0;
			for (Progetto p: testing) {
				ArrayList<Variabile> vars= new ArrayList<>();
				vars= p.getVariabili_progetto();
				for (Variabile v: vars) {
					if (v.getNome().equals("Effort")) {
						valore_effort_progetto= v.getValore();
					}
				}
				distanze_id= p.get_Distanze_ID_progetto();
				for (Distanza_ID d: distanze_id) {
					if (d.getIdentificativo().equals("Euclide")) {
						array_euclide.add(d.getDistance());
					}else if(d.getIdentificativo().equals("Euclide Ponderata")) {
						array_euclide_ponderato.add(d.getDistance());
					}else if (d.getIdentificativo().equals("Manhattan")) {
						array_manh.add(d.getDistance());
					}else {
						array_mink.add(d.getDistance());
					}
				}
				
				//Effettuo l'ordinamente degli array che contengono al loro interno le distanze di riferimento
				Collections.sort(array_euclide);
				Collections.sort(array_euclide_ponderato);
				Collections.sort(array_manh);
				Collections.sort(array_mink);

				for (int i=0; i<k; i++) {
					float distanza_euclide= array_euclide.get(i);
					float distanza_euclide_ponderata= array_euclide_ponderato.get(i);
					float distanza_manhattan= array_manh.get(i);
					float distanza_minkowski= array_mink.get(i);

					for (Distanza_ID d: distanze_id) {
						if (d.getDistance()==distanza_euclide && d.getIdentificativo().equals("Euclide")) {
							progetti_euclide.add(validate.get(d.getAccoppiata()));
						}else if (d.getDistance()==distanza_euclide_ponderata && d.getIdentificativo().equals("Euclide Ponderata")) {
							progetti_euclide_ponderato.add(validate.get(d.getAccoppiata()));
						}else if (d.getDistance()==distanza_manhattan && d.getIdentificativo().equals("Manhattan")) {
							progetti_Manhattan.add(validate.get(d.getAccoppiata()));
						}else if (d.getDistance()==distanza_minkowski && d.getIdentificativo().equals("Minkowski")){
							progetti_Minkowski.add(validate.get(d.getAccoppiata()));
						}
					}
				}

				//Adesso per ogni array mantengo i progetti di riferimento sul quale calcolare adesso la media
				float media_euclidea= calcola_media(progetti_euclide);
				float media_euclidea_ponderata= calcola_media(progetti_euclide_ponderato);
				float media_Manhattan= calcola_media(progetti_Manhattan);
				float media_Minkowski= calcola_media(progetti_Minkowski);
				float mediana_euclidea= calcola_Mediana(progetti_euclide);
				float mediana_euclidea_ponderata= calcola_Mediana(progetti_euclide_ponderato);
				float mediana_manhattan= calcola_Mediana(progetti_Manhattan);
				float mediana_minko= calcola_Mediana(progetti_Minkowski);
				float IRM_euclidea= calcola_Inverse_Rank_Mean(progetti_euclide);
				float IRM_euclidea_ponderata= calcola_Inverse_Rank_Mean(progetti_euclide_ponderato);
				float IRM_manhattan= calcola_Inverse_Rank_Mean(progetti_Manhattan);
				float IRM_minko= calcola_Inverse_Rank_Mean(progetti_Minkowski);

				//Devo mantenere i risultati di queste medie comulativi
				media_cumulativa_euclide+= Math.abs(media_euclidea-valore_effort_progetto);
				media_cumulativa_euclide_ponderata+= Math.abs(media_euclidea_ponderata-valore_effort_progetto);
				media_cumulativa_manhattan+= Math.abs(media_Manhattan-valore_effort_progetto);
				media_cumulativa_minko+= Math.abs(media_Minkowski-valore_effort_progetto);
				mediana_cumulativa_euclide+= Math.abs(mediana_euclidea-valore_effort_progetto);
				mediana_cumulativa_euclide_ponderata+= Math.abs(mediana_euclidea_ponderata-valore_effort_progetto);
				mediana_cumulativa_manhattan+= Math.abs(mediana_manhattan-valore_effort_progetto);
				mediana_cumulativa_minko+= Math.abs(mediana_minko-valore_effort_progetto);
				IRM_cumulativa_euclide+= Math.abs(IRM_euclidea-valore_effort_progetto);
				IRM_cumulativa_euclide_ponderata+= Math.abs(IRM_euclidea_ponderata-valore_effort_progetto);
				IRM_cumulativa_manhattan+= Math.abs(IRM_manhattan-valore_effort_progetto);
				IRM_cumulativa_minko+= Math.abs(IRM_minko-valore_effort_progetto);
			}
			
			Modello m= new Modello(k, "Euclidea", "Media" , media_cumulativa_euclide/testing.size());
			Modello m1= new Modello(k, "Euclidea", "Mediana" , mediana_cumulativa_euclide/testing.size());
			Modello m2= new Modello(k, "Euclidea", "IRM" , IRM_cumulativa_euclide/testing.size());
			Modello m3= new Modello(k, "Euclidea Ponderata", "Media" , media_cumulativa_euclide_ponderata/testing.size());
			Modello m4= new Modello(k, "Euclidea Ponderata", "Mediana" , mediana_cumulativa_euclide_ponderata/testing.size());
			Modello m5= new Modello(k, "Euclidea Ponderata", "IRM" , IRM_cumulativa_euclide_ponderata/testing.size());
			Modello m6= new Modello(k, "Manhattan", "Media" , media_cumulativa_manhattan/testing.size());
			Modello m7= new Modello(k, "Manhattan", "Mediana" , mediana_cumulativa_manhattan/testing.size());
			Modello m8= new Modello(k, "Manhattan", "IRM" , IRM_cumulativa_manhattan/testing.size());
			Modello m9= new Modello(k, "Minkowski", "Media" , media_cumulativa_minko/testing.size());
			Modello m10= new Modello(k, "Minkowski", "Mediana" , mediana_cumulativa_minko/testing.size());
			Modello m11= new Modello(k, "Minkowski", "IRM" , IRM_cumulativa_minko/testing.size());
			modelli_risultato.add(m);
			modelli_risultato.add(m1);
			modelli_risultato.add(m2);
			modelli_risultato.add(m3);
			modelli_risultato.add(m4);
			modelli_risultato.add(m5);
			modelli_risultato.add(m6);
			modelli_risultato.add(m7);
			modelli_risultato.add(m8);
			modelli_risultato.add(m9);
			modelli_risultato.add(m10);
			modelli_risultato.add(m11);
		}
		return modelli_risultato;
	}
	
	//Metodo che effettua la divisione del set dei progetti secondo un intero rappresentante dalla percentuale
	public ArrayList<Progetto> ritorna_Progetti_By_Percentuale(int per){
		ArrayList<Progetto> ritorno_progetti= new ArrayList<>();
		ArrayList<Integer> interi= new ArrayList<>();
		interi= array_Random();
		int indice_considerare= (int) (progetti_presenti.size()*66/100);
		indice_considerare= (int) indice_considerare*per/100;
		System.out.println("Indice ottenuto: " + indice_considerare);
		int i=0;
		for (; i<indice_considerare; i++) {
			Progetto progetto= ritorna_Progetto_Secondo_Id(interi.get(i));
			ritorno_progetti.add(progetto);
		}
		
		return ritorno_progetti;
	}
	
	/*
	 * Ritorna l'insieme dei progetti tenendo in considerazione un indice che indica la partenza e l'indice che indica la fine
	 */
	public ArrayList<Progetto> ritorna_Progetti_By_Inizio_Fine (int indice_inizio, int indice_fine){
		ArrayList<Progetto> progetti_ritorno= new ArrayList<>();
		
		return progetti_ritorno;
	}
	
	//Metodo che permette di memorizzare i Progetti per la divisione in set all'interno di un File
	public void memorizza_Valori_Progetto(ArrayList<Progetto> da_Dividere) throws FileNotFoundException {
		output= new FileOutputStream("Set_Progetti");
		print= new PrintStream(output);
		for (Progetto p: da_Dividere) {
			ArrayList<Variabile> vars= new ArrayList<>();
			vars= p.getVariabili_progetto();
			for (Variabile v: vars) {
				print.println(v.getValore());
			}
		}
	}
	
	/*
	 * Metodo che permette di creare un array di indice_inizio e indice_fine tenendo in considerazione il k preso e considerato
	 */
	public ArrayList<Integer> ritornaInizio_Fine (ArrayList<Progetto> testare, int k){
		ArrayList<Integer> ritorno= new ArrayList<>();
		
		int bound= testare.size()/k;
		
		int indice_inizio= 0;
		int indice_fine= 0;
		
		for (int i=0; i<k; i++) {
			indice_inizio= indice_fine;
			indice_fine+= bound+1;
			ritorno.add(indice_inizio);
		}
		ritorno.add(testare.size());
		
		return ritorno;
	}
	
	/*
	 * Metodo che permette di definire i progetti che devono essere considerati come progetti di testing
	 */
	public ArrayList<Progetto> ritorna_Progetti_Set_test(int inizio, int fine, ArrayList<Progetto> complessivi){
		ArrayList<Progetto> ritorno= new ArrayList<>();
		
		int indice= 0;
		for (Progetto p: complessivi) {
			if (indice<fine && indice>= inizio) {
				indice++;
			}else {
				ritorno.add(p);
				indice++;
			}
		}
		return ritorno;
	}
	
	/*
	 * Metodo che permette di definire e ritornare i progetti che devono essere considerati come Validation nella iterazione corrente
	 */
	public ArrayList<Progetto> ritorna_Progetto_Val_Set(int inzio, int fine, ArrayList<Progetto> compessivi){
		ArrayList<Progetto> ritorno= new ArrayList<>();
		
		for (int i=inzio; i<fine; i++) {
			ritorno.add(compessivi.get(i));
		}
		
		return ritorno;
	}
}