package de.softwarekollektiv.cg.uebungen.uebung6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SceneFactory {
	
	public static List<Scene> fromFile(String file) throws IOException{
		
		String contents = fileToString(file);

		List<Scene> scenesList = new LinkedList<Scene>();
		
		// -- divides different scenes in the file
		for (String scenes : contents.split("--")){
			int a = 0;
			Scene s = new Scene();
			for (String attrList : scenes.split("\\n")){
				if (!attrList.startsWith("//") ){
					attrList = attrList.trim();
					String[] atts = attrList.split(" ");
					if (atts[0].isEmpty()) continue;
					switch (a){
						case 0: 
							s.kar = Float.valueOf(atts[0]);
							s.kag = Float.valueOf(atts[1]);
							s.kab = Float.valueOf(atts[2]);
							break;
						case 1:
							s.iar = Float.valueOf(atts[0]);
							s.iag = Float.valueOf(atts[1]);
							s.iab = Float.valueOf(atts[2]);
							break;
						case 2:
							s.kdr = Float.valueOf(atts[0]);
							s.kdg = Float.valueOf(atts[1]);
							s.kdb = Float.valueOf(atts[2]);
							break;
						case 3:
							s.ilr = Float.valueOf(atts[0]);
							s.ilg = Float.valueOf(atts[1]);
							s.ilb = Float.valueOf(atts[2]);
							break;
						case 4:
							s.plx = Float.valueOf(atts[0]);
							s.ply = Float.valueOf(atts[1]);
							s.plz = Float.valueOf(atts[2]);
							break;
						case 5:
							s.c1 = Float.valueOf(atts[0]);
							s.c2 = Float.valueOf(atts[1]);
							s.c3 = Float.valueOf(atts[2]);
							break;
						case 6:
							s.ks = Float.valueOf(atts[0]);
							break;
						case 7:
							s.n = Float.valueOf(atts[0]);
							break;
					}
					a++;
				}	
			}
			scenesList.add(s);
		}
		
		return scenesList;
		
	}
	
	 private static String fileToString( String file ) throws IOException {
		    BufferedReader reader = new BufferedReader( new FileReader (file));
		    String line  = null;
		    StringBuilder stringBuilder = new StringBuilder();
		    String ls = System.getProperty("line.separator");
		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }
		    return stringBuilder.toString();
		 }
	 
	 public static void main(String[] args) throws IOException {
		SceneFactory.fromFile("res/scenes.txt"); 
	 }

}
