package com.pythongong.test.utils;

import com.pythongong.enums.ScopeEnum;
import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.Scope;

@Component("prototypeBean")
@Scope(ScopeEnum.PROTOTYPE)
public class PrototypeBean {

}
