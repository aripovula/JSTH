package com.jsthf.cardstest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.SerializationUtils;

import com.jsthf.model.FCExItem;

public class FCExItemTests {

    @Test
    public void testSerialization() {
    		FCExItem fcExItem = new FCExItem();
        
        final List<String> alist = new ArrayList<>();
        alist.add("1");  alist.add("2");
        
        fcExItem.setFirst("abc1");
        fcExItem.setSecond("abc2");
        fcExItem.setUser("xyz");
        fcExItem.setTopic(alist);
        fcExItem.setType("1");
        fcExItem.setTag("atag");
        fcExItem.setFrameworkSeln(alist);
        fcExItem.setId(1234L);
        FCExItem other = (FCExItem) SerializationUtils
                .deserialize(SerializationUtils.serialize(fcExItem));
        assertThat(other.getFirst()).isEqualTo(fcExItem.getFirst());
        assertThat(other.getSecond()).isEqualTo(fcExItem.getSecond());
        assertThat(other.getUser()).isEqualTo(fcExItem.getUser());
        assertThat(other.getTopic()).isEqualTo(fcExItem.getTopic());
        assertThat(other.getType()).isEqualTo(fcExItem.getType());
        assertThat(other.getTag()).isEqualTo(fcExItem.getTag());
        assertThat(other.getFrameworkSeln()).isEqualTo(fcExItem.getFrameworkSeln());
        assertThat(other.getId()).isEqualTo(fcExItem.getId());
    }
}
