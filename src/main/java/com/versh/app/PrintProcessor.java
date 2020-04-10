package com.versh.app;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PrintProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println("***********"+exchange.getIn().getBody().toString()+"***********");
    }

}
