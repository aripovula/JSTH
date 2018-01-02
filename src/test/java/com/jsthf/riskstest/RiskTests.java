package com.jsthf.riskstest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.SerializationUtils;

import com.jsthf.model.Risk;

public class RiskTests {

    @Test
    public void testSerialization() {
        Risk risk = new Risk();
        
        final List<String> topic = new ArrayList<>();
        topic.add("1");  topic.add("2");
        
        risk.setName("abc");
        risk.setUser("xyz");
        risk.setTopic(topic);
        risk.setFrameworkId(12);
        risk.setId(123L);
        Risk other = (Risk) SerializationUtils
                .deserialize(SerializationUtils.serialize(risk));
        assertThat(other.getName()).isEqualTo(risk.getName());
        assertThat(other.getUser()).isEqualTo(risk.getUser());
        assertThat(other.getTopic()).isEqualTo(risk.getTopic());
        assertThat(other.getFrameworkId()).isEqualTo(risk.getFrameworkId());
        assertThat(other.getId()).isEqualTo(risk.getId());
    }

}
