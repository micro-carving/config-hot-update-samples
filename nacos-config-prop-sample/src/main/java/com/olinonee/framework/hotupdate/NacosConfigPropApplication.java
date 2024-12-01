package com.olinonee.framework.hotupdate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动器
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-07-14
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConfigPropApplication {
	public static void main(String[] args) {
		SpringApplication.run(NacosConfigPropApplication.class, args);
	}
}
