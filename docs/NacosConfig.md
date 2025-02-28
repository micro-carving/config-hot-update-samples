# Nacos Config

## Spring Cloud Alibaba Nacos Config

`Nacos` 提供用于存储配置和其他元数据的 `key/value` 存储，为分布式系统中的外部化配置提供服务器端和客户端支持。使用 `Spring Cloud Alibaba Nacos Config`，您可以在 `Nacos Server` 集中管理你 `Spring Cloud` 应用的外部属性配置。

`Spring Cloud Alibaba Nacos Config` 是 `Config Server` 和 `Client` 的替代方案，客户端和服务器上的概念与 `Spring Environment` 和 `PropertySource` 有着一致的抽象，在特殊的 `bootstrap` 阶段，配置被加载到 `Spring` 环境中。当应用程序从开发到测试再到生产时，您可以管理这些环境之间的配置，并确保应用程序具有迁移时需要运行的所有内容。

### 快速开始

#### Nacos 服务端初始化

1、启动 `Nacos Server`。启动方式可见 [Nacos 官网](https://nacos.io/zh-cn/docs/quick-start.html)

2、启动好 `Nacos` 之后，在 `Nacos` 添加如下的配置：

```text
Data ID:    nacos-config.properties

Group  :    DEFAULT_GROUP

配置格式:    Properties

配置内容： 
user.name=nacos-config-properties
user.age=90
```

> **注意**
> 
> `dataId` 是以 `properties`（默认的文件扩展名方式）为扩展名。

#### 客户端使用方式

如果要在您的项目中使用 `Nacos` 来实现应用的外部化配置，使用 `group ID` 为 `com.alibaba.cloud` 和 `artifact ID` 为 `spring-cloud-starter-alibaba-nacos-config` 的 `starter`。

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

现在就可以创建一个标准的 `Spring Boot` 应用。

```java
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProviderApplication.class, args);
        String userName = applicationContext.getEnvironment().getProperty("user.name");
        String userAge = applicationContext.getEnvironment().getProperty("user.age");
        System.err.println("user name :"+userName+"; age: "+userAge);
    }
}
```

在运行此 `Example` 之前， 必须使用 `bootstrap.properties` 配置文件来配置 `Nacos Server` 地址，例如：

**bootstrap.properties**：
```properties
spring.application.name=nacos-config
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
```

> **注意**
>
> 当你使用域名的方式来访问 `Nacos` 时，`spring.cloud.nacos.config.server-addr` 配置的方式为 `域名:port`。 例如 `Nacos` 的域名为 `abc.com.nacos`，监听的端口为 `80`，则 `spring.cloud.nacos.config.server-addr=abc.com.nacos:80`。 注意 `80` 端口不能省略。 启动这个 `Example`，可以看到如下输出结果：

```text
2018-11-02 14:24:51.638 INFO 32700 --- [main] c.a.demo.provider.ProviderApplication    : Started ProviderApplication in 14.645 seconds (JVM running for 15.139)
user name :nacos-config-properties; age: 90
2018-11-02 14:24:51.688 INFO 32700 --- [-127.0.0.1:8848] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@a8c5e74: startup date [Fri Nov 02 14:24:51 CST 2018]; root of context hierarchy 2018-11
```

### 基于 dataId 为 yaml 的文件扩展名配置方式

`spring-cloud-starter-alibaba-nacos-config` 对于 `yaml` 格式也是完美支持的。这个时候只需要完成以下两步：

1、在应用的 bootstrap.properties 配置文件中显示的声明 dataId 文件扩展名。如下所示

**bootstrap.properties**:
```properties
spring.cloud.nacos.config.file-extension=yaml
```

2、在 Nacos 的控制台新增一个dataId为yaml为扩展名的配置，如下所示：

```text
Data ID:        nacos-config.yaml

Group  :        DEFAULT_GROUP

配置格式:        YAML

配置内容:        
user.name: nacos-config-yaml
user.age: 68
```

这两步完成后，重启测试程序，可以看到如下输出结果。

```text
2018-11-02 14:59:00.484 INFO 32928 --- [main] c.a.demo.provider.ProviderApplication:Started ProviderApplication in
14.183 seconds (JVM running for 14.671)
user name :nacos-config-yaml; age: 68
2018-11-02 14:59:00.529 INFO 32928 --- [-127.0.0.1:8848] s.c.a.AnnotationConfigApplicationContext : Refreshing
org.springframework.context.annotation.AnnotationConfigApplicationContext@265a478e: startup
date [Fri Nov 02 14:59:00 CST 2018]; root of context hierarchy
```

### 支持配置的动态更新

`spring-cloud-starter-alibaba-nacos-config` 也支持配置的动态更新，启动 `Spring Boot` 应用测试的代码如下：

```java
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProviderApplication.class, args);
        while(true) {
            //当动态配置刷新时，会更新到 Environment 中，因此这里每隔一秒中从 Environment 中获取配置
            String userName = applicationContext.getEnvironment().getProperty("user.name");
            String userAge = applicationContext.getEnvironment().getProperty("user.age");
            System.err.println("user name :" + userName + "; age: " + userAge);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
```

如下所示，当变更 `user.name` 时，应用程序中能够获取到最新的值：

```text
user name :nacos-config-yaml; age: 68
user name :nacos-config-yaml; age: 68
user name :nacos-config-yaml; age: 68
2018-11-02 15:04:25.069 INFO 32957 --- [-127.0.0.1:8848] o.s.boot.SpringApplication               : Started application
in 0.144 seconds (JVM running for 71.752)
2018-11-02 15:04:25.070 INFO 32957 --- [-127.0.0.1:8848] s.c.a.AnnotationConfigApplicationContext : Closing
org.springframework.context.annotation.AnnotationConfigApplicationContext@10c89124: startup
date [Fri Nov 02 15:04:25 CST 2018]; parent:
org.springframework.context.annotation.AnnotationConfigApplicationContext@6520af7
2018-11-02 15:04:25.071 INFO 32957 --- [-127.0.0.1:8848] s.c.a.AnnotationConfigApplicationContext : Closing
org.springframework.context.annotation.AnnotationConfigApplicationContext@6520af7: startup
date [Fri Nov 02 15:04:24 CST 2018]; root of context hierarchy
//从 Environment 中 读取到更改后的值
user name :nacos-config-yaml-update; age: 68
user name :nacos-config-yaml-update; age: 68
```

> **注意**
> 
> 你可以通过配置 `spring.cloud.nacos.config.refresh.enabled=false` 来关闭动态刷新

### 可支持 `profile` 粒度的配置

`spring-cloud-starter-alibaba-nacos-config` 在加载配置的时候，不仅仅加载了以 `dataId` 为 `${spring.application.name}.${file-extension:properties}` 为前缀的基础配置，还加载了 `dataId` 为 `${spring.application.name}-${profile}.${file-extension:properties}` 的基础配置。在日常开发中如果遇到多套环境下的不同配置，可以通过 `Spring` 提供的 `${spring.profiles.active}` 这个配置项来配置。

```properties
spring.profiles.active=develop
```

> **注意**
>
> `${spring.profiles.active}` 当通过配置文件来指定时必须放在 `bootstrap.properties` 文件中。

`Nacos` 上新增一个 `dataId` 为：`nacos-config-develop.yaml` 的基础配置，如下所示：

```text
Data ID:        nacos-config-develop.yaml

Group  :        DEFAULT_GROUP

配置格式:        YAML

配置内容:        current.env: develop-env
```

启动 `Spring Boot` 应用测试的代码如下：

```java
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ProviderApplication.class, args);
        while(true) {
            String userName = applicationContext.getEnvironment().getProperty("user.name");
            String userAge = applicationContext.getEnvironment().getProperty("user.age");
            //获取当前部署的环境
            String currentEnv = applicationContext.getEnvironment().getProperty("current.env");
            System.err.println("in "+currentEnv+" environment; " + "user name :" + userName + "; age: " + userAge);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
```

启动后，可见控制台的输出结果：

```text
in develop-env environment; user name :nacos-config-yaml-update; age: 68
2018-11-02 15:34:25.013 INFO 33014 --- [ Thread-11] ConfigServletWebServerApplicationContext : Closing
org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@6f1c29b7: startup
date [Fri Nov 02 15:33:57 CST 2018]; parent:
org.springframework.context.annotation.AnnotationConfigApplicationContext@63355449
```

如果需要切换到生产环境，只需要更改 `${spring.profiles.active}` 参数配置即可。如下所示：

```properties
spring.profiles.active=product
```

同时生产环境上 `Nacos` 需要添加对应 `dataId` 的基础配置。例如，在生成环境下的 `Nacos` 添加了 `dataId` 为：`nacos-config-product.yaml` 的配置：

```text
Data ID:        nacos-config-product.yaml

Group  :        DEFAULT_GROUP

配置格式:        YAML

配置内容:        current.env: product-env
```

启动测试程序，输出结果如下：

```text
in product-env environment; user name :nacos-config-yaml-update; age: 68
2018-11-02 15:42:14.628 INFO 33024 --- [Thread-11] ConfigServletWebServerApplicationContext : Closing
org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@6aa8e115: startup
date [Fri Nov 02 15:42:03 CST 2018]; parent:
org.springframework.context.annotation.AnnotationConfigApplicationContext@19bb07ed
```
> **注意**
>
> 此案例中我们通过 `spring.profiles.active=<profilename>` 的方式写死在配置文件中，而在真正的项目实施过程中这个变量的值是需要不同环境而有不同的值。这个时候通常的做法是通过 `-Dspring.profiles.active=<profile>` 参数指定其配置来达到环境间灵活的切换。

### 支持自定义 namespace 的配置

首先看一下 `Nacos` 的 `Namespace` 的概念， [Nacos 概念](https://nacos.io/zh-cn/docs/concepts.html)

> 用于进行租户粒度的配置隔离。不同的命名空间下，可以存在相同的 `Group` 或 `Data ID` 的配置。`Namespace` 的常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等。

在没有明确指定 `${spring.cloud.nacos.config.namespace}` 配置的情况下， 默认使用的是 `Nacos` 上 `Public` 这个 `namespace`。如果需要使用自定义的命名空间，可以通过以下配置来实现：

```text
spring.cloud.nacos.config.namespace=b3404bc0-d7dc-4855-b519-570ed34b62d7
```

> **注意**
>
> 该配置必须放在 `bootstrap.properties` 文件中。此外 `spring.cloud.nacos.config.namespace` 的值是 `namespace` 对应的 `id`，`id` 值可以在 `Nacos` 的控制台获取。并且在添加配置时注意不要选择其他的 `namespace`，否则将会导致读取不到正确的配置。

### 支持自定义 Group 的配置

在没有明确指定 `${spring.cloud.nacos.config.group}` 配置的情况下， 默认使用的是 `DEFAULT_GROUP` 。如果需要自定义自己的 `Group`，可以通过以下配置来实现：

```properties
spring.cloud.nacos.config.group=DEVELOP_GROUP
```

> **注意**
>
> 该配置必须放在 `bootstrap.properties` 文件中。并且在添加配置时 `Group` 的值一定要和 `spring.cloud.nacos.config.group` 的配置值一致。

### 支持自定义扩展的 Data Id 配置

`Spring Cloud Alibaba Nacos Config` 从 `0.2.1` 版本后，可支持自定义 `Data Id` 的配置。关于这部分详细的设计可参考 [这里](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/issues/141)。一个完整的配置案例如下所示：

```properties
spring.application.name=opensource-service-provider
spring.cloud.nacos.config.server-addr=127.0.0.1:8848

# config external configuration
# 1、Data Id 在默认的组 DEFAULT_GROUP,不支持配置的动态刷新
spring.cloud.nacos.config.extension-configs[0].data-id=ext-config-common01.properties

# 2、Data Id 不在默认的组，不支持动态刷新
spring.cloud.nacos.config.extension-configs[1].data-id=ext-config-common02.properties
spring.cloud.nacos.config.extension-configs[1].group=GLOBALE_GROUP

# 3、Data Id 既不在默认的组，也支持动态刷新
spring.cloud.nacos.config.extension-configs[2].data-id=ext-config-common03.properties
spring.cloud.nacos.config.extension-configs[2].group=REFRESH_GROUP
spring.cloud.nacos.config.extension-configs[2].refresh=true
```
可以看到:

- 通过 `spring.cloud.nacos.config.extension-configs[n].data-id` 的配置方式来支持多个 `Data Id` 的配置。
- 通过 `spring.cloud.nacos.config.extension-configs[n].group` 的配置方式自定义 `Data Id` 所在的组，不明确配置的话，默认是 `DEFAULT_GROUP`。
- 通过 `spring.cloud.nacos.config.extension-configs[n].refresh` 的配置方式来控制该 `Data Id` 在配置变更时，是否支持应用中可动态刷新，感知到最新的配置值。默认是不支持的。

> **注意**
>
> 多个 `Data Id` 同时配置时，他的优先级关系是 `spring.cloud.nacos.config.extension-configs[n].data-id` 其中 `n` 的值越大，优先级越高。

> **注意**
>
> `spring.cloud.nacos.config.extension-configs[n].data-id` 的值必须带文件扩展名，文件扩展名既可支持 `properties`，又可以支持 `yaml/yml`。此时 `spring.cloud.nacos.config.file-extension` 的配置对自定义扩展配置的 `Data Id` 文件扩展名没有影响。

通过自定义扩展的 `Data Id` 配置，既可以解决多个应用间配置共享的问题，又可以支持一个应用有多个配置文件。

为了更加清晰的在多个应用间配置共享的 `Data Id` ，你可以通过以下的方式来配置：

```properties
# 配置支持共享的 Data Id
spring.cloud.nacos.config.shared-configs[0].data-id=common.yaml

# 配置 Data Id 所在分组，缺省默认 DEFAULT_GROUP
spring.cloud.nacos.config.shared-configs[0].group=GROUP_APP1

# 配置Data Id 在配置变更时，是否动态刷新，缺省默认 false
spring.cloud.nacos.config.shared-configs[0].refresh=true
```

可以看到：

- 通过 `spring.cloud.nacos.config.shared-configs[n].data-id` 来支持多个共享 `Data Id` 的配置。
- 通过 `spring.cloud.nacos.config.shared-configs[n].group` 来配置自定义 `Data Id` 所在的组，不明确配置的话，默认是 `DEFAULT_GROUP`。
- 通过 `spring.cloud.nacos.config.shared-configs[n].refresh` 来控制该 `Data Id` 在配置变更时，是否支持应用中动态刷新，默认 `false`。

### 配置的优先级

`Spring Cloud Alibaba Nacos Config` 目前提供了三种配置能力从 `Nacos` 拉取相关的配置。

- A: 通过 `spring.cloud.nacos.config.shared-configs[n].data-id` 支持多个共享 `Data Id` 的配置
- B: 通过 `spring.cloud.nacos.config.extension-configs[n].data-id` 的方式支持多个扩展 `Data Id` 的配置
- C: 通过内部相关规则(应用名、应用名 + `Profile` )自动生成相关的 `Data Id` 配置

当三种方式共同使用时，他们的一个优先级关系是：`A < B < C`

### 完全关闭配置

通过设置 `spring.cloud.nacos.config.enabled = false` 来完全关闭 `Spring Cloud Nacos Config`

### 参考

- [Spring Cloud Alibaba Nacos Config](https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-config)