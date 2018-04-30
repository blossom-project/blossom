package com.blossomproject.core.common.utils.mail;

import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.NewsAddress;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MailFilterImplTest {

    private MailFilter mailFilter;


    @Before
    public void SetUp(){
        this.mailFilter = new MailFilterImpl(Sets.newHashSet(".*@test.com"),"blossom-project@blossom.com");

    }


    @Test
    public void filter() throws Exception {
        final MimeMessage mimeMessage = mock(MimeMessage.class);
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        Address[] addresses = {new InternetAddress("test@test.com"),new InternetAddress("filter@filter.com")};

        doReturn(new Address[]{new InternetAddress("test@test.com")}).when(mimeMessage).getRecipients(Message.RecipientType.TO);
        doReturn(new Address[]{new InternetAddress("filter@filter.com")}).when(mimeMessage).getRecipients(Message.RecipientType.CC);
        doReturn(null).when(mimeMessage).getRecipients(Message.RecipientType.BCC);
        helper.setSubject("subject");
        helper.setText("htmlContent", true);
        MimeMessage result =   this.mailFilter.filter(helper);
        assertEquals(1, helper.getMimeMessage().getRecipients(Message.RecipientType.TO).length);
    }

}
