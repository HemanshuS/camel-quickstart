package com.versh.app;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CopyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Handling exception
		onException(Exception.class).process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				System.out.println("handling ex");
			}
		}).log("Received body ").handled(true);
		//===============================================================
		/*
		 * uncomment to copy file from input to output
		 */
		//from("file:C:\\data\\input?noop=true").to("file:C:\\data\\output");
		//================================================================
		/*
		 * uncomment to splitter pattern
		 */
		//from("file:C:\\data\\input?noop=true").split().tokenize("\n").process(new PrintProcessor());
		//=================================================================
		/*
		 * uncomment to content based routing
		 */
		/*
		 * from("file:C:\\data\\input?noop=true").split().tokenize("\n").to("direct:cbr"
		 * );
		 * 
		 * from("direct:cbr"). choice(). when(body().contains("flood")) .process(new
		 * PrintProcessor());
		 */
		//=================================================================

		/*
		 * Routing slip example
		 */

		from("file:C:\\data\\input?noop=true").split().tokenize("\n").process(new Processor() {
			public void process(Exchange exchange) {
				String body = exchange.getIn().getBody().toString();
				String response;
				if (body.contains("flood")) {
					// the following routes will be called sequentially
					response = "direct:route1,direct:route2,direct:route3";
				} else
					// the following routes will be called sequentially
					response = "direct:route3,direct:route2,direct:route1";
				//set the route slip message in the header
				exchange.getIn().setHeader("routingSlip", response);
			}
		}).routingSlip(header("routingSlip"));

		from("direct:route1").process(new Processor() {
			public void process(Exchange exchange) {
				String body = exchange.getIn().getBody().toString();
				body = body + " in route 1";
				System.out.println(body);
				exchange.getOut().setBody(body);
			}
		});

		from("direct:route2").process(new Processor() {
			public void process(Exchange exchange) {
				String body = exchange.getIn().getBody().toString();
				body = body + " in route 2";
				System.out.println(body);
				exchange.getOut().setBody(body);
			}
		});

		from("direct:route3").process(new Processor() {
			public void process(Exchange exchange) {
				String body = exchange.getIn().getBody().toString();
				body = body + " in route 3";
				exchange.getOut().setBody(body);
				System.out.println(body);
			}
		});
	}
}
