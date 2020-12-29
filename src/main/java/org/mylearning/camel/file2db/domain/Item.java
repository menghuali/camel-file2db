package org.mylearning.camel.file2db.domain;

import java.math.BigDecimal;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@CsvRecord(separator = ",", skipFirstLine = true)
@ToString
@Getter
@Setter
public class Item {
    
    @DataField(pos = 1)
    private String transactionType;

    @DataField(pos = 2)
    private String sku;

    @DataField(pos = 3)
    private String itemDescription;

    @DataField(pos = 4, precision = 2)
    private BigDecimal price;

}
