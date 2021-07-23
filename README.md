# simple-remote-call说明文档
## 一. 为什么会想到编写此项目
### 1. 背景
在工作中，经常会遇到与其他业务系统进行通讯交互，无非是通过调用对方的http接口进行数据的交互,少则一个，多则几十个。
随着Oauth2.0协议在各个业务系统的应用，调用其他业务系统的方式也是千篇一律：首先通过对方颁发的客户端信息进行客户端认证，
认证通过之后获取accessToken，随后所有业务接口携带accessToken调用接口。一些安全性较高的系统，还会增加一些加解密、验签、完整性
、证书的验证。

当前大部分思路是通过开发各个接口调用逻辑功能，然后通过统一封装http调用工具进行接口调用，整个代码会显得特别臃肿，同时最近在读feign源码，
因此仿照feign客户端进行代码改造，旨在优化业务代码，同时也加深对feign的理解。
### 2. 改造后的代码示例
只需要按照接口出入参拼接实体类，再接口中维护接口信息即可。一些公共的接口调用逻辑，在动态代理的InvocationHandler中统一进行处理（序列化、反序列化、远程接口调用、异常处理等等）。
同时该部分代码单独打包成jar包，易于维护及管理。
```java
@InvokeClient(value = "/v1/api")
public interface RequesClient {
	@InvokeRequest(value = "/hello",withAccessToken = false,method = HttpMethod.GET)
	public ResponseDto hello( HelloDto helloDto);
	@InvokeRequest(value = "/delete",method = HttpMethod.DELETE)
	public ResponseDto delete(DeleteDto deleteDto);
	@InvokeRequest(value = "/add",withAccessToken = false,method = HttpMethod.POST)
	public ResponseDto add(AddDto addDto);
}
```
### 3. 能够实现什么功能
- 基础远程调用
- 远程调用自定义扩展
- accessToken获取自定义扩展
- 重试机制
- 熔断机制
- SPI扩展
- 自定义配置类及客户段配置类之间的隔离
- 自定义请求拦截器
## 三. 功能原理
整个功能要实现最主要需要完成两点：远程调用client自动注入及动态代理生成生成客户端实现类进行远程调用处理。当这两个点完成后，其他的就剩下一下
代理逻辑的处理了。
### 1. 远程调用client自动注入
通过ImportBeanDefinitionRegistrar进行依赖注入
```java
public class ClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
	private Environment environment;
	private ResourceLoader resourceLoader;

	/**
     * 自动注入相关类
	 * @param importingClassMetadata
	 * @param registry
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
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
```
### 2. 动态代理生成客户端实现类
通过自定义FactoryBean，在getObject方法中使用代理模式，动态生成相关类
```java
public class InvokeClientFactoryBean implements FactoryBean<Object>, ApplicationContextAware {
	private ApplicationContext applicationContext ;
	@Setter
	private Class<?> type;
	@Setter
	private String path;
	/**
	 * 生成代理对象
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getObject() {
		return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, invocationHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return this.type;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
```


