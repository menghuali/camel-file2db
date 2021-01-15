package org.mylearning.camel.file2db.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("mailProcessor")
@Slf4j
public class MailProcessor implements Processor {

    private JavaMailSender mailSender;
    private String mailFrom;
    private String mailTo;

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception e = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.info("Exception caught in mail processor: {}: {}", e.getClass().getSimpleName(), e.getMessage());
        String messageBody = String.format("Exception happened in the Camel route: %s: %s",
                e.getClass().getSimpleName(), e.getMessage());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(mailTo);
        message.setSubject("Exception in Camel Route");
        message.setText(messageBody);
        try {
            mailSender.send(message);
        } catch (Exception ex) {
            throw ex;
        }
        log.info("Email sent out");
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setMailFrom(@Value("${mailfrom}") String mailFrom) {
        this.mailFrom = mailFrom;
    }

    @Autowired
    public void setMailTo(@Value("${mailto}") String mailTo) {
        this.mailTo = mailTo;
    }

}
