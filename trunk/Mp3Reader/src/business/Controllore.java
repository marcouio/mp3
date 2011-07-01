package business;

import java.awt.EventQueue;

import vista.Vista;
import business.operazioni.IOperazioni;

public class Controllore {
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
					// GraficaUtil.setLook("Nimbus");
					vista = new Vista();
					Controllore.getSingleton();
					vista.pack();
					vista.setResizable(true);
					vista.setTitle("Gestionale Mp3");
					vista.setLocationByPlatform(true);
					vista.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
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
