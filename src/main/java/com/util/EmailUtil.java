package com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendSetPassword(String mail) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(mail);
		mimeMessageHelper.setSubject("Set Password");
		mimeMessageHelper.setText(
				"""
												<div>
													<p>Your password reset request was successful</p>
													<p>Please Click on the following link to change your password</p>
												  <a href="http://localhost:3000/setnewpass?email=%s" target="_blank">Click link to set password</a>
												</div>
												"""
						.formatted(mail),true);

		javaMailSender.send(mimeMessage);
		
		
		
		
	}
	


}
