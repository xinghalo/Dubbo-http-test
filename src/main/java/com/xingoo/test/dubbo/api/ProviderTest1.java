package com.xingoo.test.dubbo.api;

import com.xingoo.test.dubbo.ITest1;
import com.xingoo.test.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ProviderTest1")
public class ProviderTest1 implements ITest1 {

    @Autowired
    private MyService myService;

    @Override
    public String test1(){
        return myService.test1();
    }

    @Override
    public String test2(){
        return myService.test2();
    }
}
