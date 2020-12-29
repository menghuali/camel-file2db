package org.mylearning.camel.file2db.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        log.info("Inside of configure method");
        from("timer:hello?period={{poll_interval}}").log("Time invoked")
                .pollEnrich("file:{{input_folder}}?delete=true&readLock=none").log("Body is: ${body}")
                .to("file:{{output_folder}}");
    }

}
