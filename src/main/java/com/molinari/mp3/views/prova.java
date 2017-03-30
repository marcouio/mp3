package main.java.com.molinari.mp3.views;

import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class prova extends JPanel {

	/**
	 * Create the panel.
	 */
	public prova() {
		setLayout(null);

		final JSlider slider = new JSlider();
		slider.setBounds(10, 11, 200, 24);
		slider.setValue(0);
		slider.addMouseListener(new MouseAdapter() {
		});
		add(slider);

	}
}
