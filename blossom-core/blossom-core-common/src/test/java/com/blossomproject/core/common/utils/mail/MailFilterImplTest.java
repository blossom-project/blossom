package com.blossomproject.core.common.utils.mail;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.rmi.server.ExportException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class MailFilterImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private MailFilter mailFilter;


    @Before
    public void SetUp() {
        this.mailFilter = new MailFilterImpl(Sets.newHashSet(".*@test.com"), "blossom-project@blossom.com");

    }


    @Test
    public void filter_with_cc_bcc() throws Exception {
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        doReturn(new Address[]{new InternetAddress("test@test.com")}).when(mimeMessage).getRecipients(Message.RecipientType.TO);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.CC);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.BCC);
        helper.setSubject("subject");
        helper.setText("htmlContent", true);
        this.mailFilter.filter(helper);
    }

    @Test
    public void no_filter_cc_bcc() throws Exception {
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        doReturn(new Address[]{new InternetAddress("test@test.com")}).when(mimeMessage).getRecipients(Message.RecipientType.TO);
        doReturn(new Address[]{new InternetAddress("test@test.com")}).when(mimeMessage).getRecipients(Message.RecipientType.CC);
        doReturn(new Address[]{new InternetAddress("test@test.com")}).when(mimeMessage).getRecipients(Message.RecipientType.BCC);
        helper.setSubject("subject");
        helper.setText("htmlContent", true);
        this.mailFilter.filter(helper);
    }

    @Test
    public void filter_to() throws Exception{
        thrown.expect(MessagingException.class);
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.TO);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.CC);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.BCC);
        helper.setSubject("subject");
        helper.setText("htmlContent", true);
        this.mailFilter.filter(helper);
    }

    @Test
    public void no_filter() throws Exception{
        MailFilter mailFilterNoFilter = new MailFilterImpl(null,"blossom-project@blossom.com");
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        doReturn(new Address[]{new InternetAddress("test@test.com")}).when(mimeMessage).getRecipients(Message.RecipientType.TO);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.CC);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.BCC);
        helper.setSubject("subject");
        helper.setText("htmlContent", true);
        mailFilterNoFilter.filter(helper);
    }

    @Test
    public void no_recipients() throws Exception{
        thrown.expect(MessagingException.class);
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        doReturn(null).when(mimeMessage).getRecipients(Message.RecipientType.TO);
        doReturn(null).when(mimeMessage).getRecipients(Message.RecipientType.CC);
        doReturn(null).when(mimeMessage).getRecipients(Message.RecipientType.BCC);
        helper.setSubject("subject");
        helper.setText("htmlContent", true);
        this.mailFilter.filter(helper);
    }

}
