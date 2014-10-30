package com.neemre.hashly.common.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.neerme.hashly.common.ErrorCodes;

@Component("resourceWrapper")
public class ResourceWrapper {

	@Autowired
	private MessageSource messageSource;
	
	
	public String getErrorMessage(ErrorCodes error, Object[] args) {
		String msg = messageSource.getMessage(error.getMessageKey(), args, LocaleContextHolder
				.getLocale());
		return msg;
	}
}