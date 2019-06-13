package com.ponsak.autoandgeneral.demo;

import com.ponsak.autoandgeneral.demo.model.Todo;
import com.ponsak.autoandgeneral.demo.repository.TodoRepository;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.LongStream;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	public ServletWebServerFactory servletContainer(@Value("${http.port}") int httpPort) {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setPort(httpPort);
		connector.setAttribute("relaxedQueryChars", "|{}[]");
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(connector);
		tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	            connector.setAttribute("relaxedQueryChars", "|{}[]");
	        }
	    });
		return tomcat;
	}

	@Bean
	CommandLineRunner init(TodoRepository repository) {
		return args -> {
			repository.deleteAll();
			LongStream.range(1, 4).mapToObj(i -> new Todo(i, "Todo " + i, false, getDateStr()))
					.map(v -> repository.save(v)).forEach(System.out::println);
		};
	}
	
	private String getDateStr(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date = simpleDateFormat.format(new Date()).replaceFirst(" ", "T") + "Z";
		return date;
	}
}
