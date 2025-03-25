package com.pythongong.test.ioc.normal;

import com.pythongong.stereotype.Bean;
import com.pythongong.stereotype.Configuration;

@Configuration("testConfigurableFactory")
public class TestConfigurableFactory {

    @Bean
    public TestConfugrableBean createTestConfugrableBean() {
        return new TestConfugrableBean();
    }

    @Bean("testConfugrableParamBean")
    public TestConfugrableParamBean createTestConfugrableParamBean(TestBean testBean) {
        return new TestConfugrableParamBean(testBean);
    }

}
