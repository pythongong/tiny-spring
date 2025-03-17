package util.com.test.cyclic_dependency;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
@Component("CyclicA")
public class CyclicA {

    @AutoWired
    private CyclicB cyclicB;
}
