package com.molinari.mp3.business.lookup;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.molinari.mp3.business.acoustid.AudioTrack;
import com.molinari.mp3.business.acoustid.TagAudioTrack;

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
		LookUp client = new LookUp("5qZgj0WcWA0");
		String pathname = "/temp/";
		String[] list = new File(pathname).list();

		for (String string : list) {
			String pathnameFile = pathname + string;
			File file = new File(pathnameFile);
			client.lookup(file);
		}
	}

	public AudioTrack parseResult(String json, final int targetDuration) throws IOException {
		System.out.println(json);
		return readJson(json);
	}

	private AudioTrack readJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, AudioTrack.class);
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

	public TagAudioTrack lookup(File file) throws Exception {

		Map<ChromaprintField, String> fp = fpcalc(file);

		// sanity check
		if (!fp.containsKey(ChromaprintField.DURATION) || !fp.containsKey(ChromaprintField.FINGERPRINT)){
			return null;
		}

		int duration = Integer.parseInt(fp.get(ChromaprintField.DURATION));
		String fingerprint = fp.get(ChromaprintField.FINGERPRINT);

		// sanity check
		if (duration < 10){
			return null;
		}

		String response = lookup(duration, fingerprint);
		if (response != null && response.length() > 0) {
			 AudioTrack parseResult = parseResult(lookup(duration, fingerprint), duration);
			 return new TagAudioTrack(parseResult);
		}

		return null;
	}
}
