package samblake.dynaconfig;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import samblake.dynaconfig.annotations.ConfigValue;

import java.lang.reflect.Method;

import static org.springframework.util.StringUtils.isEmpty;

public abstract class ConfigMethodInterceptor implements MethodInterceptor {
	
	private static final TypeDescriptor STRING_TYPE = TypeDescriptor.valueOf(String.class);
	
	private ConversionService conversionService = new DefaultConversionService();
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		ConfigValue annotation = invocation.getMethod().getAnnotation(ConfigValue.class);
		if (annotation == null) {
			return invocation.proceed();
		}
		
		String key = isEmpty(annotation.name())
				? methodToKey(invocation.getMethod())
				: annotation.name();
		
		String defaultValue = annotation.defaultValue();
		
		Object value = findConfig(key, defaultValue);
		
		Method method = invocation.getMethod();
		MethodParameter parameter = new MethodParameter(method, -1);
		TypeDescriptor returnType = TypeDescriptor.nested(parameter, 0);
		
		if (value == null || returnType == null) {
			throw new RuntimeException("Poops");
		}
		
		if (returnType.equals(STRING_TYPE)) {
			return value;
		}
		
		if (conversionService.canConvert(STRING_TYPE, returnType)) {
			return conversionService.convert(value, STRING_TYPE, returnType);
		}
		
		return customConvert(returnType);
	}
	
	protected String methodToKey(Method method) {
		return StringConverter.toDotted(method.getName());
	}
	
	protected abstract Object findConfig(String method, String defaultValue);
	
	protected Object customConvert(TypeDescriptor returnType) {
		throw new ConverterNotFoundException(STRING_TYPE, returnType);
	}
}