package samblake.dynaconfig.example;

import samblake.dynaconfig.annotations.ConfigValue;
import samblake.dynaconfig.annotations.DynaConfig;

@DynaConfig
public interface MyConfig {
	
	@ConfigValue(defaultValue="123")
	String test();
	
	@ConfigValue(defaultValue="123")
	int testInt();
	
	@ConfigValue
	Integer testInteger();
	
	@ConfigValue(name = "custom.name")
	Integer testCustomName();
	
}