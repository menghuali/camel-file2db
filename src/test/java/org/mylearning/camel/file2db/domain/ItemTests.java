package org.mylearning.camel.file2db.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class ItemTests {

    @Test
    public void gettSetter() {
        Item item = new Item();
        item.setTransactionType("ADD");
        item.setSku("100");
        item.setItemDescription("Samsung TV");
        item.setPrice(BigDecimal.valueOf(500));

        assertEquals("ADD", item.getTransactionType());
        assertEquals("100", item.getSku());
        assertEquals("Samsung TV", item.getItemDescription());
        assertEquals(BigDecimal.valueOf(500), item.getPrice());
    }

    @Test
    public void testToString() {
        Item item = new Item();
        item.setTransactionType("ADD");
        item.setSku("100");
        item.setItemDescription("Samsung TV");
        item.setPrice(BigDecimal.valueOf(500));
        assertEquals("Item(transactionType=ADD, sku=100, itemDescription=Samsung TV, price=500)", item.toString());
    }

}
