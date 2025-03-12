package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pythongong.core.filter.AnnotationTypeFilter;
import com.pythongong.core.filter.TypeFilter;
import com.pythongong.stereotype.Component;

import util.com.test.BeanA;
import util.com.test.PathA;

public class FilterTest {
    
    @Test
    public void test_Match() {
        TypeFilter filter = new AnnotationTypeFilter(Component.class);
        assertTrue(filter.match(BeanA.class));
        assertFalse(filter.match(PathA.class));
    }
}
