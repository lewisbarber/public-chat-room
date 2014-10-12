package com.lb.chat.room.logging;

import org.apache.log4j.helpers.PatternParser;

public class CustomPatternParser extends PatternParser {

	private static final char THREAD_ID = 'i';

	public CustomPatternParser(String pattern) {
		super(pattern);
	}

	@Override
	protected void finalizeConverter(char c) {
		switch (c) {
		case THREAD_ID:
			currentLiteral.setLength(0);
			addConverter(new ThreadIdPatternConverter());
			break;
		default:
			super.finalizeConverter(c);
		}
	}
}