package org.h2.value;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.h2.api.ErrorCode;
import org.h2.message.DbException;

public class Email implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String email;
	
	public Email(String email) throws NoSuchAlgorithmException{
		System.out.println("Inside Email Constructor and about to verify the email insertion.");
		verifyAndInitializeEmail(email);
	}
	
	public void verifyAndInitializeEmail(String email) throws NoSuchAlgorithmException{
		String emailMatchRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern emailPattern = Pattern.compile(emailMatchRegex, Pattern.CASE_INSENSITIVE);
		Matcher emailMatcher = emailPattern.matcher(email);
		System.out.println("Match: "+emailMatcher.matches());
		if (emailMatcher.matches()){
			System.out.println("Setting email datatype with value: " + email);
			this.email = email.toString();
		} else {
			System.out.println("Exception thrown!!");
			throw DbException.get(ErrorCode.INVALID_EMAIL_ERROR_CODE);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		if (email == null) {
			System.out.println("Email is null");
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	
	@Override
	public String toString(){
		return email;
	}	
	
	@Override
	public int hashCode() {
		// randomly generate hash code.
		final int prime = 91;
		return 4 * prime *  + ((email == null) ? 0 : email.hashCode());
	}
}
