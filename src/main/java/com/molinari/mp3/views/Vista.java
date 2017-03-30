package main.java.com.molinari.mp3.views;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

public class Vista extends JFrame {

	private static final long serialVersionUID = -1455195023357489172L;
	private JPanel contentPane;
	private NewPlayList playList;
	private Pannello pannello;
	private JPanel panForGestore;
	private JPanel panForPlayer;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final Vista frame = new Vista();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Vista() {
		getContentPane().setLayout(null);
		getContentPane().setLayout(null);
		setLook("Nimbus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(674, 337));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		initMenu();

		initGestore();
		initPlayer();

	}

	private void initPlayer() {
		panForPlayer = new JPanel();
		panForPlayer.setLayout(null);
		contentPane.add(panForPlayer);
		panForPlayer.setBounds(283, 31, 400, 317);
		playList = NewPlayList.getSingleton();
		playList.setBounds(0, 0, 450, 257);
		panForPlayer.add(playList);
	}

	private void initGestore() {
		panForGestore = new JPanel();
		panForGestore.setLayout(null);
		panForGestore.setBounds(10, 31, 278, 300);
		contentPane.add(panForGestore);
		pannello = Pannello.getSingleton();
		pannello.setBounds(0, 0, 258, 257);
		panForGestore.add(pannello);
	}

	private void initMenu() {
		contentPane.setLayout(null);
		final MyMenu menu = new MyMenu();
		menu.setBounds(0, 0, 1000, 20);
		contentPane.add(menu);

	}

	public NewPlayList getPlayList() {
		return playList;
	}

	public void setPlayList(final NewPlayList playList) {
		this.playList = playList;
	}

	public Pannello getPannello() {
		return pannello;
	}

	public void setPannello(final Pannello pannello) {
		this.pannello = pannello;
	}

	public void setLook(final String look) {
		final LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
		for (int i = 0; i < looks.length; i++) {
			if (looks[i].getName().equals(look)) {
				try {
					UIManager.setLookAndFeel(looks[i].getClassName());
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public JPanel getPanForGestore() {
		return panForGestore;
	}

	public void setPanForGestore(final JPanel panForGestore) {
		this.panForGestore = panForGestore;
	}

	public JPanel getPanForPlayer() {
		return panForPlayer;
	}

	public void setPanForPlayer(final JPanel panForPlayer) {
		this.panForPlayer = panForPlayer;
	}
}
