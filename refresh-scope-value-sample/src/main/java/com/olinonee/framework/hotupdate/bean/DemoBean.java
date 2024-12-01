package com.olinonee.framework.hotupdate.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
public class DemoBean {

	@Value("${demo.title:}")
	private String title;

	@Value("${demo.order:}")
	private Integer order;
}
