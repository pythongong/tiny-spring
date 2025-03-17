package util.com.test.init_destroy;

import java.util.HashMap;
import java.util.Map;

import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;

import lombok.Getter;

@Getter
@Component
public class Infor {

    private Map<Integer, String> nameMap;

    @PostConstruct
    public void init() {
        this.nameMap = new HashMap<>();
        nameMap.put(1, "Tom");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy exectued");
        this.nameMap.clear();
    }
    
}
