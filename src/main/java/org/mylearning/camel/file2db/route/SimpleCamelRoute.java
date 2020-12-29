package org.mylearning.camel.file2db.route;

import javax.sql.DataSource;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.mylearning.camel.file2db.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Qualifier("buildSQLProcessor")
    @Autowired
    private Processor buildSQLProcessor;

    @Override
    public void configure() throws Exception {
        log.info("Inside of configure method");

        DataFormat dFormat = new BindyCsvDataFormat(Item.class);
        from("timer:hello?period={{poll_interval}}").log("Time invoked")
                .pollEnrich("file:{{input_folder}}?delete=true&readLock=none").log("Body is: ${body}")
                .to("file:{{output_folder}}").unmarshal(dFormat).log("Unmarshalled object is: ${body}").split(body())
                .log("Record is: ${body}").process(buildSQLProcessor).to("jdbc:dataSource").end();
    }

}
