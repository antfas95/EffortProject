package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Bean.GestoreStruttura;
import Bean.Progetto;

public class Menu_Frame extends JFrame {

	//Dichiarazione delle variabili di istanza della classe
	private static final long serialVersionUID = 1L;
	private GestoreStruttura gestore;
	private ArrayList<Progetto> progetti_da_testare;
	private ArrayList<Progetto> progetti_ela_finale;
	//private ArrayList<Progetto> progetti_testing_definitivi;
	//private ArrayList<Progetto> progetti_validation_definitivi;

	private JPanel central_panel;
	private JButton cross_validation;
	private JButton LOO;
	private JButton indietro;

	public Menu_Frame(GestoreStruttura gestore) {
		this.gestore= gestore;
		this.setTitle("Menu Frame");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);

		this.gestore= gestore;
		this.progetti_da_testare= new ArrayList<>();
		this.progetti_ela_finale= new ArrayList<>();
		//progetti_testing_definitivi= new ArrayList<>();
		//progetti_validation_definitivi= new ArrayList<>();

		JLabel label= new JLabel("Scegli la procedura che intendi utilizzare: ");
		label.setForeground(Color.RED);

		createCentralPanel();

		add(label, BorderLayout.NORTH);
		this.setVisible(true);
	}

	private void createCentralPanel() {
		// TODO Auto-generated method stub
		central_panel= new JPanel();
		cross_validation= new JButton("Hold Out");
		LOO= new JButton ("K-Fold-Validation");
		indietro= new JButton("indietro");

		class Operazione_CrossValidation implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Adesso devo chiamare la procedura presente in GestoreStruttura che mi permette di effettuare la divisione del set dei
				 * progetti che sono presenti all'interno del file china
				 */
				progetti_da_testare= gestore.ritorna_Progetto_Testing();
				progetti_ela_finale= gestore.ritorna_Progetto_Validation();
				
				int k_sets= Integer.parseInt(JOptionPane.showInputDialog("Digita il valore del k, utile per la divisione in k parti del training set: "));
				
				ArrayList<Integer> inizio_fine= gestore.ritornaInizio_Fine(progetti_da_testare, k_sets);

				JFrame frame= new CalcolaModelloFrame(gestore, progetti_ela_finale, progetti_da_testare, k_sets, inizio_fine);
				dispose();
			}
		}
		ActionListener listener= new Operazione_CrossValidation();
		cross_validation.addActionListener(listener);

		class Operazione_LOO implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Inserimento delle istruzioni per eseguire la funzionalità di LOO
				int valore= Integer.parseInt(JOptionPane.showInputDialog("Digita il valore del k utile, per la divisione dei progetti presenti in k parti"));
				ArrayList<Progetto> presenti= new ArrayList<>();
				presenti= gestore.ritorna_Progetti_Presenti();
				ArrayList<Integer> inizio_fine= gestore.ritornaInizio_Fine(presenti, valore);
				JFrame frame= new CalcolaModelloFrame_ByK(gestore, presenti, valore, inizio_fine);
				dispose();
			}
		}
		ActionListener listener2= new Operazione_LOO();
		LOO.addActionListener(listener2);

		class Operazione_Indietro implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame frame= new IndicazioniFrame(gestore);
				dispose();
			}
		}
		ActionListener listener3= new Operazione_Indietro();
		indietro.addActionListener(listener3);

		central_panel.add(cross_validation);
		central_panel.add(LOO);
		add(central_panel, BorderLayout.CENTER);
	}
}