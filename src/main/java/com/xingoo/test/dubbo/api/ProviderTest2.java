package com.xingoo.test.dubbo.api;

import com.xingoo.test.dubbo.ITest2;
import org.springframework.stereotype.Service;

@Service("ProviderTest2")
public class ProviderTest2 implements ITest2 {

    @Override
    public String test3(){
        return "test3";
    }

    @Override
    public String test4(){
        return "test4";
    }

    @Override
    public String test5(){
        return "test5";
    }
}
