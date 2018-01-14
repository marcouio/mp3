package com.molinari.mp3.business.op.binder;

public class Mp3File{
	private String nome;
	private String path;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}