package samblake.dynaconfig.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import samblake.dynaconfig.annotations.DynaConfig;

import javax.annotation.PostConstruct;

import static org.springframework.boot.SpringApplication.run;

@ComponentScan(value = "samblake.dynaconfig", includeFilters = {
		@Filter(type = FilterType.ANNOTATION, classes = DynaConfig.class)
})
@EnableAutoConfiguration
public class App {
	
	@Autowired
	private MyConfig config;
	
	public void setConfig(MyConfig config) {
		this.config = config;
	}
	
	@PostConstruct
	public void init() {
		System.out.println(config.test());
		System.out.println(config.testInt());
		System.out.println(config.testInteger());
		System.out.println(config.testCustomName());
	}
	
	public static void main(String[] args) throws Exception {
		run(App.class, args);
	}
}
