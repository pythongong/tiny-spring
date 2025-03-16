package util.com.test.event;

import com.pythongong.context.event.ApplicationContextEvent;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
public class SingUpEvent extends ApplicationContextEvent {
    
    private String meassge;

    public SingUpEvent(Object source, String meassge) {
        super(source);
        this.meassge = meassge;
    }


}
