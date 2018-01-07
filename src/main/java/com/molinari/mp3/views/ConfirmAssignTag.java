package com.molinari.mp3.views;

import java.awt.Dialog.ModalityType;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.molinari.mp3.business.objects.Tag;
import com.molinari.utility.graphic.component.alert.Alert;
import com.molinari.utility.graphic.component.alert.DialogoBase;
import com.molinari.utility.graphic.component.button.ButtonBase;
import com.molinari.utility.graphic.component.label.Label;
import com.molinari.utility.graphic.component.textfield.TextFieldBase;

public class ConfirmAssignTag {

	private DialogoBase dialog; 
	private Label labArtist;
	private Label labSong;
	private Label labTrack;
	private TextFieldBase tfArtist;
	private TextFieldBase tfSong;
	private TextFieldBase tfTrack;
	private BeanAssign beanAssign = new BeanAssign();
	private Label labAlbum;
	private TextFieldBase tfAlbum;
	private ButtonBase btnFile;

	public ConfirmAssignTag(JFrame frame, Tag result, String pathFile) {
		createBody(frame, result, pathFile);
	}

	private void createBody(JFrame frame, Tag result, String pathFile) {
		this.dialog = new DialogoBase(frame);
		this.dialog.setBounds(frame.getX(), frame.getY(), 42, 400);
		this.dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// questo permette di mantenere il focus sulla dialog
		this.dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		this.dialog.setLayout(null);
		this.dialog.setTitle("Confirm new tag");
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("File: ");
		sb.append(pathFile);

		btnFile = new ButtonBase("Click for path file", getDialog().getContentPane()); 
		btnFile.setBounds(0, 0, 100, 30);
		btnFile.setSize(100, 30);
		btnFile.addActionListener(e -> {
			Alert.info(sb.toString(), "Path File");
		});
		
		labArtist = new Label("Artist", dialog.getContentPane());
		labArtist.posizionaSottoA(btnFile, 0, 10);
		labArtist.setSize(100, 30);
		
		tfArtist = new TextFieldBase(result.getArtistaPrincipale(), this.getDialog().getContentPane());
		tfArtist.posizionaSottoA(labArtist, 0, 10);
		tfArtist.setSize(100, 30);
		
		labSong = new Label("Song", dialog.getContentPane());
		labSong.posizionaSottoA(tfArtist, 0, 10);
		labSong.setSize(100, 30);
		
		tfSong = new TextFieldBase(result.getTitoloCanzone(), dialog.getContentPane());
		tfSong.posizionaSottoA(labSong, 0, 0);
		tfSong.setSize(100, 30);
		
		labAlbum = new Label("Album", dialog.getContentPane());
		labAlbum.posizionaSottoA(tfSong, 0, 0);
		labAlbum.setSize(100, 30);
		
		tfAlbum = new TextFieldBase(result.getTraccia(), dialog.getContentPane());
		tfAlbum.posizionaSottoA(labAlbum, 0, 0);
		tfAlbum.setSize(100, 30);
		
		labTrack = new Label("Track", dialog.getContentPane());
		labTrack.posizionaSottoA(tfAlbum, 0, 0);
		labTrack.setSize(100, 30);
		
		tfTrack = new TextFieldBase(result.getTraccia(), dialog.getContentPane());
		tfTrack.posizionaSottoA(labTrack, 0, 0);
		tfTrack.setSize(100, 30);
		
		ButtonBase button = new ButtonBase("Confirm", dialog.getContentPane());
		button.posizionaSottoA(tfTrack, 0, 10);
		button.setSize(100, 30);
		
		button.addActionListener(e -> {
			
			if(tfArtist.getText() != null && tfSong.getText() != null) {
				
				beanAssign.setArtist(tfArtist.getText());
				beanAssign.setSong(tfSong.getText());
				beanAssign.setTrack(tfTrack.getText());
				beanAssign.setAlbum(tfAlbum.getText());
				getDialog().dispose();
			}else {
				Alert.segnalazioneErroreWarning("Artist and Song have to be valued");
			}
			
		});
		
	}
	
	public static class BeanAssign{
		private String track;
		private String artist;
		private String song;
		private String album;
		
		public String getTrack() {
			return track;
		}
		public void setTrack(String track) {
			this.track = track;
		}
		public String getArtist() {
			return artist;
		}
		public void setArtist(String artist) {
			this.artist = artist;
		}
		public String getSong() {
			return song;
		}
		public void setSong(String song) {
			this.song = song;
		}
		public String getAlbum() {
			return album;
		}
		public void setAlbum(String album) {
			this.album = album;
		}
	}

	public DialogoBase getDialog() {
		return dialog;
	}

	public void setDialog(DialogoBase dialog) {
		this.dialog = dialog;
	}

	public Label getLabArtist() {
		return labArtist;
	}

	public void setLabArtist(Label labArtist) {
		this.labArtist = labArtist;
	}

	public Label getLabSong() {
		return labSong;
	}

	public void setLabSong(Label labSong) {
		this.labSong = labSong;
	}

	public Label getLabTrack() {
		return labTrack;
	}

	public void setLabTrack(Label labTrack) {
		this.labTrack = labTrack;
	}

	public TextFieldBase getTfArtist() {
		return tfArtist;
	}

	public void setTfArtist(TextFieldBase tfArtist) {
		this.tfArtist = tfArtist;
	}

	public TextFieldBase getTfSong() {
		return tfSong;
	}

	public void setTfSong(TextFieldBase tfSong) {
		this.tfSong = tfSong;
	}

	public TextFieldBase getTfTrack() {
		return tfTrack;
	}

	public void setTfTrack(TextFieldBase tfTrack) {
		this.tfTrack = tfTrack;
	}

	public Label getLabAlbum() {
		return labAlbum;
	}

	public void setLabAlbum(Label labAlbum) {
		this.labAlbum = labAlbum;
	}

	public BeanAssign getBeanAssign() {
		return beanAssign;
	}

	
	
}
