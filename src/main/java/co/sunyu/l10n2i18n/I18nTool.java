package co.sunyu.l10n2i18n;

import java.io.*;
import java.util.*;

import co.sunyu.l10n2i18n.parser.SimpleJSPParser;
import co.sunyu.l10n2i18n.util.FileUtil;

public class I18nTool {

	private static final String BASE_PATH = "/Users/sunyu/git/agent/agent_web/WebContent/WEB-INF/pages";

	private static final List<String> sourceList = new LinkedList<String>();
	private static final List<String> targetList = new LinkedList<String>();

	public static void main(String[] args) {

		// get jsp file List
		List<File> files = new ArrayList<File>();
		FileUtil.getJspFiles(new File(BASE_PATH), files);
		// loop
		for (File file : files) {
			// parse jsp file
			SimpleJSPParser.processJSPFile( file, sourceList, targetList) ;
		}

		FileUtil.saveI18nList(sourceList, BASE_PATH + "/message_CN.properties");
		FileUtil.saveI18nList(targetList, BASE_PATH + "/message_EN.properties");

		System.out.println("Finish!");

	}

}
