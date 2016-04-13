package fr.mgargadennec.blossom.core.common.support.mail;

import javax.mail.MessagingException;

import org.thymeleaf.context.Context;

public interface IBlossomMailService {
	
	void sendMail(String htmlTemplate, Context ctx, String mailSubject, String... mailTo) throws MessagingException;

}
