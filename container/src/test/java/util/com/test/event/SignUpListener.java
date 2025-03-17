package util.com.test.event;

import com.pythongong.context.event.ApplicationListener;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
@Component("SignUpListener")
public class SignUpListener implements ApplicationListener<SingUpEvent> {

    private String messgae;

    @Override
    public void onApplicationEvent(SingUpEvent event) {
        this.messgae = event.getMeassge();
    }
    
}
