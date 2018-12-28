/**
 * Created by Kushal Gevaria and Akshay Pudage
 */
package org.h2.value;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.h2.api.ErrorCode;
import org.h2.engine.Mode;
import org.h2.message.DbException;
import org.h2.util.JdbcUtils;


public class ValueEmail extends Value{

	protected Email email;
	
	ValueEmail(String email) throws NoSuchAlgorithmException{
		this.email = new Email(email);
	}
	
	public ValueEmail(Email emailValue){
		this.email = emailValue;
	}
	
	public static Value get(Email emailObj)  throws NoSuchAlgorithmException {
		ValueEmail valueemailobj = new ValueEmail(emailObj);
		System.out.println("Get Email Value" + valueemailobj.getString());
        return valueemailobj;
    }

	@Override
	public String getSQL() {
		System.out.println("Get SQL");
		return email.toString();
	}

	@Override
	public int getType() {
		System.out.println("Get Type "+ Value.EMAIL);
		return Value.EMAIL;
	}

	@Override
	public long getPrecision() {
		return 0;
	}

	@Override
	public int getDisplaySize() {
		return 0;
	}
	
	@Override
	public String getString() {
		System.out.println("Get Email String" + email.toString());
		return email.toString();
	}

	@Override
	public Object getObject() {
		System.out.println("Getting email object.");
		return email;
	}

	@Override
	public void set(PreparedStatement prep, int parameterIndex) throws SQLException {
		System.out.println("Setting email value.");
		//prep.setString(parameterIndex, email.getEmail());
		Object obj = JdbcUtils.deserialize(getBytesNoCopy(), getDataHandler());
        prep.setObject(parameterIndex, obj, Types.JAVA_OBJECT);
	}
	
	@Override
	public int compareTypeSafe(Value v, CompareMode mode) {
		System.out.println("Comparison");
		ValueEmail vEmail = (ValueEmail) v.getObject();
		System.out.println(mode.compareString(this.email.toString(), vEmail.email.toString(), false));
		return mode.compareString(this.email.toString(), vEmail.email.toString(), false);
	}

	@Override
	public int hashCode() {
		System.out.println("Hashcode: " + email.hashCode());
		return email.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		System.out.println(email.toString() + "<-Match->" + ((ValueEmail) other).getString());
		return other instanceof ValueEmail
				&& email.equals(((ValueEmail) other).email);
	}
	
	@Override
    public Value convertTo(int targetType, int precision, Mode mode, Object column, String[] enumerators) {
		if (getType() == targetType) {
            return this;
        }
        switch (targetType) {
            case Value.BYTES: {
                return ValueBytes.getNoCopy(JdbcUtils.serialize(email, null));
            }
            case Value.STRING: {
                return ValueString.get(email.toString());
            }
            case Value.JAVA_OBJECT: {
                return ValueJavaObject.getNoCopy(JdbcUtils.serialize(email, null));
            }
        }
        throw DbException.get(
                ErrorCode.DATA_CONVERSION_ERROR_1, getString());
    }
}