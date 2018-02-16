package com.tc;

import org.apache.camel.CamelContext;
import org.apache.camel.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.nativefs.NativeFileSystemFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

@SpringBootApplication
public class SpringBootCamelApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootCamelApplication.class, args);
		
		SshServer sftpServer = SshServer.setUpDefaultServer();
		sftpServer.setPort(2223);
		
		//Below line is needed
	    sftpServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("hostkey-new-01.ser")));
		
				
 /*      sftpServer.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return "bala".equals(username) && "123".equals(password);
            	//return true;
            }
        });*/
	    

	    //sftpServer.setShellFactory(new ProcessShellFactory(new String[] { "/bin/bash", "-i", "-l" }));
	    
	    sftpServer.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i"}));

        //sftpServer.setFileSystemFactory(new VirtualFileSystemFactory(FileSystems.getDefault().getPath("/tmp/sshd-home")));
        
        sftpServer.setPublickeyAuthenticator(new AuthorizedKeysAuthenticator(new File("id_rsa.pub")));
        

        sftpServer.setCommandFactory(new ScpCommandFactory());
        List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
        namedFactoryList.add(new SftpSubsystemFactory());
        sftpServer.setSubsystemFactories(namedFactoryList);


        try {
            sftpServer.start();
            //Keep this on loop. Use IDE 'STOP' option to shutdown the server.
            //while (true) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
		


	
	
	CamelContext ctx = new DefaultCamelContext();
	try {
		ctx.addRoutes(new TestRoute());
	} catch (Exception e1) {
		
		e1.printStackTrace();
		System.out.println("Error message is :: "+e1.getMessage());
	}
	
	ctx.start();
    Thread.sleep(5000);
	ctx.stop();
	
// If the below is used,the route runs infinetly. so we use a Camelcontext to start the route and stop it after the route has completed the job

	
/*	Main main = new Main();
	main.addRouteBuilder(new TestRoute());
	
	try {
		main.run();
	} catch (Exception e) {
		System.out.println("Error Message is :: "+ e.getMessage());
		e.printStackTrace();
	}*/
	
}
	
}
