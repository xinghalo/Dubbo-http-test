package com.xingoo.test.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 应用主配置，为所有的请求增加跨域配置
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.xingoo.test")
public class ApplicationConfiguration {

}
