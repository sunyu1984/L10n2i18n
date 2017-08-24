package co.sunyu.l10n2i18n.util;

import java.util.Properties;

import org.json.JSONObject;

import com.baidu.translate.demo.TransApi;

public class TranslateUtil {
	
	
	public static String baiduTrans(String sourceString , String sourceLocale, String targetLocale){
		Properties props = null;
		try {
			props = PropertiesUtil.loadProperty();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	TransApi api = new TransApi(props.getProperty("trans.baidu.appId"), props.getProperty("trans.baidu.appKey"));
        String translatedText=api.getTransResult(sourceString , sourceLocale, targetLocale);
        translatedText= new JSONObject(translatedText).getJSONArray("trans_result").getJSONObject(0).getString("dst");
		
		return translatedText;
	}
	
	

}
