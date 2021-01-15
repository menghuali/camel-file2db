package org.mylearning.camel.file2db.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.mylearning.camel.file2db.domain.Item;
import org.mylearning.camel.file2db.processor.DataException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    private Processor buildSQLProcessor;
    private Processor mailProcessor;

    @Override
    public void configure() throws Exception {
        log.info("Inside of configure method");

        // errorHandler(deadLetterChannel("log:errorInRoute?level=ERROR&showProperties=true").maximumRedeliveries(3)
        // .redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR));

        onException(DataException.class).log(LoggingLevel.ERROR, "DataException is: ${body}").process(mailProcessor);
        onException(PSQLException.class).log(LoggingLevel.ERROR, "PSQLException is: ${body}").maximumRedeliveries(2)
                .redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR)
                .process(mailProcessor);

        DataFormat dFormat = new BindyCsvDataFormat(Item.class);
        from("timer:hello?period={{poll_interval}}").log("Time invoked")
                .pollEnrich("file:{{input_folder}}?delete=true&readLock=none&moveFailed=error").log("Body is: ${body}")
                .to("file:{{output_folder}}").unmarshal(dFormat).log("Unmarshalled object is: ${body}").split(body())
                .log("Record is: ${body}").process(buildSQLProcessor).to("jdbc:dataSource").end();
    }

    @Qualifier("buildSQLProcessor")
    @Autowired
    public void setBuildSQLProcessor(Processor buildSQLProcessor) {
        this.buildSQLProcessor = buildSQLProcessor;
    }

    @Qualifier("mailProcessor")
    @Autowired
    public void setMailProcessor(Processor mailProcessor) {
        this.mailProcessor = mailProcessor;
    }

}
