package com.jsthf;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jsthf.controller.FCExItemController;
import com.jsthf.model.FCExItem;
import com.jsthf.model.Risk;
import com.jsthf.model.User;
import com.jsthf.service.FCExItemService;
import com.jsthf.service.RiskService;
import com.jsthf.service.UserService;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceIntegrationTests {

    @Autowired
    private FCExItemService fcexitemService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private UserService userService;

    List<String> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    
	private static Logger myLogger = Logger
			.getLogger(FCExItemController.class.getName());

    
    @Test
    public void shouldCreateNewCardAndDeleteThatCard() {
        
    		// Test #1  - adding new card
    		FCExItem card = new FCExItem();
        card.setFirst("abc");
        card.setSecond("xyz");
        card.setType("1");
        card.setTopic(list);
        card.setFrameworkSeln(list);
        card.setUser("user");

        long countBeforeInsert = fcexitemService.findAll().size();
        fcexitemService.save(card);
        long countAfterInsert = fcexitemService.findAll().size();
        Assert.assertEquals(countBeforeInsert+1, countAfterInsert);
      
        // test #2  -  values were added correctly
        FCExItem card2 = fcexitemService.findById(card.getId());
        Assert.assertNotNull(card2);
        Assert.assertEquals(card2.getFirst(),"abc");
        Assert.assertEquals(card2.getSecond(),"xyz");
        Assert.assertEquals(card2.getType(),"1");
        Assert.assertEquals(card2.getUser(),"user");
        
        // test #3  - update value of question text
        card.setFirst("updated");
        card.setSecond("xyz2");
        card.setType("0");
        card.setTopic(list2);
        card.setFrameworkSeln(list2);
        card.setUser("newuser");
        fcexitemService.save(card);
        
        FCExItem card3 = fcexitemService.findById(card.getId());
        Assert.assertNotNull(card3);
        Assert.assertEquals(card3.getFirst(),"updated");
        Assert.assertEquals(card3.getSecond(),"xyz2");
        Assert.assertEquals(card3.getType(),"0");
        Assert.assertEquals(card3.getUser(),"newuser");
        Assert.assertEquals(card3.getFrameworkSeln(),list2);
        Assert.assertEquals(card3.getTopic(),list2);
        
        // test #4  - that card was deleted correctly
        fcexitemService.delete(card);
        long countAfterDelete = fcexitemService.findAll().size();
        Assert.assertEquals(countAfterInsert-1, countAfterDelete);
    }
    
    
    @Test
    public void shouldCreateNewRiskAndDeleteThatRisk() {
		
    		// Test #5  - adding new card
    		Risk risk = new Risk();
        risk.setName("abc");
        risk.setFrameworkId(1);
        risk.setTopic(list);
        risk.setUser("user");

        long countBeforeInsert = riskService.findAll().size();
        riskService.save(risk);
        long countAfterInsert = riskService.findAll().size();
        Assert.assertEquals(countBeforeInsert+1, countAfterInsert);
               
        // test #6  -  values were added correctly
        Risk risk2 = riskService.findById(risk.getId());
        Assert.assertNotNull(risk2);
        Assert.assertEquals(risk2.getName(),"abc");
        Assert.assertEquals(risk2.getTopic(),list);
        Assert.assertEquals(risk2.getFrameworkId(),1);
        Assert.assertEquals(risk2.getUser(),"user");
        
        // test #7  - update value of question text
        risk.setName("updated");
        risk.setTopic(list2);
        risk.setFrameworkId(2);
        risk.setUser("newuser");
        riskService.save(risk);
        
        Risk risk3 = riskService.findById(risk.getId());
        Assert.assertNotNull(risk3);
        Assert.assertEquals(risk3.getName(),"updated");
        Assert.assertEquals(risk3.getUser(),"newuser");
        Assert.assertEquals(risk3.getFrameworkId(),2);
        Assert.assertEquals(risk3.getTopic(),list2);
        
        // test #8  - that risk was deleted correctly
        
        riskService.delete(risk);
        long countAfterDelete = riskService.findAll().size();
        Assert.assertEquals(countAfterInsert-1, countAfterDelete);
    }
    
    // test #9  - above we played with dummy data, here we work with real data in DB
    @Test
    public void shouldFindRiskById() {
        Risk risk = riskService.findById(1L);
        Assert.assertNotNull(risk);
        Assert.assertEquals("no risk", risk.getName());
        Assert.assertEquals("adm", risk.getUser());
    }
    
    @Test
    public void shouldCreateNewUserAndDeleteThatUser() {
    	
		// Test #10  - adding new card
    		User user = new User();
        user.setUsername("abc");
        user.setFrameworkSeln(list);
        user.setCardTypeSeln("xyz");
        user.setSkin(1);

        long countBeforeInsert = userService.findAll().size();
        userService.save(user);
        long countAfterInsert = userService.findAll().size();
        Assert.assertEquals(countBeforeInsert+1, countAfterInsert);
            
        // test #11  -  values were added correctly
        User user2 = userService.findById(user.getId());
        Assert.assertNotNull(user2);
        Assert.assertEquals(user2.getUsername(),"abc");
        Assert.assertEquals(user2.getFrameworkSeln(),list2);
        Assert.assertEquals(user2.getCardTypeSeln(),"xyz");
        Assert.assertEquals(user2.getSkin(),1);
        
        // test #12  - update value of question text
        user.setUsername("updated");
        user.setFrameworkSeln(list);
        user.setCardTypeSeln("xyz2");
        user.setSkin(2);
        userService.save(user);
        
        User user3 = userService.findById(user.getId());
        Assert.assertNotNull(user3);
        Assert.assertEquals(user3.getUsername(),"updated");
        Assert.assertEquals(user3.getFrameworkSeln(),list2);
        Assert.assertEquals(user3.getCardTypeSeln(),"xyz2");
        Assert.assertEquals(user3.getSkin(),2);
        
        // test #13  - that user was deleted correctly
        userService.delete(user);
        long countAfterDelete = userService.findAll().size();
        Assert.assertEquals(countAfterInsert-1, countAfterDelete);
    }
    
    // test #14  - above we played with dummy data, here we work with real data in DB
    @Test
    public void shouldFindUserById() {
        User user = userService.findById(1L);
        Assert.assertNotNull(user);
        Assert.assertEquals("a@b.com", user.getUsername());
    }
    

    // User class ("user_prefs" table in MySQL) has lots of fields. 
    // Checking constructor and equals for randomly selected fields of different types
    @Test
    public void constructorShouldSetDigfferentTypesOfParamsAndTestEqualsWorkingProperlyForAllTypes() {
        User user = new User();
        user.setUsername("abc");
        user.setFrameworkSeln(list);
        user.setTopicsSeln(list);
        user.setCurrentCardId(1);
        Assert.assertNotNull(user);
        Assert.assertEquals("abc", user.getUsername());
        Assert.assertEquals(list, user.getFrameworkSeln());
        Assert.assertEquals(list, user.getTopicsSeln());
        Assert.assertEquals("1 is expected and "+user.getCurrentCardId()+" is returned",1, user.getCurrentCardId());        
    }
}