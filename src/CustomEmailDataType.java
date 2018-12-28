/**
 * Create by Kushal Gevaria and Akshay Pudage
 */
package org.h2.api;
import java.security.NoSuchAlgorithmException;
import java.sql.Types;
import java.util.Locale;
import org.h2.message.DbException;
import org.h2.store.DataHandler;
import org.h2.util.JdbcUtils;
import org.h2.value.DataType;
import org.h2.value.Email;
import org.h2.value.Value;
import org.h2.value.ValueEmail;
import org.h2.value.ValueJavaObject;


public class CustomEmailDataType implements CustomDataTypesHandler{

	public final static String emailDatatypeName = "EMAIL";
	public final DataType emailDataType;
	
	public CustomEmailDataType() {
		DataType incomingDatatype = new DataType();
		incomingDatatype.name = emailDatatypeName;
		incomingDatatype.type = Value.EMAIL;
		incomingDatatype.sqlType = Types.JAVA_OBJECT;
		emailDataType = incomingDatatype;
	}
	
	@Override
	public DataType getDataTypeByName(String name) {
		if (name.toUpperCase(Locale.ENGLISH).equals(emailDatatypeName)){
			System.out.println("Getting EMAIL datatype by name.");
			return emailDataType;
		}else{
			System.out.println("Did not get EMAIL datatype by name.");
			return null;
		}
	}

	@Override
	public DataType getDataTypeById(int type) {
		if (type == Value.EMAIL){
			System.out.println("Getting EMAIL datatype by type.");
			return emailDataType;
		}else{
			System.out.println("Did not get EMAIL datatype by type.");
			return null;
		}
	}

	@Override
	public int getDataTypeOrder(int type) {
		if (type == Value.EMAIL){
			return 53_000;
		}else{
			throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:" + type);
		}
	}

	@Override
	public Value convert(Value source, int targetType) {
		System.out.println("In Convert: "+source.getType()+" "+targetType);
		
		if (targetType == source.getType()){
			return source;
		}
		else if (targetType == Value.EMAIL){
			if (source.getType() == Value.JAVA_OBJECT){
				try {
					return ValueEmail.get((Email) JdbcUtils.deserialize(source.getBytesNoCopy(), null));					
				}catch (Exception e){
					throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, source.getString());
				}
			}
			else if (source.getType() == Value.STRING){
				try {
					System.out.println("For String Check: " + source.getString());
					Value emailValue = ValueEmail.get(new Email(source.getString()));
					System.out.println("Got the email value as: " + emailValue.getString());
					return emailValue;
				}catch (Exception e){
					throw DbException.get(ErrorCode.INVALID_EMAIL_ERROR_CODE, source.getString());
				}
			}
			else if (source.getType() == Value.BYTES){
				try {
					return ValueEmail.get((Email) JdbcUtils.deserialize(source.getBytesNoCopy(), null));
				}catch (Exception e){
					throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, source.getString());
				}
			}
			System.out.println("Something went wrong, inner else while converting email");
			throw DbException.get(ErrorCode.DATA_CONVERSION_ERROR_1, source.getString());
		}else{
			System.out.println("Something went wrong, outer else while converting email");
			return source.convertTo(targetType);
		}
	}

	@Override
	public String getDataTypeClassName(int type) {
		if (type == Value.EMAIL){
			return Email.class.getName();
		}
		else{
			throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:"+type);
		}
	}

	@Override
	public int getTypeIdFromClass(Class<?> cls) {
		if (cls == Email.class){
			return Value.EMAIL;
		}
		return Value.JAVA_OBJECT;
	}

	@Override
	public Value getValue(int type, Object data, DataHandler dataHandler) {
		System.out.println("Get the Value: "+type+" "+data.getClass());
		if (type == Value.EMAIL && data instanceof Email) {
			try {
				return ValueEmail.get((Email) data);
			}		
			catch (NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			}
		}
		return ValueJavaObject.getNoCopy(data, null, dataHandler);
	}

	@Override
	public Object getObject(Value value, Class<?> cls) {
		if (cls.equals(Email.class)) {			
			if(value.getType() == Value.EMAIL)
				return value.getObject();
			return convert(value, Value.EMAIL).getObject();
		}
		else
			throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:" + value.getType());
	}

	@Override
	public boolean supportsAdd(int type) {
		return false;
	}

	@Override
	public int getAddProofType(int type) {
		if (type == Value.EMAIL)
			return type;
		else
			throw DbException.get(ErrorCode.UNKNOWN_DATA_TYPE_1, "type:" + type);
	}

}
