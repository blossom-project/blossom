package fr.mgargadennec.blossom.core.common.utils.mail;

import java.util.Map;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
public interface MailSender {

  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo) throws Exception;

}
