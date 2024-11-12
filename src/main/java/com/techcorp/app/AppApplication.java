package com.techcorp.app;

import com.techcorp.app.domain.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@ImportResource("classpath:beans.xml")
public class AppApplication {
	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(AppApplication.class, args);

//		Person ceo = (Person) context.getBean("ceo");
//		Person vp = (Person) context.getBean("vp");
//		Person secretary = (Person) context.getBean("secretary");

//		System.out.println("Key Roles:");
//		System.out.println(ceo);
//		System.out.println(vp);
//		System.out.println(secretary);
	}
}
