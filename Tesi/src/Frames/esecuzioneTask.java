package Frames;

import java.util.ArrayList;

import javax.swing.JFrame;

import Bean.GestoreStruttura;
import Bean.Progetto;
import Bean.Variabile;

public class esecuzioneTask {
	public static void main(String args[]) {
		GestoreStruttura gestore= new GestoreStruttura();
		gestore.carica_struttura();
		
		JFrame frame= new IndicazioniFrame(gestore);
	}
}