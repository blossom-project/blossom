package fr.mgargadennec.blossom.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import fr.mgargadennec.blossom.autoconfigure.EnableBlossom;

@EnableBlossom
public class App extends WebMvcConfigurerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(App.class, args);
	}
}
