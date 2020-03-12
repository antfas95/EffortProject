package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Bean.GestoreStruttura;
import Bean.Modello;
import Bean.Progetto;
import Bean.Variabile;

public class Modello_Applicato_Frame extends JFrame {
	//Dichiarazioni delle variabili di istanza della classe
	private GestoreStruttura gestore;
	private Modello applicare;
	private ArrayList<Progetto> finali;
	private ArrayList<Integer> pesi_riferimento;
	private ArrayList<Progetto> testing;
	private JPanel sud_panel;
	private JButton conferma;
	private JButton annulla;
	private JTextArea area;
	private JScrollPane pane;
	private boolean controllo;
	private JButton avanti;
	private float effort_predetto;
	private FileOutputStream output;
	private FileOutputStream output2;
	private PrintStream print;
	private PrintStream print2;
	private boolean eseguito= false;
	
	//Dichiarazione delle variabili di istanza che permettono di scrivere all'interno di un file in Excel
	private String path_file= "C:\\\\Users\\\\Antonio.Fasulo\\\\Desktop\\\\Dati.xls";
	private HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet("View di tutti gli effort");
	HSSFRow row = null;

	//Esempio array di prova per il calcolo di determinati valori
	private ArrayList<Float> valori_effort;
	private ArrayList<Float> valori_predetto;
	private JButton vista_scarti;

	//Dichiarazione delle variabili di istanza per la creazione del Frame riguardante per la creazione del frame k-fold-validation
	private ArrayList<Integer> interi_Indici_Divisione;
	private int minkowski;

	//Variabili di istanza per la scrittura sul file ID_REALE_PREDETTO
	private float ID;
	private float reale;
	private float predetto;

	public Modello_Applicato_Frame(GestoreStruttura ges, Modello risultante, ArrayList<Progetto> f, ArrayList<Progetto> testing, ArrayList<Integer> pesi) throws FileNotFoundException {
		this.setTitle("Applicazione del modello");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setBackground(Color.gray);

		//String path_file= JOptionPane.showInputDialog("Digita il path del file 'excel' sul quale memorizzare i dati relativi ai progetti di validazione: ");

		this.gestore= ges;
		this.applicare= risultante;
		this.finali= f;
		this.testing= testing;
		this.output= new FileOutputStream("SalvataggioRealePredetto");
		this.output2= new FileOutputStream("ID_REALE_PREDETTO");
		this.print= new PrintStream(output);
		this.print2= new PrintStream(output2);
		this.pesi_riferimento= pesi;

		//Inizializzazione dati per progetti
		valori_effort= new ArrayList<>();
		valori_predetto= new ArrayList<>();

		area= new JTextArea();
		pane= new JScrollPane(area);

		for (Progetto p: finali) {
			System.out.println(p.miaToString());
		}

		int contatore=0;
		for (Progetto p: finali) {
			contatore++;
			ArrayList<Variabile> v= p.getVariabili_progetto();
			for (Variabile va: v) {
				if (va.getNome().equals("Effort")) {
					float valore= va.getValore();
					area.append(contatore + " -> " + valore);
					area.append("\n");
				}
			}
		}
		area.append("\n");
		area.append("Premi 'Applica' per poter applicare il modello sui progetti presenti nel Validation test...");

		createSudPanel();

		add(pane, BorderLayout.CENTER);
		this.setVisible(true);
	}

	//Creazione del costruttore Calcola modello ma che riguarda l'attività della k-fold-validation
	public Modello_Applicato_Frame(GestoreStruttura ges, Modello risultante, ArrayList<Progetto> f, ArrayList<Integer> interi, ArrayList<Integer> pesi, int p_mink) throws IOException {
		this.setTitle("Premi 'applica' per poter usufruire del modello");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setBackground(Color.gray);

		//String path_file= JOptionPane.showInputDialog("Digita il path del file 'excel' sul quale memorizzare i dati relativi ai progetti di validazione: ");

		this.gestore= ges;
		this.applicare= risultante;
		this.finali= f;
		this.interi_Indici_Divisione= interi;
		this.minkowski= p_mink;
		this.output= new FileOutputStream("SalvataggioRealePredetto");
		this.output2= new FileOutputStream("ID_REALE_PREDETTO");
		this.print= new PrintStream(output);
		this.print2= new PrintStream(output2);
		this.pesi_riferimento= pesi;

		//Inizializzazione dati per progetti
		valori_effort= new ArrayList<>();
		valori_predetto= new ArrayList<>();

		area= new JTextArea();
		pane= new JScrollPane(area);

		for (Progetto p: finali) {
			System.out.println(p.miaToString());
		}

		int contatore=0;
		for (Progetto p: finali) {
			contatore++;
			ArrayList<Variabile> v= p.getVariabili_progetto();
			for (Variabile va: v) {
				if (va.getNome().equals("Effort")) {
					float valore= va.getValore();
					area.append(contatore + " -> " + valore);
					area.append("\n");
				}
			}
		}
		area.append("\n");
		area.append("Premi 'Applica' per poter applicare il modello sui progetti presenti nel Validation test...");
		FileOutputStream fileOut = new FileOutputStream(path_file);
		boolean ritorno= createSudPanel_k();
		/*
		if (ritorno==true) {
			row = sheet.createRow(0);
			row.createCell((short)0).setCellValue("ID");
			row.createCell((short)1).setCellValue("Reale");
			row.createCell((short)2).setCellValue("Predetto");
			
			int r_number= finali.size();
			for (int i=0; i<r_number; i++) {
				row = sheet.createRow(i+1);
				row.createCell((short)0).setCellValue(i+1);
				row.createCell((short)1).setCellValue(valori_effort.get(i));
				row.createCell((short)2).setCellValue(valori_predetto.get(i));
			}
			wb.write(fileOut);
			int intero= valori_effort.size();
			System.out.println("Valore effort indicato: " + intero);
			fileOut.close();
		}
		*/
		add(pane, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public boolean createSudPanel_k() {
		// TODO Auto-generated method stub
		sud_panel= new JPanel();
		conferma= new JButton("Applica");
		annulla= new JButton("Annulla");
		avanti= new JButton("Avanti");

		class Operazione_applica_Modello implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ArrayList<Progetto> ritorno= new ArrayList<>();
				controllo= true;
				ArrayList<Variabile> variabili_progetto= new ArrayList<>();
				//Variabili utili per la creazione della prima riga all'interno del file excel
				FileOutputStream fileOut = null;
				try {
					fileOut = new FileOutputStream(path_file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				row = sheet.createRow(0);
				row.createCell((short)0).setCellValue("ID");
				row.createCell((short)1).setCellValue("Reale");
				row.createCell((short)2).setCellValue("Predetto");
				
				for (int i=0; i<interi_Indici_Divisione.size()-1; i++) {
					ArrayList<Progetto> test_Set= new ArrayList<>();
					ArrayList<Progetto> test_Val= new ArrayList<>();
					test_Set= gestore.ritorna_Progetti_Set_test(interi_Indici_Divisione.get(i), interi_Indici_Divisione.get(i+1), finali);
					test_Val= gestore.ritorna_Progetto_Val_Set(interi_Indici_Divisione.get(i), interi_Indici_Divisione.get(i+1), finali);
					if (applicare.getTipologia_distanza().equals("Euclidea")) {
						System.out.println("Mi trovo nel calcolo del modello euclideo");
						for (Progetto p: test_Val) {
							variabili_progetto= p.getVariabili_progetto();
							ritorno= gestore.distanza_euclidea_semplice(variabili_progetto, applicare.getK(), test_Set);
							for (Variabile v: variabili_progetto) {
								if(v.getNome().equals("ID")) {
									//Memorizzo nel file di indirizzo la tripla necessaria
									ID= v.getValore();
								}

								if(v.getNome().equals("Effort")) {
									//Effuttuo il salvataggio del valore Reale
									reale= v.getValore();
									print.println(v.getValore());
									valori_effort.add(reale);
								}

							}
							if (applicare.getTipologia_analisi().equals("Media")) {
								effort_predetto= gestore.calcola_media(ritorno);
								//Effettuo il salvataggio del valore Predetto
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								//Scrittura dei dati all'interno del file excel
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else if (applicare.getTipologia_analisi().equals("Mediana")) {
								effort_predetto= gestore.calcola_Mediana(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else {
								effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}
						}
					}
					else if (applicare.getTipologia_distanza().equals("Euclidea Ponderata")) {
						System.out.println("Mi trovo nel calcolo dell'euclidea Ponderata");
						ArrayList<Variabile> vars= new ArrayList<>();
						Progetto rif= finali.get(0);
						vars= rif.getVariabili_progetto();
						/*
						for (Variabile v: vars) {
							int valore= Integer.parseInt(JOptionPane.showInputDialog("Digita peso di: " + v.getNome()));
							pesi.add(valore);
						}
						 */
						for (Progetto p: test_Val) {
							variabili_progetto= p.getVariabili_progetto();
							ritorno= gestore.distanza_euclidea_ponderata(variabili_progetto, applicare.getK(), pesi_riferimento, test_Set);
							for (Variabile v: variabili_progetto) {
								if(v.getNome().equals("ID")) {
									//Memorizzo nel file di indirizzo la tripla necessaria
									ID= v.getValore();
								}

								if(v.getNome().equals("Effort")) {
									//Effuttuo il salvataggio del valore Reale
									reale= v.getValore();
									print.println(v.getValore());
									valori_effort.add(reale);
								}
							}
							if (applicare.getTipologia_analisi().equals("Media")) {
								effort_predetto= gestore.calcola_media(ritorno);
								//Effettuo il salvataggio del valore Predetto
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else if (applicare.getTipologia_analisi().equals("Mediana")) {
								effort_predetto= gestore.calcola_Mediana(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else {
								effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}
						}
					}else if (applicare.getTipologia_distanza().equals("Manhattan")) {
						for (Progetto p: test_Val) {
							variabili_progetto= p.getVariabili_progetto();
							ritorno= gestore.calcola_distanza_Manhattan(variabili_progetto, applicare.getK(), test_Set);
							for (Variabile v: variabili_progetto) {
								if(v.getNome().equals("ID")) {
									//Memorizzo nel file di indirizzo la tripla necessaria
									ID= v.getValore();
								}

								if(v.getNome().equals("Effort")) {
									//Effuttuo il salvataggio del valore Reale
									reale= v.getValore();
									valori_effort.add(reale);
									print.println(v.getValore());
								}
							}
							if (applicare.getTipologia_analisi().equals("Media")) {
								effort_predetto= gestore.calcola_media(ritorno);
								//Effettuo il salvataggio del valore Predetto
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else if (applicare.getTipologia_analisi().equals("Mediana")) {
								effort_predetto= gestore.calcola_Mediana(ritorno);
								print.println(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								valori_predetto.add(effort_predetto);
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else {
								effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}
						}
					}else {
						System.out.println("Mi trovo nel calcolo secondo Minkowski");
						int valore= gestore.ritorna_variabile_Minkowski();
						for (Progetto p: test_Val) {
							variabili_progetto= p.getVariabili_progetto();
							ritorno= gestore.distanza_Minkowski(variabili_progetto, applicare.getK(), valore, test_Set);
							for (Variabile v: variabili_progetto) {
								if(v.getNome().equals("ID")) {
									//Memorizzo nel file di indirizzo la tripla necessaria
									ID= v.getValore();
								}

								if(v.getNome().equals("Effort")) {
									//Effuttuo il salvataggio del valore Reale
									reale= v.getValore();
									print.println(v.getValore());
									valori_effort.add(reale);
								}
							}
							if (applicare.getTipologia_analisi().equals("Media")) {
								effort_predetto= gestore.calcola_media(ritorno);
								//Effettuo il salvataggio del valore Predetto
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else if (applicare.getTipologia_analisi().equals("Mediana")) {
								effort_predetto= gestore.calcola_Mediana(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}else {
								effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
								print.println(effort_predetto);
								valori_predetto.add(effort_predetto);
								row = sheet.createRow(i+1);
								row.createCell((short)0).setCellValue(ID);
								row.createCell((short)1).setCellValue(reale);
								row.createCell((short)2).setCellValue(effort_predetto);
								try {
									wb.write(fileOut);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
							}
						}
					}
				}
				area.append("\n");
				area.append("Modello calcolato con successo, 'Avanti' per continuare");
				eseguito= true;
			}
		}
		if (eseguito==true) {
			area.append("\n");
			area.append("\n");
			area.append("Modello calcolato, 'Avanti per continuare....'");
		}

		ActionListener listener= new Operazione_applica_Modello();
		conferma.addActionListener(listener);

		class Operazione_Indietro implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controllo= false;
				JFrame frame= new Menu_Frame(gestore);
				dispose();
			}
		}
		ActionListener listener2= new Operazione_Indietro();
		annulla.addActionListener(listener2);

		class Operazione_MRE implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (controllo==true) {
					try {
						JFrame frame= new Visualizza_Valori_Potenza_Predittiva(gestore);
						dispose();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showConfirmDialog(null, "Devi prima applicare il modello poi calcolare MDR o MdRE");
				}
			}
		}
		ActionListener listener3= new Operazione_MRE();
		avanti.addActionListener(listener3);

		sud_panel.add(conferma);
		sud_panel.add(annulla);
		sud_panel.add(avanti);
		add(sud_panel, BorderLayout.SOUTH);
		return true;
	}

	public void createSudPanel() {
		// TODO Auto-generated method stub
		sud_panel= new JPanel();
		conferma= new JButton("Applica");
		annulla= new JButton("Annulla");
		avanti= new JButton("Avanti");

		class Operazione_applica_Modello implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ArrayList<Progetto> ritorno= new ArrayList<>();
				controllo= true;
				ArrayList<Variabile> variabili_progetto= new ArrayList<>();
				if (applicare.getTipologia_distanza().equals("Euclidea")) {
					System.out.println("Mi trovo nel calcolo del modello euclideo");
					for (Progetto p: finali) {
						variabili_progetto= p.getVariabili_progetto();
						ritorno= gestore.distanza_euclidea_semplice(variabili_progetto, applicare.getK(), testing);
						for (Variabile v: variabili_progetto) {
							if(v.getNome().equals("ID")) {
								//Memorizzo nel file di indirizzo la tripla necessaria
								ID= v.getValore();
							}

							if(v.getNome().equals("Effort")) {
								//Effuttuo il salvataggio del valore Reale
								reale= v.getValore();
								valori_effort.add(reale);
								print.println(v.getValore());
							}

						}
						if (applicare.getTipologia_analisi().equals("Media")) {
							effort_predetto= gestore.calcola_media(ritorno);
							//Effettuo il salvataggio del valore Predetto
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else if (applicare.getTipologia_analisi().equals("Mediana")) {
							effort_predetto= gestore.calcola_Mediana(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else {
							effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}
					}
					area.append("\n");
					area.append("Modello calcolato con successo, 'Avanti' per continuare");
				}else if (applicare.getTipologia_distanza().equals("Euclidea Ponderata")) {
					System.out.println("Mi trovo nel calcolo dell'euclidea Ponderata");
					ArrayList<Variabile> vars= new ArrayList<>();
					Progetto rif= finali.get(0);
					vars= rif.getVariabili_progetto();
					/*
					for (Variabile v: vars) {
						int valore= Integer.parseInt(JOptionPane.showInputDialog("Digita peso di: " + v.getNome()));
						pesi.add(valore);
					}
					 */
					for (Progetto p: finali) {
						variabili_progetto= p.getVariabili_progetto();
						ritorno= gestore.distanza_euclidea_ponderata(variabili_progetto, applicare.getK(), pesi_riferimento, testing);
						for (Variabile v: variabili_progetto) {
							if(v.getNome().equals("ID")) {
								//Memorizzo nel file di indirizzo la tripla necessaria
								ID= v.getValore();
							}

							if(v.getNome().equals("Effort")) {
								//Effuttuo il salvataggio del valore Reale
								reale= v.getValore();
								valori_effort.add(reale);
								print.println(v.getValore());
							}
						}
						if (applicare.getTipologia_analisi().equals("Media")) {
							effort_predetto= gestore.calcola_media(ritorno);
							//Effettuo il salvataggio del valore Predetto
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else if (applicare.getTipologia_analisi().equals("Mediana")) {
							effort_predetto= gestore.calcola_Mediana(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else {
							effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}
					}
					area.append("\n");
					area.append("Modello calcolato con successo, 'Avanti' per continuare");
				}else if (applicare.getTipologia_distanza().equals("Manhattan")) {
					for (Progetto p: finali) {
						variabili_progetto= p.getVariabili_progetto();
						ritorno= gestore.calcola_distanza_Manhattan(variabili_progetto, applicare.getK(), testing);
						for (Variabile v: variabili_progetto) {
							if(v.getNome().equals("ID")) {
								//Memorizzo nel file di indirizzo la tripla necessaria
								ID= v.getValore();
							}

							if(v.getNome().equals("Effort")) {
								//Effuttuo il salvataggio del valore Reale
								reale= v.getValore();
								valori_effort.add(reale);
								print.println(v.getValore());
							}
						}
						if (applicare.getTipologia_analisi().equals("Media")) {
							effort_predetto= gestore.calcola_media(ritorno);
							//Effettuo il salvataggio del valore Predetto
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else if (applicare.getTipologia_analisi().equals("Mediana")) {
							effort_predetto= gestore.calcola_Mediana(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else {
							effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}
					}
					area.append("\n");
					area.append("Modello calcolato con successo, 'Avanti' per continuare");
				}else {
					System.out.println("Mi trovo nel calcolo secondo Minkowski");
					int valore= gestore.ritorna_variabile_Minkowski();
					for (Progetto p: finali) {
						variabili_progetto= p.getVariabili_progetto();
						ritorno= gestore.distanza_Minkowski(variabili_progetto, applicare.getK(), valore, testing);
						for (Variabile v: variabili_progetto) {
							if(v.getNome().equals("ID")) {
								//Memorizzo nel file di indirizzo la tripla necessaria
								ID= v.getValore();
							}

							if(v.getNome().equals("Effort")) {
								//Effuttuo il salvataggio del valore Reale
								reale= v.getValore();
								valori_effort.add(reale);
								print.println(v.getValore());
							}
						}
						if (applicare.getTipologia_analisi().equals("Media")) {
							effort_predetto= gestore.calcola_media(ritorno);
							//Effettuo il salvataggio del valore Predetto
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else if (applicare.getTipologia_analisi().equals("Mediana")) {
							effort_predetto= gestore.calcola_Mediana(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}else {
							effort_predetto= gestore.calcola_Inverse_Rank_Mean(ritorno);
							print.println(effort_predetto);
							valori_predetto.add(effort_predetto);
							print2.println("ID-> " + ID + " Effort reale-> " + reale + " Effort predetto-> " + effort_predetto);
						}
					}
					area.append("\n");
					area.append("Modello calcolato con successo, 'Avanti' per continuare");
				}
			}
		}
		ActionListener listener= new Operazione_applica_Modello();
		conferma.addActionListener(listener);

		class Operazione_Indietro implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				controllo= false;
				JFrame frame= new Menu_Frame(gestore);
				dispose();
			}
		}
		ActionListener listener2= new Operazione_Indietro();
		annulla.addActionListener(listener2);

		class Operazione_MRE implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (controllo==true) {
					try {
						JFrame frame= new Visualizza_Valori_Potenza_Predittiva(gestore);
						System.out.println("Calcolo e stamo i risultati: ");
						for (int i=0; i<valori_effort.size(); i++) {
							float valori_scarti= Math.abs(valori_effort.get(i)-valori_predetto.get(i));
							System.out.println("Scarto calcolato: " + valori_scarti);
							if (valori_scarti==0.0) {
								System.out.println("Trovata corrispondenza con lo 0 non si è risolto un beata minchia");
							}
						}
						dispose();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showConfirmDialog(null, "Devi prima applicare il modello poi calcolare MDR o MdRE");
				}
			}
		}
		ActionListener listener3= new Operazione_MRE();
		avanti.addActionListener(listener3);

		sud_panel.add(conferma);
		sud_panel.add(annulla);
		sud_panel.add(avanti);
		add(sud_panel, BorderLayout.SOUTH);
	}
}