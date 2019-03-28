package script.analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ini4j.Ini;
import org.ini4j.Wini;

import java.util.Scanner;
import java.util.Set;


/**
 *
 * @author hundun
 * Created on 2019/03/26
 */
public class ActorWordCounter {
	
	Map<String, Integer> actorWords = new HashMap<>();
	
	private static final String CONFIG_FILE_NAME = "config.ini";
	
	boolean isLog;
	
	public static void main(String[] args) throws IOException {
		
//		Scanner sc = new Scanner(System.in);
//		System.out.print( "输入剧本文件名:" ); 
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        String fileName = br.readLine(); 
		
		ActorWordCounter counter = new ActorWordCounter();
		
		Wini configIni;
		File config = new File(CONFIG_FILE_NAME);
		if (config.exists()) {
			configIni = new Wini(config);
		} else {
			System.out.println("config文件不存在:" + CONFIG_FILE_NAME);
			return;
		}
		
		
		String fileName = configIni.get("main", "script-file-name");
		counter.isLog = Boolean.parseBoolean(configIni.get("main", "log"));
		
        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
        	counter.handleActorWordInLine(line);
        }
        
        
        
        System.out.println("=============== result ==============");
        
        List<Entry<String, Integer>> list = new ArrayList<>(counter.actorWords.entrySet());
        list.sort(Entry.<String, Integer>comparingByValue().reversed());
        System.out.println(list.toString());    
	}
	
	
	/**
	 * 0.不区分中英文符号，处理时会先转为中文（正则不转义）
	 * 1.开头为“>”或“//”，结束。开头不含“：”，结束。
	 * 2.移除这个开头
	 * 3.移除“（）”内的内容，移除“【】”内的内容,移除空格
	 * @param line
	 * @return
	 */
	private void handleActorWordInLine(String line) {
		// 中文化
		line = line.replaceAll(":", "：");
		line = line.replaceAll("\\(", "（");
		line = line.replaceAll("\\)", "）");
		
		String actorWordLineStart = "：";
		int index = line.indexOf(actorWordLineStart);
		boolean isActorWordLine = index > 0 && index < 8 && !line.startsWith(">") && !line.startsWith("//");
		if (!isActorWordLine) {
			return;
		}
		
		String actorName = line.substring(0, index);
		line = line.substring(index + actorWordLineStart.length());
		line = line.replaceAll("(【[^】]*】)", "");
		line = line.replaceAll("(（[^）]*）)", "");
		line = line.replaceAll(" ", "");
		int actorWordLength = line.length();
		
		if (isLog) {
			System.out.println(actorName + "--"+ actorWordLength + "--" + line);
		}
		
		// 累加
		actorWords.merge(actorName, actorWordLength, (oldValue, newValue) -> oldValue + newValue);
					
		
	}

}
