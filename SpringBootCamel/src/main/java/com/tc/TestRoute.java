package com.tc;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component

public class TestRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception{
		
		// Uncomment the following to test a simple file copy from 'input' to 'output' folder
		
		from("file:input?noop=true")
		.log("Read from input folder").
		to("file:output").log("Written to OP folder");
		
		
		// Uncomment the following to copy a single file- named file - from SFTP server's home directory
		
    	/*from("sftp://bala@localhost:2223?password=123&fileName=pom.xml").to("log:?level=INFO&showAll=true").
        to("file://output-dir");*/
		
	/*	from("sftp://bala@localhost:2222?password=123&streamDownload=true").to("log:?level=INFO&showAll=true").
        to("file://out");*/
		
		
		//from("stream:url?url=sftp://bala@localhost:2222?password=123&scanStream=true").
		/*from("stream:url?url=sftp://bala:123@localhost:2222").
		to("log:?level=INFO&showAll=true").
		to("file://out");*/
		
	}
	

}
