package com.ufcg.br.listapramim.usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    Users user = userRepository.findByEmail(email);
	    if(user != null) {
	        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
	        return buildUserForAuthentication(user, authorities);
	    } else {
	        throw new UsernameNotFoundException("username not found");
	    }
	}
	
	public Users findUserByEmail(String email) {
	    return userRepository.findByEmail(email);
	}
	
	public Users saveUser(Users user) {
	    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    Role userRole = roleRepository.findByRole("ADMIN");
	    user.setRoles(new HashSet<>(Arrays.asList(userRole)));
	    return userRepository.save(user);
	}
	
	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
	    Set<GrantedAuthority> roles = new HashSet<>();
	    userRoles.forEach((role) -> {
	        roles.add(new SimpleGrantedAuthority(role.getRole()));
	    });

	    List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
	    return grantedAuthorities;
	}
	
	private UserDetails buildUserForAuthentication(Users user, List<GrantedAuthority> authorities) {
	    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

	public int numeroUsers() {
		return userRepository.findAll().size();
	}

	public Users getUserCurrent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.userRepository.findByEmail(authentication.getName());
	}
	

	@Value("${sqs.url}")
	private String sqsURL;
	final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

	@Async
	@SqsListener(value = "${sqs.url}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void consume (String message, Acknowledgment acknowledgment) throws InterruptedException, ExecutionException {
		System.out.println("entrando no sqslistener");
		
		try {
			String[] values = message.split(" ");
			if(values.length == 2) {
				String email = values[0];
				String password = values[1];
				changePassword(email,password);
				acknowledgment.acknowledge().get(); //To delete message from queue				
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println("saindo do sqslistener");
	}

	private void changePassword(String email, String password) {
		try {
			Users user = findUserByEmail(email);
			user.setPassword(bCryptPasswordEncoder.encode(password));
			this.userRepository.save(user);	
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}	
	
	@Bean
	public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
	    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
	    simpleAsyncTaskExecutor.setConcurrencyLimit(50);
	    return simpleAsyncTaskExecutor;
	}

	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(
	        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor) {

	    SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
	    factory.setAutoStartup(true);
	    factory.setTaskExecutor(simpleAsyncTaskExecutor);
	    factory.setWaitTimeOut(20);
	    factory.setMaxNumberOfMessages(10);

	    return factory;
	}
	
}
