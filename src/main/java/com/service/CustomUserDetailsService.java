package com.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.SignUpDTO;
import com.model.Account;
import com.model.UserInfoDetails;
import com.repository.AccountRepository;
import com.util.EmailUtil;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private EntityManager entityManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> userDetail = accountRepository.findByUsername(username);

		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
	}

	public String addUser(SignUpDTO signUpDTO) {
		Account account = new Account();
		account.setName(signUpDTO.getName());
		account.setUsername(signUpDTO.getUsername());
		account.setMail(signUpDTO.getMail());
		account.setRoles("USER");
		account.setPassword(encoder.encode(signUpDTO.getPassword()));
		accountRepository.save(account);
		return "User Added Successfully";
	}

	public String forgotPassword(String mail) {
		Account account = accountRepository.findByMail(mail)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + mail));

		try {
			emailUtil.sendSetPassword(mail);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send set password please try again");
		}
		return "Please check your email to set new password to your account";
	}

	public String setPassword(String mail, String newPassword) {
		Account account = accountRepository.findByMail(mail)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + mail));

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);

		account.setPassword(encodedPassword);
		accountRepository.save(account);
		return "New password set successfully login with new password";
	}
	
	public Account getUserInfoByUsername(String username) {
		try {
		
			String queryString = "SELECT u FROM Account u WHERE u.username = :username";	
			Query query = entityManager.createQuery(queryString);
			query.setParameter("username", username);
			
			return (Account) query.getSingleResult();
		} catch (NoResultException e) {
			throw new UsernameNotFoundException("User not Found!!!");	
		}
		
	}
	
}