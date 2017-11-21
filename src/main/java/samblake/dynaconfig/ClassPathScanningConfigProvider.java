package samblake.dynaconfig;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import samblake.dynaconfig.annotations.DynaConfig;

public class ClassPathScanningConfigProvider extends ClassPathScanningCandidateComponentProvider {
	
	public ClassPathScanningConfigProvider() {
		super(false);
		addIncludeFilter(new AnnotationTypeFilter(DynaConfig.class));
	}
	
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() || beanDefinition.getMetadata().isAbstract();
	}
}
