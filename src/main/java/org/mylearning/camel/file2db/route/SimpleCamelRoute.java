package org.mylearning.camel.file2db.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:hello?period={{poll_interval}}").log("Time invoked")
                .pollEnrich("file:{{input_folder}}?delete=true&readLock=none").log("Body is: ${body}")
                .to("file:{{output_folder}}");
    }

}
