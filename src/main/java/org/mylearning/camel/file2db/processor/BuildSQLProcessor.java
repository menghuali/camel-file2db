package org.mylearning.camel.file2db.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.mylearning.camel.file2db.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component("buildSQLProcessor")
@Slf4j
public class BuildSQLProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Item item = (Item) exchange.getIn().getBody();
        log.info("Item is {}", item);

        if (!StringUtils.hasText(item.getSku()))
            throw new DataException("Sku is null for " + item.getItemDescription());

        String query = null;
        switch (item.getTransactionType()) {
            case "ADD":
                query = String.format("INSERT INTO ITEMS(SKU, ITEM_DESCRIPTION, PRICE) VALUES('%s', '%s', %s)",
                        item.getSku(), item.getItemDescription(), item.getPrice().toString());
                break;
            case "UPDATE":
                query = String.format("UPDATE ITEMS SET PRICE = %s WHERE SKU = '%s'", item.getPrice().toString(),
                        item.getSku());
                break;
            case "DELETE":
                query = String.format("DELETE FROM ITEMS WHERE SKU = '%s'", item.getSku());
                break;
            default:
                throw new UnsupportedOperationException(item.getTransactionType());
        }
        log.info("The query is: {}", query);
        exchange.getIn().setBody(query);
    }

}
