package com.lb.chat.room.logging;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

public class CustomPatternLayout extends PatternLayout {
	protected PatternParser createPatternParser(String pattern) {
		return new CustomPatternParser(pattern);
	}
}