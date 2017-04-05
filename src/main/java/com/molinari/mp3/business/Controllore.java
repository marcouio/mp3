package com.molinari.mp3.business;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.views.Vista;
import com.molinari.utility.log.LoggerOggetto;

public class Controllore {
	public static final String APPLICATION_NAME = "MP3";
	private static int operazione = IOperazioni.DEFAULT;
	private static Controllore singleton;
	private static Vista vista;

	public static Controllore getSingleton() {
		if (singleton == null) {
			singleton = new Controllore();
		}
		return singleton;
	}

	private Controllore() {

	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					vista = new Vista();
					Controllore.getSingleton();
					vista.pack();
					vista.setResizable(true);
					vista.setTitle("Gestionale Mp3");
					vista.setLocationByPlatform(true);
					vista.setVisible(true);
				} catch (final Exception e) {
					getLog().log(Level.SEVERE, e.getMessage(), e);
				}
			}
		});
	}
	
	public static void log(Level level, String msg, Throwable e){
		getLog().log(level, msg, e);
	}

	public static Logger getLog() {
		return LoggerOggetto.getLog(APPLICATION_NAME);
	}

	public void setVista(final Vista vista) {
		Controllore.vista = vista;
	}

	public Vista getVista() {
		return vista;
	}

	public static int getOperazione() {
		return operazione;
	}

	public static void setOperazione(final int operazione) {
		Controllore.operazione = operazione;
	}

}
