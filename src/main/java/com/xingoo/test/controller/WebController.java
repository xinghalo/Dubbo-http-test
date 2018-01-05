package com.xingoo.test.controller;

import com.xingoo.test.utils.DubboApiUtils;
import com.xingoo.test.utils.SpringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("xingoo")
public class WebController {
    @RequestMapping(value = "classes")
    public List classes(String pagkage){
        return DubboApiUtils.getClasses(pagkage)
                .stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "methods")
    public Map<String,String> methods(String clazz){
        Map<String,String> methods = new HashMap<>();
        for(Method method : DubboApiUtils.getMethod(clazz)){

            String paramTypes =  StringUtils.join(
                    Arrays.stream(method.getParameterTypes()).map(c -> c.getName()).collect(Collectors.toList()),
                    ",");

            methods.put(method.getName(), paramTypes);
        }
        return methods;
    }

    @RequestMapping(value = "invoke")
    public Object invoke(String clazz, String method, String params,String types){
        try {
            // 反射拿到对应的class
            Class cla = Thread.currentThread().getContextClassLoader().loadClass(clazz);

            // 在appContext中拿到对应的bean
            Object bean = SpringUtils.getBean(cla.getSimpleName());

            // 格式化参数与参数类型
            Class<?>[] parameterTypes = DubboApiUtils.paramTypeFormat(types);
            Object[] parameters = DubboApiUtils.paramsFormat(params,types);

            // 通过反射调用对应的方法
            return cla.getMethod(method, parameterTypes).invoke(bean,parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }
}
