package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Bean.GestoreStruttura;
import Bean.Modello;
import Bean.Progetto;
import Bean.Variabile;

public class CalcolaModelloFrame extends JFrame {
	
	//Dichiarazione delle variabili di istanza della classe ritornati dal costruttore
	private static final long serialVersionUID = 1L;
	private GestoreStruttura gestore;
	private ArrayList<Progetto> progetti_ela_finale;
	private ArrayList<Progetto> progetti_testing;
	private ArrayList<Integer> inzio_fine;
	private int k;
	
	//Variabili di istanza utilizzate per la rappresentazione del frame
	private JPanel sud_panel;
	private JButton calcola;
	private JButton indietro;
	private JButton applica;
	private JTextArea area;
	private JScrollPane scroll;
	
	
	private ArrayList<Modello> modelli_finali;
	private Modello risultante;
	
	//Dichiarazione delle variabili di istanza per l'elaborazione della validazione secondo la divisione dei sei
	private ArrayList<Progetto> per_Validazione;
	private ArrayList<Progetto> da_Testare_X_Modello; 
	private ArrayList<Modello> modelli_By_All_Sets;
	
	//Variabili di istanza per rendere standard le variabili dei pesi e di Minkowski per il calcolo del modello
	private int p_mink;
	private ArrayList<Integer> pesi;
	
	private boolean controllo= false;

	public CalcolaModelloFrame(GestoreStruttura ges, ArrayList<Progetto> val_fin, ArrayList<Progetto> da_Testare, int k_sets, ArrayList<Integer> interi) {
		this.setTitle("Calcolo Modello");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		
		this.gestore= ges;
		this.progetti_ela_finale= val_fin;
		this.progetti_testing= da_Testare;
		this.k= k_sets;
		this.inzio_fine= interi;
		this.modelli_finali= new ArrayList<>();
		this.pesi= new ArrayList<>();
		
		JLabel label= new JLabel("Premi 'calcola', per ottenere il modello migliore");
		label.setForeground(Color.RED);
		area= new JTextArea();
		scroll= new JScrollPane(area);
		
		area.append("In attesa che venga cliccato il pulsante 'Calcola'");
		
		createSudPanel();
		
		add(scroll, BorderLayout.CENTER);
		add(label, BorderLayout.NORTH);
		this.setVisible(true);
	}
	
	/*
	public CalcolaModelloFrame(GestoreStruttura ges, ArrayList<Progetto> val_fin, ArrayList<Progetto> da_Testare, int k) throws FileNotFoundException {
		this.setTitle("Calcolo Modello");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		
		this.gestore= ges;
		this.progetti_ela_finale= val_fin;
		this.progetti_testing= da_Testare;
		this.modelli_finali= new ArrayList<>();
		this.k= k;
		this.per_Validazione= new ArrayList<>();
		this.modelli_By_All_Sets= new ArrayList<>();
		this.pesi= new ArrayList<>();
		
		JLabel label= new JLabel("Premi 'calcola', per ottenerlo");
		label.setForeground(Color.RED);
		area= new JTextArea();
		scroll= new JScrollPane(area);
		
		area.append("Ecco il/i modelli più efficienti riscontrati");
		
		//Popolo l'array che è dedicato per la validazione dei dati di riferimento
		int grandezza_set= da_Testare.size()/k;
		for (int i=da_Testare.size()-grandezza_set+1; i<=da_Testare.size()-1; i++) {
			Progetto p= da_Testare.get(i);
			per_Validazione.add(p);
		}
		
		createSudPanelByK();
		
		add(scroll, BorderLayout.CENTER);
		add(label, BorderLayout.NORTH);
		this.setVisible(true);
	}
	

	public void createSudPanelByK() {
		// TODO Auto-generated method stub
		sud_panel= new JPanel();
		calcola= new JButton("Calcola");
		indietro= new JButton("Indietro");
		applica= new JButton("Avanti");
		
		class Operazione_Calcola_Modello implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//Istruzioni per specificare il peso di riferimento per la distanza euclidea Ponderata
				controllo=true;
				ArrayList<Variabile> vars= progetti_testing.get(0).getVariabili_progetto();
				for (Variabile v: vars) {
					pesi.add(Integer.parseInt(JOptionPane.showInputDialog("Digita il peso della variabile: " + v.getNome())));
				}
				
				p_mink= Integer.parseInt(JOptionPane.showInputDialog("Digita il peso della variabile p, in Mink: "));
				
				int j=0;
				int indice_sviluppo= progetti_testing.size()/k;
				for (int i=1; i<k; i++) {
					da_Testare_X_Modello= new ArrayList<>();
					int indice_fine= j+indice_sviluppo;
					for (j=j+1; j<=indice_fine; j++) {
						da_Testare_X_Modello.add(progetti_testing.get(j));
					}
					modelli_finali= gestore.calcola_modello_By_Sets(da_Testare_X_Modello, per_Validazione, pesi, p_mink);
					for (Modello m: modelli_finali) {
						System.out.println(m.toString());
					}
					
					ArrayList<Float> interi= new ArrayList<>();
					for (Modello m: modelli_finali) {
						interi.add(m.getEffort());
					}
					Collections.sort(interi);
					float effort_minimo= interi.get(0);
					for (Modello m: modelli_finali) {
						System.out.println(m.toString());
						if (m.getEffort()==effort_minimo) {
							modelli_By_All_Sets.add(m);
							break;
						}
					}
				}
				
				//Stampo i modelli che sono stati selezionati
				System.out.println("Stampo i modelli di riferimento risultanti per ogni singolo set");
				for (Modello k: modelli_By_All_Sets) {
					System.out.println(k.toString());
				}
				
				int i= 1;
				for (Modello m: modelli_By_All_Sets) {
					area.append("Ecco il modello riscontrato sull'elaborazione del " + i + " set di progetti: ");
					area.append("\n");
					area.append("\n");
					area.append("K -> " + m.getK());
					area.append("\n");
					area.append("Tipologia distanza -> " + m.getTipologia_distanza());
					area.append("\n");
					area.append("Strategia di adattamento -> " + m.getTipologia_analisi());
					area.append("\n");
					//area.append("Effort -> " + m.getEffort());
					area.append("\n");
					area.append("\n");
					area.append("Il modello è stato calcolato. Per continuare clicca 'Avanti'");
					i++;
				}	
			}
		}
		ActionListener listener= new Operazione_Calcola_Modello();
		calcola.addActionListener(listener);
		
		for (Modello m: modelli_By_All_Sets) {
			System.out.println(m.toString());
		}
		
		class Operazione_Indietro implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame frame= new Menu_Frame(gestore);
				dispose();
			}
		}
		ActionListener listener2= new Operazione_Indietro();
		indietro.addActionListener(listener2);
		
		class Operazione_Applica_Modelli implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (controllo==false) {
					JOptionPane.showConfirmDialog(null, "Devi prima elaborare il modello per poi poterlo applicare");
				}else {
					try {
						JFrame frame= new Modello_Applicato_Frame_By_Sets(gestore, modelli_finali, progetti_ela_finale, progetti_testing, k, pesi, p_mink);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					dispose();
				}
			}
		}
		ActionListener listener3= new Operazione_Applica_Modelli();
		applica.addActionListener(listener3);
		
		sud_panel.add(calcola);
		sud_panel.add(applica);
		sud_panel.add(indietro);
		add(sud_panel, BorderLayout.SOUTH);
	}
	*/
	
	public void createSudPanel() {
		// TODO Auto-generated method stub
		sud_panel= new JPanel();
		calcola= new JButton("Calcola");
		indietro= new JButton("Indietro");
		applica= new JButton("Avanti");
		
		class Operazione_Calcolo_Modello implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//In questo frangente aggiungere il metodo presente in Gestore Struttura che permette di calcolare il modello
				ArrayList<Variabile> v_rif= progetti_testing.get(0).getVariabili_progetto();
				for (Variabile v: v_rif) {
					int valore= Integer.parseInt(JOptionPane.showInputDialog("Digita il peso della variabile: " + v.getNome()));
					pesi.add(valore);
				}
				
				p_mink= Integer.parseInt(JOptionPane.showInputDialog("Digita il valore p, oggetto nella formula di Minkowski: "));
				
				controllo= true;
				
				
				for (int i= 0; i<k; i++) {
					ArrayList<Progetto> test_Set= new ArrayList<>();
					ArrayList<Progetto> test_Val= new ArrayList<>();
					
					test_Set= gestore.ritorna_Progetti_Set_test(inzio_fine.get(i), inzio_fine.get(i+1), progetti_testing);
					System.out.println("Valori del test_Set: " + inzio_fine.get(i+1) + " Size: " + test_Set.size());
					test_Val= gestore.ritorna_Progetto_Val_Set(inzio_fine.get(i), inzio_fine.get(i+1), progetti_testing);
					System.out.println("Valori del Validation: " + inzio_fine.get(i) + " " + inzio_fine.get(i+1) + " Size: " + test_Val.size());
					ArrayList<Modello> modelli_risultato= gestore.calcola_modello1(test_Set, test_Val, pesi, p_mink);
					
					
					for (Modello m: modelli_risultato) {
						modelli_finali.add(m);
					}
				}
				
				for (Modello m: modelli_finali) {
					System.out.println(m.toString());
				}
				
				System.out.println("Numero di elementi pari a: " + modelli_finali.size());
				
				ArrayList<Float> interi= new ArrayList<>();
				for (Modello m: modelli_finali) {
					interi.add(m.getEffort());
				}
				
				Collections.sort(interi);
				
				float effort_minimo= interi.get(0);
				for (Modello m: modelli_finali) {
					if (m.getEffort()==effort_minimo) {
						risultante= m;
					}
				}
				
				area.setText("Ecco i valori del modello risultante: ");
				area.append("\n");
				area.append("\n");
				area.append("K -> " + risultante.getK());
				area.append("\n");
				area.append("Tipologia distanza -> " + risultante.getTipologia_distanza());
				area.append("\n");
				area.append("Strategia di adattamento -> " + risultante.getTipologia_analisi());
				area.append("\n");
				//area.append("Effort -> " + risultante.getEffort());
				area.append("\n");
				area.append("\n");
				area.append("Modello calcolato. Per continuare clicca su 'Avanti'");
				
				/*
				for (Modello m: modelli_finali) {
					area.append("\n");
					area.append("K -> " + m.getK());
					area.append("\n");
					area.append("Tipologia distanza -> " + m.getTipologia_distanza());
					area.append("\n");
					area.append("Tipologia analisi -> " + m.getTipologia_analisi());
					area.append("\n");
					area.append("Effort -> " + m.getEffort());
					area.append("\n");
				}
				*/
			}
		}
		ActionListener listener= new Operazione_Calcolo_Modello();
		calcola.addActionListener(listener);
	
		class Operazione_Indietro implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame frame= new Menu_Frame(gestore);
				dispose();
			}
		}
		ActionListener listener2= new Operazione_Indietro();
		indietro.addActionListener(listener2);
		
		class Operazione_Applica_Modello implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (controllo==false) {
					JOptionPane.showConfirmDialog(null, "Devi prima elaborare il modello per poi poterlo applicare");
				}else {
					try {
						JFrame frame= new Modello_Applicato_Frame(gestore, risultante, progetti_ela_finale, progetti_testing, pesi);
						dispose();
						controllo= false;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dispose();
				}
			}
		}
		ActionListener listener3= new Operazione_Applica_Modello();
		applica.addActionListener(listener3);
		
		sud_panel.add(calcola);
		sud_panel.add(indietro);
		sud_panel.add(applica);
		add(sud_panel, BorderLayout.SOUTH);
	}
}