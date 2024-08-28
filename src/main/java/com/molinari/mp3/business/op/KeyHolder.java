package com.molinari.mp3.business.op;

public class KeyHolder {
		
		private static final KeyHolder singleton = new KeyHolder(); 
		
		private KeyHolder() {
			//do nothing
		}
		
		private String key = "_Q6hoFWgAAc";

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public static KeyHolder getSingleton() {
			return singleton;
		}
		
	}
