package com.elelco.elelcomobilesalesdriza;
import android.text.*;
import java.util.regex.*;

public class DecimalDigitsInputFilter implements InputFilter
{
	Pattern mPattern;
	public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero){
		//mPattern=Pattern.compile( "^(?!\\.?$)\\d{0,6}(\\.\\d{0,2})?$");//
		if (digitsAfterZero==0) mPattern=Pattern.compile("^[1-9]+[0-9]{0,5}$");
	else	 mPattern=Pattern.compile("[0-9]{1," + (digitsBeforeZero) + "}+((\\.[0-9]{0," + (digitsAfterZero) + "})?)||(\\.)?");
		
	}
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
//		Matcher matcher=mPattern.matcher(source);
//		if (!matcher.matches())
//			return "";
//			return  null;
			
		if (end > start) {
			String destTxt = dest.toString();
			String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
			Matcher matcher=mPattern.matcher(resultingTxt);
			if (!matcher.matches())
			return "";
//			if (!resultingTxt.matches("^\\d(\\d(\\.\\d{0,2})?)?")) {
//				return "";
//			}
		}
       return null;
			
	}
}
