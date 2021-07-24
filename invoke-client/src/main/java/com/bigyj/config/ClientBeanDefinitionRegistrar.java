package com.bigyj.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bigyj.ClientConfigurationcation;
import com.bigyj.InvokeClientFactoryBean;
import com.bigyj.annotation.EnableInvokeClient;
import com.bigyj.annotation.InvokeClient;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
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
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class ClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
	private Environment environment;
	private ResourceLoader resourceLoader;
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		ClassPathScanningCandidateComponentProvider scanner = this.getScanner();
		scanner.setResourceLoader(resourceLoader);
		/**
		 * 获取扫描包路径
		 */
		Set<String> basePackages = getBasePackages(importingClassMetadata);
		AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(InvokeClient.class);
		scanner.addIncludeFilter(annotationTypeFilter);
		//循环扫描多个包，进行注入
		basePackages.stream().forEach(basePackage->{
			scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
				/**
				 * 注册客户端信息
				 */
				registryClient(beanDefinition,registry);
			});
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

	/**
	 * 注册
	 * @param invokeClient
	 * @param registry
	 */
	private void registryClient(BeanDefinition invokeClient, BeanDefinitionRegistry registry) {
		if (invokeClient instanceof AnnotatedBeanDefinition) {
			AnnotationMetadata metadata = ((AnnotatedBeanDefinition) invokeClient).getMetadata();
			String className = metadata.getClassName();
			Map<String, Object> attributes = metadata.getAnnotationAttributes(InvokeClient.class.getCanonicalName());
			/**
			 * 注册客户端配置信息
			 */
			registryClientConfig(attributes,this.getClientName(attributes),registry);
			/**
			 * 注册client
			 */
			registClientBean(attributes,className,registry);
		}
	}

	/**
	 * 获取客户端名称
	 * @return
	 */
	private String getClientName(Map<String, Object> client) {
		String value = (String) client.get("name");
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("value");
		}
		return value ;
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
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class,name));
		name = name + "." + ClientConfigurationcation.class.getSimpleName();
		registry.registerBeanDefinition(name,beanDefinition);

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
		builder.addPropertyValue("name", getClientName(attributes));

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

	/**
	 * 获取包扫描路径
	 * @param importingClassMetadata
	 * @return
	 */
	private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
		Map<String, Object> attributes = importingClassMetadata
				.getAnnotationAttributes(EnableInvokeClient.class.getCanonicalName());

		Set<String> basePackages = new HashSet<>();
		for (String pkg : (String[]) attributes.get("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		//若未维护包名，则获取当前启动类所在的目录
		if (basePackages.isEmpty()) {
			basePackages.add(
					ClassUtils.getPackageName(importingClassMetadata.getClassName()));
		}
		return basePackages;
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
