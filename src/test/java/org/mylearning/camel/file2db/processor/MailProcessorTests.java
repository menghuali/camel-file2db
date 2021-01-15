package org.mylearning.camel.file2db.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MailProcessorTests.TestApp.class)
public class MailProcessorTests {

    @SpringBootApplication(scanBasePackages = "org.mylearning.camel.file2db.processor")
    public static class TestApp {

    }

    @Autowired
    @Qualifier("mailProcessor")
    private Processor emailProcssor;

    @Test
    public void process() throws Exception {
        CamelContext ctx = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(ctx);
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("intended exception"));
        emailProcssor.process(exchange);
    }

}
