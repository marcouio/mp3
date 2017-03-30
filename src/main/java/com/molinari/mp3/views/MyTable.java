package com.molinari.mp3.views;

import javax.swing.JTable;

import com.molinari.mp3.business.objects.Mp3;

public class MyTable extends JTable {

	private static final long serialVersionUID = 1L;

	public MyTable(final String[][] costruisciMatriceVuota, final String[] nomiColonne) {
		super(costruisciMatriceVuota, nomiColonne);
	}

	public MyTable(final Mp3[][] canzoni, final String[] nomiColonne) {
		super(canzoni, nomiColonne);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return false;
	}
}
