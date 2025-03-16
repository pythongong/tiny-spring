package util.com.test.cyclic_dependency;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
@Component("CyclicB")
public class CyclicB {

    @AutoWired
    private CyclicB cyclicB;

}
