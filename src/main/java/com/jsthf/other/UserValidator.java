package com.jsthf.other;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jsthf.model.User;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
      return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
      User user = (User) target;

      
      if(user.getLoadTypeSeln() == null) {
          errors.rejectValue("loadTypeSeln", "error_code1");
      }

      if(user.getCardTypeSeln() == null) {
          errors.rejectValue("loadTypeSeln", "error_code2");
      }

      if(user.getRandomNotSeln() == null) {
          errors.rejectValue("loadTypeSeln", "error_code3");
      }

    }

}