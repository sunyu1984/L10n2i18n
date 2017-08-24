package co.sunyu.l10n2i18n.util;

import java.io.*;
import java.util.*;

public class FileUtil {

	public static void getJspFiles(File path, List<File> fileList) {
		File[] files = path.listFiles();
		for (File file : files)
			if (file.isDirectory() && !file.isHidden())
				getJspFiles(file, fileList);
			else if (file.getName().toLowerCase().endsWith(".jsp"))
				fileList.add(file);
	}

	

	public static void saveI18nMap(Map<String, String> map, String path) {
		Properties ps = new Properties();
		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			for (Map.Entry<String, String> entry : map.entrySet()) {
				ps.setProperty(entry.getValue(), entry.getKey());
			}
			ps.store(fos, null);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveI18nList(List<String> list, String path) {

		File file = new File(path);
		FileWriter fw = null;
		BufferedWriter bw = null;
		Iterator<String> iter = list.iterator();
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			while (iter.hasNext()) {
				bw.write(iter.next());
				bw.newLine();
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
