package org.mylearning.camel.file2db.route;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.FileSystemUtils;

public class SimpleCamelMockRouteJUnitTests extends CamelTestSupport {
    private static Properties props;
    private static final String DUMMY_QUERY = "SELECT 'Hello world'";

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
        getMockEndpoint("mock:jdbc:dataSource").expectedBodiesReceived(DUMMY_QUERY);
        template.sendBody(String.format("file:%s?delete=true&readLock=none", props.getProperty("input_folder")), body);
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        SimpleCamelRoute route = new SimpleCamelRoute();
        Processor buildSQLProcessor = mock(Processor.class);
        route.setBuildSQLProcessor(buildSQLProcessor);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock arg) throws Throwable {
                Exchange exchange = arg.getArgument(0);
                exchange.getIn().setBody(DUMMY_QUERY);
                return null;
            }
        }).when(buildSQLProcessor).process(any(Exchange.class));
        return route;
    }

    @Override
    public String isMockEndpoints() {
        return "*";
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        return props;
    }

    @Override
    protected void bindToRegistry(Registry registry) throws Exception {
        DataSource ds = new DriverManagerDataSource("jdbc:h2:mem:testdb");
        registry.bind("dataSource", ds);
        super.bindToRegistry(registry);
    }

}
