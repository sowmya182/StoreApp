package com.kosuri.stores.handler;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl {

	private final JavaMailSender javaMailSender;

	public boolean sendEmailMessage(String to, String message, String subject) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message, true);
			javaMailSender.send(msg);
			return true;
		} catch (MessagingException messagingException) {
			log.error("Failed Sending Email To Email Id {}", to);
			return false;
		}
	}
}
