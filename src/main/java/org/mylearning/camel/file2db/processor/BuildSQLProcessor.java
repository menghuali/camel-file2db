package org.mylearning.camel.file2db.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mylearning.camel.file2db.domain.Item;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component("buildSQLProcessor")
@Slf4j
public class BuildSQLProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Item item = (Item) exchange.getIn().getBody();
        log.info("Item is {}", item);
        String query = null;
        switch (item.getTransactionType()) {
            case "ADD":
                query = String.format("INSERT INTO ITEMS(SKU, ITEM_DESCRIPTION, PRICE) VALUES('%s', '%s', %s)",
                        item.getSku(), item.getItemDescription(), item.getPrice().toString());
                break;
            case "UPDATE":
            case "DELETE":
            default:
                break;
        }
        log.info("The query is: ", query);
        exchange.getIn().setBody(query);
    }

}
