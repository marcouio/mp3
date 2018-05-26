package com.molinari.mp3.business.op;

import java.io.File;
import java.text.MessageFormat;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.utility.io.UtilIo;

public class UtilOp {

	private UtilOp() {
		// do nothing
	}
	
	public static String adjust(String nome){
		return nome.replaceAll("/", "")
				.replaceAll(":", "")
				.replaceAll("\"", "")
				.replace("(", "")
				.replace(")", "");
	}
	
	public static boolean rename(final File mp3, final String nomedopo) {
		if(!mp3.getAbsolutePath().equals(nomedopo)){
			try{
				UtilIo.moveFile(mp3, new File(nomedopo));
				java.nio.file.Files.deleteIfExists(mp3.toPath());
				String format = MessageFormat.format("-> Mp3 rinominato in: {0}", nomedopo);
				Controllore.getLog().info(format);
				return true;
			}catch (Exception e) {
				Controllore.getLog().severe("-> Cambio nome non riuscito");
				Controllore.getLog().severe(e.getMessage());
				throw new Mp3Exception(e);
			}
		}
		Controllore.getLog().info("-> Mp3 non rinominato perché già con lo stesso nome");
		return false;
	}
}
