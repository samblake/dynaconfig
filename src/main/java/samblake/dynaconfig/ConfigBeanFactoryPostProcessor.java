package samblake.dynaconfig;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

@Component
public abstract class ConfigBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	
	private final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ClassPathScanningConfigProvider scanner = new ClassPathScanningConfigProvider();
		Set<BeanDefinition> components = scanner.findCandidateComponents("samblake/dynaconfig");
		for (BeanDefinition component : components) {
			try {
				Class<?> configInterface = Class.forName(component.getBeanClassName());
				Object configProxy = createConfigProxy(configInterface);
				beanFactory.registerSingleton(component.getBeanClassName(), configProxy);
			}
			catch (ClassNotFoundException e) {
				throw new BeanCreationException(e.getMessage());
			}
		}
	}
	
	private <T> T createConfigProxy(Class<T> configInterface) {
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(configInterface);
		factory.setInterfaces(getInterfaces(configInterface));
		factory.addAdvice(ExposeInvocationInterceptor.INSTANCE);
		factory.addAdvice(getConfigMethodInterceptor());
		return (T) factory.getProxy(classLoader);
	}
	
	protected final Class<?>[] getInterfaces(Class<?> configInterface) {
		List<? extends Class<?>> additionalInterfaces = getAdditionalInterfaces();
		ArrayList<Class<?>> interfaces = new ArrayList<Class<?>>(additionalInterfaces);
		interfaces.add(configInterface);
		interfaces.addAll(additionalInterfaces);
		return interfaces.toArray(new Class<?>[interfaces.size()]);
	}
	
	protected List<? extends Class<?>> getAdditionalInterfaces() {
		return emptyList();
	}
	
	protected abstract ConfigMethodInterceptor getConfigMethodInterceptor();
}
