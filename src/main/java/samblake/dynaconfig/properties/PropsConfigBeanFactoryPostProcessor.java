package samblake.dynaconfig.properties;

import org.springframework.stereotype.Component;
import samblake.dynaconfig.ConfigBeanFactoryPostProcessor;
import samblake.dynaconfig.ConfigMethodInterceptor;

@Component
public class PropsConfigBeanFactoryPostProcessor extends ConfigBeanFactoryPostProcessor {
	
	@Override
	protected ConfigMethodInterceptor getConfigMethodInterceptor() {
		return new PropsMethodInterceptor();
	}
	
}