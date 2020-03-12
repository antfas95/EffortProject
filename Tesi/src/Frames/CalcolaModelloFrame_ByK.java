package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

public class CalcolaModelloFrame_ByK extends JFrame {

	//Dichiarazione delle variabili di istanza della classe
	private ArrayList<Modello> modelli_finali;
	private boolean controllo= false;
	private GestoreStruttura gestore;
	private ArrayList<Progetto> presenti_Sistema;
	private int primo_k;
	private int secondo_k;
	private ArrayList<Integer> indici_prima_divisione;
	private Modello risultante;

	//Dichiarazione delle variabili di istanza che risultano essere essenziali per il relativo calcolo del modello
	private ArrayList<Integer> pesi;
	private int p_mink;

	//Dichiarazione delle variabili di istanza per la rappresentazione del frame
	private JPanel sud_panel;
	private JButton calcola;
	private JButton indietro;
	private JButton applica;
	private JTextArea area;
	private JScrollPane scroll;

	public CalcolaModelloFrame_ByK(GestoreStruttura gestore, ArrayList<Progetto> presenti, int k, ArrayList<Integer> indici) {
		this.setTitle("Calcolo Modello secondo k-fold");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);

		this.gestore= gestore;
		this.presenti_Sistema= presenti;
		this.primo_k= k;
		this.indici_prima_divisione= indici;
		this.pesi= new ArrayList<>();
		this.modelli_finali= new ArrayList<>();

		//Implementare una sola partizione del k sul quale apportare la divisione dei progetti di riferimento
		//secondo_k= Integer.parseInt(JOptionPane.showInputDialog("Digita secondo valore k, che dividerà il k-esimo set dei progetti presenti in k parte, importante per il calcolo del modello utilizzando come calcolo la k-fold "));

		JLabel label= new JLabel("Premi 'calcola', per ottenere il modello migliore");
		label.setForeground(Color.RED);
		area= new JTextArea();
		scroll= new JScrollPane(area);

		createSudPanel();

		add(scroll, BorderLayout.CENTER);
		add(label, BorderLayout.NORTH);
		this.setVisible(true);
	}

	public void createSudPanel() {
		// TODO Auto-generated method stub
		sud_panel= new JPanel();
		calcola= new JButton("Calcola");
		indietro= new JButton("Indietro");
		applica= new JButton("Avanti");

		class Operazione_Calcola_Modello implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controllo= true;
				ArrayList<Variabile> v_rif= presenti_Sistema.get(0).getVariabili_progetto();
				for (Variabile v: v_rif) {
					int valore= Integer.parseInt(JOptionPane.showInputDialog("Digita il peso della variabile, utile per il calcolo della distanza euclidea ponderata: " + v.getNome()));
					pesi.add(valore);
				}

				p_mink= Integer.parseInt(JOptionPane.showInputDialog("Digita il valore p, oggetto nella formula di Minkowski: "));

				int conta=0;
				for (Integer i: indici_prima_divisione) {
					System.out.println(conta + " Valore: " + i);
					conta++;
				}
				System.out.println("Numero degli elementi: " + conta);

				//ArrayList fondamentale per tenere traccia dei secondi indici ottenuti nella divizione
				for (int i= 0; i<primo_k; i++) {
					
					System.out.println("Iterazione: " + i);
					ArrayList<Progetto> test_Set= new ArrayList<>();
					ArrayList<Progetto> test_Val= new ArrayList<>();

					test_Set= gestore.ritorna_Progetti_Set_test(indici_prima_divisione.get(i), indici_prima_divisione.get(i+1), presenti_Sistema);
					test_Val= gestore.ritorna_Progetto_Val_Set(indici_prima_divisione.get(i), indici_prima_divisione.get(i+1), presenti_Sistema);
					System.out.println("Primi indici considerati: " + indici_prima_divisione.get(i) + "" + indici_prima_divisione.get(i+1));
					
					ArrayList<Modello> modelli_risultato= gestore.calcola_modello1(test_Set, test_Val, pesi, p_mink);
					
					for (Modello m: modelli_risultato) {
						modelli_finali.add(m);
					}

					/*
					ArrayList<Integer> indici_seconda_Div= new ArrayList<>();
					indici_seconda_Div= gestore.ritornaInizio_Fine(test_Set, secondo_k);
					*/

					/*
					 * Implemetare solo la partizione con un unico k quindi questa sequenza di codice non è necessaria
					for(int j=0; j<secondo_k; j++) {
						ArrayList<Progetto> test_Set_Def= new ArrayList<>();
						ArrayList<Progetto> test_Val_Def= new ArrayList<>();

						test_Set_Def= gestore.ritorna_Progetti_Set_test(indici_seconda_Div.get(j), indici_seconda_Div.get(j+1), test_Set);
						test_Val_Def= gestore.ritorna_Progetto_Val_Set(indici_seconda_Div.get(j), indici_seconda_Div.get(j+1), test_Set);
						System.out.println("Adesso calcolo il modello sugli indici: " + indici_seconda_Div.get(j) + " " + indici_seconda_Div.get(j+1));

						ArrayList<Modello> modelli_risultato= gestore.calcola_modello1(test_Set_Def, test_Val_Def, pesi, p_mink);

						for (Modello m: modelli_risultato) {
							modelli_finali.add(m);
						}
					}
					*/
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
				
				area.append("\n");
				area.append("K -> " + risultante.getK());
				area.append("\n");
				area.append("Tipologia distanza -> " + risultante.getTipologia_distanza());
				area.append("\n");
				area.append("Strategia di adattamento -> " + risultante.getTipologia_analisi());
				area.append("\n");
				//area.append("Effort -> " + risultante.getEffort());
				//area.append("\n");
				area.append("\n");
				area.append("Modello calcolato con successo, adesso premi 'Avanti' per continuare esecuzione");
				
			}
		}
		ActionListener listener2= new Operazione_Calcola_Modello();
		calcola.addActionListener(listener2);

		class Operazione_Indietro implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				controllo= false;
				JFrame frame= new Menu_Frame(gestore);
				dispose();
			}
		}
		ActionListener listener= new Operazione_Indietro();
		indietro.addActionListener(listener);
		
		class Operazione_Applica_Modello_risultato implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				controllo= false;
				/*
				ArrayList<Progetto> progetti_Validate= new ArrayList<>();
				ArrayList<Progetto> progetti_test= new ArrayList<>();
				progetti_test= gestore.ritorna_Progetto_Testing();
				progetti_Validate= gestore.ritorna_Progetto_Validation();
				*/
				try {
					JFrame frame= new Modello_Applicato_Frame(gestore, risultante, presenti_Sistema, indici_prima_divisione, pesi, p_mink);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dispose();
			}
		}
		ActionListener listener3= new Operazione_Applica_Modello_risultato();
		applica.addActionListener(listener3);

		sud_panel.add(calcola);
		sud_panel.add(applica);
		sud_panel.add(indietro);
		add(sud_panel, BorderLayout.SOUTH);
	}
}