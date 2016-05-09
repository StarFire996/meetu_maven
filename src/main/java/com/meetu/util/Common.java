package com.meetu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Common {

	public static String encryptByMD5 (String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
	        return toHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
    }
	private static String toHex(byte buffer[])
    {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++)
        {
            sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
        }
        return sb.toString();
    }
	
	public synchronized static String generateToken() {
		long current = System.currentTimeMillis();
		String now = new Long(current).toString();
		return encryptByMD5(now);
	}
	public static String generateId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static int indexString(String[] strs, String s){
	    for(int i = 0; i < strs.length; i++){
	        if(strs[i].equals(s)){
	            return i;
	         }
	    }
	    return -1;
	}
	
	
	public static boolean checkMobileNumber(String mobileNumber){ 
		
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(mobileNumber);
//		return m.matches();
		
		return true;
		
	}
	public static String formatNull(String value){
		if(value == null){
			return "";
		}
		return value;
	}
	//计算年龄
    public static Integer getAgeByBirthday(Date birthday) {
    	if(birthday == null){
    		return null;
    	}
    	
    	Calendar cal = Calendar.getInstance();

    	if (cal.before(birthday)) {
    		throw new IllegalArgumentException(
    				"The birthDay is before Now.It's unbelievable!");
    	}

    	int yearNow = cal.get(Calendar.YEAR);
    	int monthNow = cal.get(Calendar.MONTH) + 1;
    	int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

    	cal.setTime(birthday);
    	int yearBirth = cal.get(Calendar.YEAR);
    	int monthBirth = cal.get(Calendar.MONTH) + 1;
    	int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

    	int age = yearNow - yearBirth;

    	if (monthNow <= monthBirth) {
    		if (monthNow == monthBirth) {
    			// monthNow==monthBirth 
    			if (dayOfMonthNow < dayOfMonthBirth) {
    				age--;
    			}
    		} else {
    			// monthNow>monthBirth 
    			age--;
    		}
    	}
    	return age;
    }
    
    public static String getConstellation(Date date){
    	final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
    	final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
    	if(date==null){
    		return "";
    	}
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	int month = cal.get(Calendar.MONTH);
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	if(day<constellationEdgeDay[month]){
    		month = month -1;
    	}
    	if(month>=0){
    		return constellationArr[month];
    	}
    	// default to return
    	return constellationArr[9];
    }
    /**
	 * 过滤查询条件中的特殊字符
	 * 
	 */
	public static String dealWithParam(String param){
		String realParam = param.toLowerCase();
		String regEx="%+|_+"; 
		Pattern pat=Pattern.compile(regEx);
		Matcher mat=pat.matcher(realParam);
		return mat.replaceAll("\\\\%"); 
		
	}
}
