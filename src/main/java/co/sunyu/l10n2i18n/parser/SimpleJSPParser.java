package co.sunyu.l10n2i18n.parser;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.MessageFormat;

import co.sunyu.l10n2i18n.util.ChineseCharToEnUtil;
import co.sunyu.l10n2i18n.util.TranslateUtil;

public class SimpleJSPParser {

	// chinese regex
	private static final Pattern SOURCE_PATTERN = Pattern
			.compile("[ \\w\\ufe30-\\uffa0\\u4e00-\\u9fa5]*[\\u4e00-\\u9fa5]+[ \\w\\ufe30-\\uffa0\\u4e00-\\u9fa5]*");
	// symbol regex
	private static final Pattern SYMBOL = Pattern
			.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
	// spring message tag format
	private static final MessageFormat SPRING_TAG = new MessageFormat("<!-- {0} --><spring:message code=\"{1}\"/>");
	// file encoding
	private static final String ENCODING = "utf-8";

	/**
	 * parse JSP File
	 * 
	 * 
	 * @param file
	 *            JSP File
	 */
	public static void processJSPFile(File file, List<String> sourceList, List<String> targetList) {

		Map<String, String> sourceMap = new HashMap<String, String>();

		try {
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
			String fileName = file.getName();
			// create cache file
			File tmpfile = new File(file.getParentFile().getAbsolutePath() + File.pathSeparator + fileName + ".tmp");
			BufferedWriter writer = new BufferedWriter(new FileWriter(tmpfile));
			boolean flag = false;

			sourceList.add("######" + file.getParentFile() + File.pathSeparator + fileName + "######");
			targetList.add("######" + file.getParentFile() + File.pathSeparator + fileName + "######");

			// replace chinese to taglib
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				} else {
					line = parseLine(fileName, line, sourceMap);
					writer.write(line + "\n");
					flag = true;
				}
			}

			for (Map.Entry<String, String> entry : sourceMap.entrySet()) {

				sourceList.add("#" + entry.getKey());
				sourceList.add(entry.getValue() + "=" + entry.getKey());

				targetList.add("#" + entry.getKey());
				// trans chinese
				String translatedText = TranslateUtil.baiduTrans(entry.getKey(), "auto", "en");

				targetList.add(entry.getValue() + "=" + translatedText);

			}

			is.close();

			writer.flush();
			writer.close();

			if (flag) {//if file changed
				file.renameTo(new File(file.getAbsolutePath() + ".bak"));
				tmpfile.renameTo(new File(file.getAbsolutePath()));
			} else
				tmpfile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// replace chinese to taglib
	public static String parseLine(String fileName, String line, Map<String, String> sourceMap) {

		Matcher sourceMatcher = SOURCE_PATTERN.matcher(line);
		StringBuffer lineStrBuffer = new StringBuffer();
		while (sourceMatcher.find()) {
			
			String sourceString = sourceMatcher.group();
			Matcher symbolMatcher = SYMBOL.matcher(sourceString);
			String sourceStringNoSym = symbolMatcher.replaceAll("").trim();
			System.out.println("sourceString:" + sourceString);
			String propId = fileName.replace(".jsp", ".") + ChineseCharToEnUtil.getAllFirstLetter(sourceStringNoSym);
			sourceMap.put(sourceString, propId);
			
			sourceMatcher.appendReplacement(lineStrBuffer, SPRING_TAG.format(new String[] { sourceString, propId }));
		}
		sourceMatcher.appendTail(lineStrBuffer);

		return lineStrBuffer.toString();
	}
}
