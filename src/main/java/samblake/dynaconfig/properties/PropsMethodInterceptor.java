package samblake.dynaconfig.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samblake.dynaconfig.ConfigMethodInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.springframework.util.ClassUtils.getDefaultClassLoader;

public class PropsMethodInterceptor extends ConfigMethodInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(PropsMethodInterceptor.class);
	
	private static final String PATH = "test.properties";
	
	private final Properties props;
	
	public PropsMethodInterceptor() {
		logger.debug("Loading " + PATH);
		InputStream resource = getDefaultClassLoader().getResourceAsStream(PATH);
		this.props = loadProps(resource);
	}
	
	private Properties loadProps(InputStream resource) {
		Properties props = new Properties();
		try {
			props.load(resource);
		}
		catch (IOException e) {
			logger.error("Could not read " + PATH, e);
		}
		return props;
	}
	
	@Override
	protected Object findConfig(String key, String defaultValue) {
		Object value = props.get(key);
		return value == null ? defaultValue : value;
	}
}