package com.olinonee.framework.hotupdate.bean;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@NacosPropertySource(dataId = "common.yaml", autoRefreshed = true)
@ConfigurationProperties(prefix = "demo")
public class DemoBean {

	private String title;

	private Integer order;
}
