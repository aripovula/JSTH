package com.jsthf.usertest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.SerializationUtils;

import com.jsthf.model.User;

public class UserTests {

    @Test
    public void testSerialization() {
        User user = new User();
        
        final List<String> alist = new ArrayList<>();
        alist.add("1");  alist.add("2");
        
        user.setUsername("abc");
        user.setFramework(1);
        user.setTopicsSeln(alist);
        user.setRisksSeln(alist);
        user.setFrameworkSeln(alist);
        user.setCurrentCardId(11);
        user.setCardTypeSeln("abd");
        user.setLoadTypeSeln("abf");
        user.setSkin(1);
        user.setFontSize(2);
        user.setId(123L);
        User other = (User) SerializationUtils
                .deserialize(SerializationUtils.serialize(user));
        assertThat(other.getUsername()).isEqualTo(user.getUsername());
        assertThat(other.getFramework()).isEqualTo(user.getFramework());
        assertThat(other.getTopicsSeln()).isEqualTo(user.getTopicsSeln());
        assertThat(other.getRisksSeln()).isEqualTo(user.getRisksSeln());
        assertThat(other.getFrameworkSeln()).isEqualTo(user.getFrameworkSeln());
        assertThat(other.getCurrentCardId()).isEqualTo(user.getCurrentCardId());
        assertThat(other.getCardTypeSeln()).isEqualTo(user.getCardTypeSeln());
        assertThat(other.getLoadTypeSeln()).isEqualTo(user.getLoadTypeSeln());
        assertThat(other.getSkin()).isEqualTo(user.getSkin());
        assertThat(other.getFontSize()).isEqualTo(user.getFontSize());
        assertThat(other.getId()).isEqualTo(user.getId());
    }

}
