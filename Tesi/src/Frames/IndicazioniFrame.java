package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Bean.GestoreStruttura;

public class IndicazioniFrame extends JFrame {
	
	//Dichiarazioni delle variabili di istanza della classe
	private GestoreStruttura gestore;
	private JPanel panel_center;
	private JPanel panel_sud;
	private JButton conferma;
	private JButton annulla;
	
	public IndicazioniFrame(GestoreStruttura gestore) {
		this.gestore= gestore;
		this.setTitle("Warning");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		
		JLabel label= new JLabel("ATTENZIONE! Controllare i requisiti prima di continuare");
		label.setForeground(Color.RED);
		
		createCentralPanal();
		createSudPanel();
		
		add(label, BorderLayout.NORTH);
		
		this.setVisible(true);
	}

	private void createSudPanel() {
		// TODO Auto-generated method stub
		panel_sud= new JPanel();
		conferma= new JButton("Conferma");
		annulla= new JButton("Annulla");
		
		class Conferma_Operazione implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFrame frame= new Menu_Frame(gestore);
				dispose();
			}
		}
		ActionListener listener= new Conferma_Operazione();
		conferma.addActionListener(listener);
		
		class Annulla_Operazione implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		}
		ActionListener listener2= new Annulla_Operazione();
		annulla.addActionListener(listener2);
		
		panel_sud.add(conferma);
		panel_sud.add(annulla);
		add(panel_sud, BorderLayout.SOUTH);
	}

	private void createCentralPanal() {
		// TODO Auto-generated method stub
		panel_center= new JPanel();
		JLabel label1= new JLabel("1) Il file da leggere deve essere di formato ARFF; ");
		JLabel label2= new JLabel("2) L'ultimo elemento del file deve riguardare l'effort del progetto; ");
		JLabel label3= new JLabel("3) L'identificativo del progetto deve essere contrassegnato dalla stringa 'ID'");
		panel_center.add(label1);
		panel_center.add(label2);
		panel_center.add(label3);
		add(panel_center, BorderLayout.CENTER);
	}
}