package com.molinari.mp3.business;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.views.Vista;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.graphic.component.container.FrameBase;
import com.molinari.utility.log.LoggerOggetto;

public class Controllore extends ControlloreBase {
	public static final String APPLICATION_NAME = "MP3";
	private static int operazione = IOperazioni.DEFAULT;
	private static Controllore singleton;
	private static Vista vista;

	private Controllore() {

	}
	
	
	public static Controllore getSingleton() {
		if (singleton == null) {
			singleton = new Controllore();
		}
		return singleton;
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

	@Override
	public void mainOverridato(FrameBase frame) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					Controllore.getSingleton();
					
					frame.getContentPane().setLayout(null);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setPreferredSize(new Dimension(674, 337));
					vista = new Vista(frame);
					
					vista.setBorder(new EmptyBorder(5, 5, 5, 5));
					frame.setContentPane(vista);
					
					frame.pack();
					frame.setResizable(true);
					frame.setTitle("Gestionale Mp3");
					frame.setLocationByPlatform(true);
					frame.setVisible(true);
					
					setLook("Nimbus");
				} catch (final Exception e) {
					getLog().log(Level.SEVERE, e.getMessage(), e);
				}
			}
		});
			
	}

	public void setLook(final String look) {
		final LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
		for (int i = 0; i < looks.length; i++) {
			if (looks[i].getName().equals(look)) {
				try {
					UIManager.setLookAndFeel(looks[i].getClassName());
				} catch (final Exception e) {
					Controllore.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		SwingUtilities.updateComponentTreeUI(vista);
	}
	
	public static void main(final String[] args) {
		Controllore.getSingleton().myMain(Controllore.getSingleton(), true, "myApplication");
	}

	@Override
	public String getConnectionClassName() {
		return null;
	}

}
