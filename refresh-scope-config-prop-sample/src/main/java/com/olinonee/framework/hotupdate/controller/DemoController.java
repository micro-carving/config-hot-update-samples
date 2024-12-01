package com.olinonee.framework.hotupdate.controller;

import lombok.AllArgsConstructor;
import com.olinonee.framework.hotupdate.bean.DemoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 实例 控制器
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-07-14
 */
@RestController
@RequestMapping("/demo")
@AllArgsConstructor
public class DemoController {

	private final DemoBean demoBean;

	@GetMapping("/echo")
	public Map<String, Object> echo() {
		Map<String, Object> map = new HashMap<>();
		map.put("title", demoBean.getTitle());
		map.put("order", demoBean.getOrder());
		return map;
	}
}
