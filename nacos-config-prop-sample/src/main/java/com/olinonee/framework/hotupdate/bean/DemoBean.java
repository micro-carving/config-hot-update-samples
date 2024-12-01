package com.olinonee.framework.hotupdate.bean;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 实例 bean对象
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-07-14
 */
@Component
@Data
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "127.0.0.1:8848", namespace = "d8a5e82b-1d9e-4d61-b254-e44945bbd796"))
@NacosConfigurationProperties(dataId = "common.yaml", prefix = "demo", autoRefreshed = true)
public class DemoBean {

	private String title;

	private Integer order;
}
