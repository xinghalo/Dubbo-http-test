package com.xingoo.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by xinghailong on 2017/7/21.
 */

@Configuration
@ImportResource({"classpath:config/*.xml"})
public class DubboConfig {
}
