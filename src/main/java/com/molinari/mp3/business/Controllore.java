package com.molinari.mp3.business;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.molinari.mp3.business.op.IOperazioni;
import com.molinari.mp3.views.Vista;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.database.ConnectionPool;
import com.molinari.utility.database.ExecuteResultSet;
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
					ControlloreBase.getSingleton().setConnectionClassName(ConnectionMp3.class.getName());
					Controllore.getSingleton();
					
					verificaPresenzaDb();
					
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
	
	private static void verificaPresenzaDb() {
		final String sql = "SELECT MAX(IDSONG) FROM SONG";
		try {
			ExecuteResultSet<Boolean> executeResultSet = new ExecuteResultSet<Boolean>() {

				@Override
				protected Boolean doWithResultSet(ResultSet resulSet) throws SQLException {
					return resulSet.next();
				}
			};
			executeResultSet.execute(sql);
		} catch (final SQLException e) {
			getLog().log(Level.SEVERE, "Il database non c'è ancora, è da creare!", e);
			try {
				generaDB();
			} catch (final SQLException e1) {
				getLog().log(Level.SEVERE, "Error on Db creation: " + e1.getMessage(), e1);
				File file = new File(ConnectionMp3.DB_FILE_URL);
				file.deleteOnExit();
			}
			getLog().severe(e.getMessage());
		}
	}


	private static void generaDB() throws SQLException {
		String sql = "CREATE TABLE \"SONG\" ( `IDSONG` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `NAME` TEXT NOT NULL, `GENRE` TEXT, `COMMENTS` TEXT, `TRACKNUMBER` INTEGER, `KEY` TEXT, `FPDURATION` INTEGER NOT NULL, `ARTIST` TEXT NOT NULL, `ALBUM` TEXT );";
		final ConnectionPool cp = ConnectionPool.getSingleton();
		cp.executeUpdate(sql);
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
		ControlloreBase.getLog().setLevel(Level.SEVERE);
		Controllore.getSingleton().myMain(Controllore.getSingleton(), true, "myApplication");
	}

	@Override
	public String getConnectionClassName() {
		return ConnectionMp3.class.getName();
	}

}
