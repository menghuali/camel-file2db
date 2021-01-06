package org.mylearning.camel.file2db.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.util.FileSystemUtils;

@CamelSpringBootTest
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(OrderAnnotation.class)
public class SimpleCamelRouteSpringBootTests {
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    Environment env;

    @BeforeEach
    public void beforeEach() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of("data", "output"));
    }

    @Test
    @Order(1)
    public void moveFile_ADD() throws InterruptedException {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        String body = "type,sku#,item_description,price\n" + "ADD,200,Samsung TV,500\n" + "ADD,201,LG TV,400";
        String fileName = "fileTest.txt";
        producerTemplate.sendBodyAndHeader("file:data/input", body, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);
        assertTrue(Files.exists(Path.of("data", "output", fileName)));
    }

    @Test
    @Order(2)
    public void moveFile_UPDATE() throws InterruptedException {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        String body = "type,sku#,item_description,price\n" + "UPDATE,200,Samsung TV,510\n" + "UPDATE,201,LG TV,410";
        String fileName = "fileTest.txt";
        producerTemplate.sendBodyAndHeader("file:data/input", body, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);
        assertTrue(Files.exists(Path.of("data", "output", fileName)));
    }

    @Test
    @Order(3)
    public void moveFile_DELETE() throws InterruptedException {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        String body = "type,sku#,item_description,price\n" + "DELETE,200,Samsung TV,500\n" + "DELETE,201,LG TV,400";
        String fileName = "fileTest.txt";
        producerTemplate.sendBodyAndHeader("file:data/input", body, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);
        assertTrue(Files.exists(Path.of("data", "output", fileName)));
    }

    @Test
    @Order(4)
    public void moveFile_ADD_Exception() throws InterruptedException {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        String body = "type,sku#,item_description,price\n" + "ADD,,Samsung TV,500";
        String fileName = "fileTest.txt";
        producerTemplate.sendBodyAndHeader("file:data/input", body, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);
        assertTrue(Files.exists(Path.of("data", "output", fileName)));
    }

}
