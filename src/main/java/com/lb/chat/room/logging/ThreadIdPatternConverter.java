package com.lb.chat.room.logging;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

public class ThreadIdPatternConverter extends PatternConverter {

	@Override
	protected String convert(LoggingEvent evt) {
		return new Long(Thread.currentThread().getId()).toString();
	}

}