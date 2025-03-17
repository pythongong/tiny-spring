package util.com.test.factory;

import com.pythongong.enums.ScopeEnum;
import com.pythongong.stereotype.Bean;
import com.pythongong.stereotype.Configuration;
import com.pythongong.stereotype.Scope;

@Configuration
public class DataFactory {

    @Bean("foodA")
    Food getFoodA() {
        return new Food();
    }

    @Bean("foodB")
    Food getFoodB() {
        return new Food();
    }

    @Bean("foodC")
    @Scope(ScopeEnum.PROTOTYPE)
    Food getFoodC() {
        return new Food();
    }

}
