package Frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Bean.GestoreStruttura;

public class Visualizza_Valori_Potenza_Predittiva extends JFrame {
	
	//Dichiarazione delle variabili di istanza della classe
	private GestoreStruttura gestore;
	private JTextArea area;
	private JScrollPane scroll;
	private ArrayList<Float> effort_reali;
	private ArrayList<Float> effort_predetto;
	private ArrayList<Float> solo_pred25;
	private ArrayList<Float> solo_pred25_ordinati;
	//Importante la divisione dei due array proprio perchè il primo serve per la scrittura nel file secondo un determinato ordine il secondo per facilitare determinati calcoli nella classe
	private ArrayList<Float> mre_valori;
	private ArrayList<Float> mre_valori_ordinati;
	private ArrayList<Float> absres_valori;
	private ArrayList<Float> absres_valori_ordinati;
	private JPanel sud_panel;
	private JButton ottieni;
	private JButton fine;
	
	//Variabili importanti per il riferimento della lettura di un determinato File
	private Scanner in;
	private FileReader reader;
	
	public Visualizza_Valori_Potenza_Predittiva(GestoreStruttura gestore) throws FileNotFoundException {
		this.setTitle("View Predictive Results");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		
		this.gestore= gestore;
		effort_reali= new ArrayList<>();
		effort_predetto= new ArrayList<>();
		solo_pred25= new ArrayList<>();
		solo_pred25_ordinati= new ArrayList<>();
		mre_valori= new ArrayList<>();
		mre_valori_ordinati= new ArrayList<>();
		absres_valori= new ArrayList<>();
		absres_valori_ordinati= new ArrayList<>();
		reader= new FileReader("SalvataggioRealePredetto");
		in= new Scanner(reader);
		
		area= new JTextArea();
		scroll= new JScrollPane(area);
		area.append("Clicca su 'ottieni' per i valori che riguardano il calcolo della potenza predittiva: ");
		area.append("\n");
		
		createSudPanel();
		
		add(scroll, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public void createSudPanel() {
		// TODO Auto-generated method stub
		sud_panel= new JPanel();
		ottieni= new JButton("Ottieni");
		fine= new JButton("Fine");
		
		class Operazione_Ottieni implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				area.setText("Ecco i valori risultanti dell'analisi predittiva calcolata");
				boolean reale_riferimento= true;
				
				while (in.hasNextLine()) {
					if (reale_riferimento==true) {
						effort_reali.add(Float.parseFloat(in.nextLine()));
						reale_riferimento= false;
					}else {
						effort_predetto.add(Float.parseFloat(in.nextLine()));
						reale_riferimento= true;
					}
				}
				System.out.println("Ho memorizzato tutti i valori presenti all'interno del file");
				
				float tot_mre= 0;
				float tot_pred25= 0;
				float tot_AbsRes= 0;
				//Ottenuti i valori Reali-Predetto di ogni progetto presente all'interno del progetto adesso calcolo AR, MRE, MdMRE
				for (int i=0; i<effort_reali.size(); i++) {
					float mre= Math.abs(effort_reali.get(i)-effort_predetto.get(i))/effort_reali.get(i);
					mre_valori.add(mre);
					tot_mre+= mre;
					
					if (mre<=0.25) {
						solo_pred25.add(mre);
						tot_pred25+= mre;
					}
					float absolute_residual= Math.abs(effort_reali.get(i)-effort_predetto.get(i));
					tot_AbsRes+= absolute_residual;
					absres_valori.add(absolute_residual);
				}
				
				for (Float f: mre_valori) {
					System.out.println("Ecco il valore mre : " + f);
				}
				
				//For grazie al quale riesco a prelevare l'MRE più alto, il totale, il più basso e la media
				mre_valori_ordinati= mre_valori;
				solo_pred25_ordinati= solo_pred25;
				absres_valori_ordinati= absres_valori;
				
				Collections.sort(mre_valori_ordinati);
				Collections.sort(solo_pred25_ordinati);
				Collections.sort(absres_valori_ordinati);
				
				for (Float f: mre_valori_ordinati) {
					System.out.println(f);
				}
				System.out.println("Ecco la grandezza array: " + mre_valori.size());
				System.out.println("Ecco la grandezza array ordinati: " + mre_valori_ordinati.size());
				
				//Mostro a video nella Text Area i valori relativi a MRE
				area.append("\n");
				area.append("Valori principali relativi a MRE");
				area.append("\n");
				int indice = 0;
				for (int i=0; i<mre_valori_ordinati.size(); i++) {
					if (mre_valori_ordinati.get(i)!=0.0) {
						indice= i;
						break;
					}
				}
				area.append("Valore più piccolo riscontrato: " + mre_valori_ordinati.get(indice));
				area.append("\n");
				area.append("Valore più grande riscontrato: " + mre_valori_ordinati.get(mre_valori_ordinati.size()-2));
				area.append("\n");
				area.append("Valore della MMRE: " + tot_mre/mre_valori_ordinati.size());
				area.append("\n");
				float mediana= calcola_Mediana(mre_valori_ordinati);
				area.append("Valore della MdMRE: " + mediana);
				area.append("\n");
				area.append("Valore della media di pred(25): " + tot_pred25/mre_valori.size());
				area.append("\n");
				float valore_pred25= solo_pred25.size()/mre_valori.size();
				area.append("Valore pred(25) in base al numero di occorrenze: " + valore_pred25);
				area.append("Valore elementi in pred 25: " + solo_pred25.size() + "   " + mre_valori.size());
				
				//Mostro a video all'interno della text Area i valori riguaradanti l'absolute residual
				area.append("\n");
				area.append("\n");
				area.append("Valori principali relativi a Absolute Residual");
				area.append("\n");
				int indice1 = 0;
				for (int i=0; i<absres_valori_ordinati.size(); i++) {
					if (absres_valori_ordinati.get(i)!=0.0) {
						indice1= i;
						break;
					}
				}
				area.append("Valore più piccolo riscontrato: " + absres_valori_ordinati.get(indice1));
				area.append("\n");
				area.append("Valore più grande riscontrato: " + absres_valori_ordinati.get(absres_valori_ordinati.size()-2));
				area.append("\n");
				area.append("Valore della media di absolute residual: " + tot_AbsRes/absres_valori_ordinati.size());
				area.append("\n");
				float mediana1= calcola_Mediana(absres_valori_ordinati);
				area.append("Valore della mediana secondo Absolute residual: " + mediana1);
				
				/*
				 * Non è necessario mostrare a video questi valori per il calcolo del pred(25)
				area.append("\n");
				area.append("\n");
				area.append("Valori principali relativi a Pred(25)");
				area.append("\n");
				area.append("Valore più piccolo riscontrato: " + solo_pred25_ordinati.get(0));
				area.append("\n");
				area.append("Valore più grande riscontrato: " + solo_pred25_ordinati.get(solo_pred25_ordinati.size()-1));
				area.append("\n");
				float mediana2= calcola_Mediana(solo_pred25_ordinati);
				area.append("\n");
				area.append("Valore della mediana secondo i valori di solo pred(25): " + mediana2);
				 */
				
				/*
				 * In questo punto della classe deve essere aggiunta la funzionalità di poter aggiungere e salvare i dati riscontrati in un file Excel
				 * il quale path viene fornito all'inzio e memorizzato all'interno del costruttore della classe stessa esaminata
				 */
				
			}

			public float calcola_Mediana(ArrayList<Float> valori) {
				// TODO Auto-generated method stub
				float ritorno= 0;
				if (valori.size()%2==0) {
					float primo= valori.get(valori.size()/2);
					float secondo= valori.get(valori.size()/2+1);
					ritorno= (primo + secondo)/2;
				}else {
					ritorno= valori.get((int) (valori.size()/2+0.5));
				}
				return ritorno;
			}
		}
		ActionListener listener= new Operazione_Ottieni();
		ottieni.addActionListener(listener);
		
		class Operazione_Fine implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Menu_Frame frames= new Menu_Frame(gestore);
				dispose();
			}
		}
		
		sud_panel.add(ottieni);
		sud_panel.add(fine);
		add(sud_panel, BorderLayout.SOUTH);
	}
}