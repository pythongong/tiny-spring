package beans;

import java.util.HashMap;
import java.util.Map;

import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserDao {

    private Map<Integer, String> nameMap;
    
    private Integer id;

    @PostConstruct
    public void init() {
        this.nameMap = new HashMap<>();
        nameMap.put(1, "Tom");
    }

    @PreDestroy
    public void destroy() {
        this.nameMap.clear();
    }
}
