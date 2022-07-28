package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);
	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	public FundamentosApplication(@Qualifier("componentToImplement") ComponentDependency componentDependency,
								  MyBean myBean, MyBeanWithDependency myBeanWithDependency,MyBeanWithProperties myBeanWithProperties,
								  UserPojo userPojo, UserRepository userRepository ) {

		this.componentDependency  = componentDependency;
		this.myBean               = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo             = userPojo;
		this.userRepository       = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
	}

	private void getInformationJpqlFromUser(){
		LOGGER.info("Usuario con el metodo findByUserEmail: " + userRepository.findByUserEmail("julie@mail.com")
				.orElseThrow(() -> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("user", Sort.by("id").descending())
				.forEach(user -> LOGGER.info("Usuario con metodo sort: " + user));
	}

	private void saveUsersInDataBase(){
		User user1  = new User("John"   , "john@mail.com"   , LocalDate.of(2021 ,3  ,20));
		User user2  = new User("Julie"  , "julie@mail.com"  , LocalDate.of(2021 ,5  ,21));
		User user3  = new User("Daniela", "daniela@mail.com", LocalDate.of(2021 ,7  ,12));
		User user4  = new User("user4"  , "user4@mail.com"  , LocalDate.of(2021 ,11 ,11));
		User user5  = new User("user5"  , "user5@mail.com"  , LocalDate.of(2021 ,2  ,27));
		User user6  = new User("user6"  , "user6@mail.com"  , LocalDate.of(2021 ,3  ,15));
		User user8  = new User("user8"  , "user8@mail.com"  , LocalDate.of(2021 ,4  ,2 ));
		User user9  = new User("user9"  , "user9@mail.com"  , LocalDate.of(2021 ,6  ,8 ));
		User user10 = new User("user10" , "user10@mail.com" , LocalDate.of(2021 ,8  ,30));
		List<User> list = Arrays.asList(user1, user2,user3, user4, user5, user6, user8, user9, user10);
		list.stream().forEach(userRepository::save);
	}

	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + " - " + userPojo.getPassword() + " - " + userPojo.getAge());
		try{
			//error
			int value = 10 / 0;
			LOGGER.debug("Mi valor: " + value);
		}catch(Exception e){
			LOGGER.error("Esto es un error al dividir por 0 " + e.getStackTrace());
		}
	}
}
