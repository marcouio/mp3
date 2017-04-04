package com.molinari.mp3.business.lookup;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.molinari.mp3.business.acoustid.AudioTrack;

public class LookUp {

	private String apikey;

	public LookUp(String string) {
		this.apikey = string;
	}

	public String lookup(int duration, String fingerprint) throws IOException {

		URL url = createUrl(duration, fingerprint);
		return getInputStreamFromUrl(url);
	}

	private URL createUrl(int duration, String fingerprint) throws MalformedURLException {
		StringBuilder sb = new StringBuilder();
		sb.append("http://api.acoustid.org/v2/lookup?client=");
		sb.append(apikey);
		sb.append("&meta=recordings+releasegroups+compress&fingerprint=");
		sb.append(fingerprint);
		sb.append("&duration=");
		sb.append(String.valueOf(duration));
		return new URL(sb.toString());
	}

	private String getInputStreamFromUrl(URL url) throws IOException, ProtocolException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder results = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            results.append(line);
        }

        connection.disconnect();
		
        return results.toString();
	}

	public static void main(String[] args) throws Exception {

//		LookUp client = new LookUp("hDMsDzihFA");
		LookUp client = new LookUp("eRiLd6LkRAg");
		String pathname = "/temp/";
		String[] list = new File(pathname).list();
		List<File> files = new ArrayList<>();

		for (String string : list) {
			String pathnameFile = pathname + string;
			File file = new File(pathnameFile);
			files.add(file);
		}
		client.lookup(files);
	}

	public AudioTrack parseResult(String json, final int targetDuration) throws IOException {
		Object data = readJson(json);

		return null;
	}

	private Object readJson(String json) {
		
		// TODO Auto-generated method stub
		return null;
	}

	public enum ChromaprintField {
		FILE, FINGERPRINT, DURATION;
	}

	public String getChromaprintCommand() {
		System.setProperty("com.molinari.AcoustID.fpcalc", "C:/Users/Marco/workspace/Mp3Reader/lib/native/win32-x64/fpcalc.exe");
		// use fpcalc executable path as specified by the cmdline or default to
		// "fpcalc" and let the shell figure it out
		return System.getProperty("com.molinari.AcoustID.fpcalc", "fpcalc");
	}

	public Map<ChromaprintField, String> fpcalc(File file) throws IOException, InterruptedException {
		Map<ChromaprintField, String> output = new EnumMap<>(ChromaprintField.class);

		final ProcessBuilder processBuilder = new ProcessBuilder(getChromaprintCommand(), null);
		processBuilder.redirectErrorStream(true);
		processBuilder.command().set(1, file.getAbsolutePath());
		final Process fpcalcProc = processBuilder.start();

		try (Scanner scanner = new Scanner(new InputStreamReader(fpcalcProc.getInputStream(), UTF_8))) {
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				System.out.println(nextLine);
				String[] value = RegularExpressions.EQUALS.split(nextLine, 2);
				if (value.length == 2) {
					try {
						output.put(ChromaprintField.valueOf(value[0]), value[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return output;
	}

	public Map<File, AudioTrack> lookup(Collection<File> files) throws Exception {
		Map<File, AudioTrack> results = new LinkedHashMap<>();

		for (File file : files) {
			Map<ChromaprintField, String> fp = fpcalc(file);

			// sanity check
			if (!fp.containsKey(ChromaprintField.DURATION) || !fp.containsKey(ChromaprintField.FINGERPRINT))
				continue;

			int duration = Integer.parseInt(fp.get(ChromaprintField.DURATION));
			String fingerprint = fp.get(ChromaprintField.FINGERPRINT);

			// sanity check
			if (duration < 10)
				continue;

			String response = lookup(duration, fingerprint);
			if (response != null && response.length() > 0) {
				results.put(file, parseResult(lookup(duration, fingerprint), duration));
			}
		}

		return results;
	}

	private static class MostFieldsNotNull implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			return Integer.compare(count(o2), count(o1));
		}

		public int count(Object o) {
			int n = 0;
			try {
				for (Field field : o.getClass().getDeclaredFields()) {
					if (field.get(o) != null) {
						n++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return n;
		}
	}
}
