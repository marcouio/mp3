package com.molinari.mp3.business.lookup;

import static java.util.regex.Pattern.LITERAL;
import static java.util.regex.Pattern.UNICODE_CHARACTER_CLASS;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public class RegularExpressions {
	
	private RegularExpressions() {
		// do nothing
	}

	public static final Pattern DIGIT = compile("\\d+");
	public static final Pattern NON_DIGIT = compile("\\D+");
	public static final Pattern NON_WORD = compile("[\\P{Alnum}]+");

	public static final Pattern PIPE = compile("|", LITERAL);
	public static final Pattern EQ = compile("=", LITERAL);
	public static final Pattern TAB = compile("\t", LITERAL);
	public static final Pattern SEMICOLON = compile(";", LITERAL);

	public static final Pattern COMMA = compile("\\s*[,;:]\\s*", UNICODE_CHARACTER_CLASS);
	public static final Pattern RATIO = compile("(?<=\\w)[:\u2236](?=\\w)", UNICODE_CHARACTER_CLASS);
	public static final Pattern COLON = compile("\\s*[:]+\\s*", UNICODE_CHARACTER_CLASS);
	public static final Pattern SLASH = compile("\\s*[\\\\/]+\\s*", UNICODE_CHARACTER_CLASS);
	public static final Pattern SPACE = compile("\\s+", UNICODE_CHARACTER_CLASS); // French No-Break Space U+00A0

	public static final Pattern NEWLINE = compile("\\R+", UNICODE_CHARACTER_CLASS);

}