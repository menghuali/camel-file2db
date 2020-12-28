package org.mylearning.camel.file2db.route;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

public class SimpleCamelRouteJUnitTests extends CamelTestSupport {
    private static Properties props;

    @BeforeAll
    public static void beforeAll() {
        props = new Properties();
        props.put("poll_interval", "10s");
        props.put("input_folder", "data/input");
        props.put("output_folder", "data/output");
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of("data", "output"));
    }

    @Test
    public void moveFiles() throws InterruptedException {
        String body = "type,sku#,item_description,price\n" + "UPDATE,200,Samsung TV,500\n" + "UPDATE,201,LG TV,400";
        getMockEndpoint(String.format("mock:file:%s?delete=true&readLock=none", props.getProperty("input_folder")))
                .expectedBodiesReceived(body);
        getMockEndpoint(String.format("mock:file:%s", props.getProperty("output_folder"))).expectedBodiesReceived(body);
        template.sendBody(String.format("file:%s?delete=true&readLock=none", props.getProperty("input_folder")), body);
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SimpleCamelRoute();
    }

    @Override
    public String isMockEndpoints() {
        return "*";
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        return props;
    }

}
