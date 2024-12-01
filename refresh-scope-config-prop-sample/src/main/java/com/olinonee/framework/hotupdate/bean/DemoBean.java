package com.olinonee.framework.hotupdate.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
@ConfigurationProperties(prefix = "demo")
public class DemoBean {

	private String title;

	private Integer order;
}
