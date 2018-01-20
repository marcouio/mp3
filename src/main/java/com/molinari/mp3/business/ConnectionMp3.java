package com.molinari.mp3.business;

import org.sqlite.JDBC;

import com.molinari.utility.database.ConnectionPool;

public class ConnectionMp3 extends ConnectionPool {

	public static final String DB_URL_WORKSPACE = "../mp3.sqlite";
	public static final String DB_URL_JAR = "./mp3.sqlite";
//	private static String url = "jdbc:sqlite:" + DB_URL_WORKSPACE;
	private static String url = "jdbc:sqlite:C:/Users/molinaris/workspace/mp3.sqlite" ;
	
	@Override
	protected String getPassword() {
		return null;
	}

	@Override
	protected String getUser() {
		return null;
	}

	@Override
	protected String getDriver() {
		return JDBC.class.getName();
	}

	@Override
	protected String getDbUrl() {
		return url;
	}

	@Override
	protected int getNumeroConnessioniDisponibili() {
		return 5;
	}

}
