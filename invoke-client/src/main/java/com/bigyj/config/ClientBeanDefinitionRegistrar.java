package com.bigyj.config;

import java.util.Map;

import com.bigyj.ClientConfigurationcation;
import com.bigyj.InvokeClientFactoryBean;
import com.bigyj.annotation.InvokeClient;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

public class ClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
	private Environment environment;
	private ResourceLoader resourceLoader;
	private static final String BASE_PACKAGE ="com.bigyj.client";
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		ClassPathScanningCandidateComponentProvider scanner = this.getScanner();
		scanner.setResourceLoader(resourceLoader);
		AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(InvokeClient.class);
		scanner.addIncludeFilter(annotationTypeFilter);
		scanner.findCandidateComponents(BASE_PACKAGE).forEach(beanDefinition -> {
			/**
			 * 注册客户端信息
			 */
			registryClient(beanDefinition,registry);
		});

	}



	private ClassPathScanningCandidateComponentProvider getScanner() {
		return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
			@Override
			protected boolean isCandidateComponent(
					AnnotatedBeanDefinition beanDefinition) {
				boolean isCandidate = false;
				if (beanDefinition.getMetadata().isIndependent()) {
					if (!beanDefinition.getMetadata().isAnnotation()) {
						isCandidate = true;
					}
				}
				return isCandidate;
			}
		};
	}

	private void registryClient(BeanDefinition invokeClient, BeanDefinitionRegistry registry) {
		if (invokeClient instanceof AnnotatedBeanDefinition) {
			AnnotationMetadata metadata = ((AnnotatedBeanDefinition) invokeClient).getMetadata();
			String className = metadata.getClassName();
			Map<String, Object> attributes = metadata.getAnnotationAttributes(InvokeClient.class.getCanonicalName());
			//注册client
			registClientBean(attributes,className,registry);
			//注册配置信息
			registryClientConfig(attributes,className,registry);
		}
	}

	/**
	 * 注册配置类
	 * @param registry
	 */
	private void registryClientConfig(Map<String, Object> attributes,String name , BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ClientConfigurationcation.class);
		Object configuration = attributes.get("configuration");
		builder.addConstructorArgValue(name);
		builder.addConstructorArgValue(configuration);
		registry.registerBeanDefinition(name + "." + ClientConfigurationcation.class.getSimpleName(),
				builder.getBeanDefinition());
	}

	/**
	 * 注册客户端bean
	 * @param attributes
	 * @param className
	 * @param registry
	 */
	private void registClientBean(Map<String, Object> attributes,String className,BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(InvokeClientFactoryBean.class);
		builder.addPropertyValue("type", className);
		builder.addPropertyValue("path", getPath(attributes));
		builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		beanDefinition.setPrimary(true);
		String alias = "InvokeClient" + className.substring(className.lastIndexOf(".") + 1);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{alias});
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}

	private String getPath(Map<String, Object> attributes) {
		String path = resolve((String) attributes.get("path"));
		return getPath(path);
	}

	private String resolve(String value) {
		if (StringUtils.hasText(value)) {
			return this.environment.resolvePlaceholders(value);
		}
		return value;
	}

	static String getPath(String path) {
		if (StringUtils.hasText(path)) {
			path = path.trim();
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
			}
		}
		return path;
	}
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment ;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader ;
	}
}
