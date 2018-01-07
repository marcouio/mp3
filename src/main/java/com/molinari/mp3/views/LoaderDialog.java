package com.molinari.mp3.views;

import javax.swing.SwingWorker;

public class LoaderDialog extends SwingWorker<ConfirmAssignTag, Void>{

	ConfirmAssignTag confirm;
	
	public LoaderDialog(ConfirmAssignTag confirm) {
		this.confirm = confirm;
		confirm.getDialog().setVisible(true);
	}

	@Override
	protected ConfirmAssignTag doInBackground() throws Exception {
		while(confirm.getBeanAssign().getArtist() == null) {
			//do nothing
		}
		return confirm;
	}
	
	@Override
	protected void done() {
		confirm.getDialog().setVisible(false);
	}

}
