package stage3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import _io.*;
import _misc.Constants;
import stage3.InfoSet.*;
import stage3.InfoSet.InfoString;
import  stage3.NameMap.*;

public class test {
	
	public static final String ROOT_GAME_INPUT_DIR = Constants.DATA_FILE_REPOSITORY +
			"stage3" + Constants.dirSep + "root" + Constants.dirSep;
	public static NameMap rootNamesP1;
	public static NameMap rootNamesP2;

	
	public static void main(String arrg[]) throws Exception {//you need to add main function 
//	List<String> list = Arrays.asList("zero", "one", "two");
//	
//
//	int i = 0;
//	for (Iterator<String> it = list.iterator(); it.hasNext(); i++) {
//	    String s = it.next();
//	    System.out.println(i + ": " + s);
//	}
	
//	String inNamesP1 = ROOT_GAME_INPUT_DIR + "nameMap.p1.obj";
//	String inNamesP2 = ROOT_GAME_INPUT_DIR + "nameMap.p2.obj";
//	rootNamesP1 = ReadBinaryNameMap.getNameMap(inNamesP1);
//	rootNamesP2 = ReadBinaryNameMap.getNameMap(inNamesP2);
	
	String inFile = Constants.DATA_FILE_REPOSITORY +
			"stage3" + Constants.dirSep + "root" + Constants.dirSep + "constraints.p1.obj";
	//System.out.println(inFile);
	Object[] cmInfo = ReadBinaryConstraintMatrix.getCm(inFile);
	ConstraintMatrix cmP1 = (ConstraintMatrix) cmInfo[0];
	//String filename = "C:\\Users\\jinyi\\Desktop\\game-theory-poker\\poker_data\\14-card holdem\\stage3\\root\\test.txt";
//	int shortName = 20779  ;
//	int shortName1 = 20780 ;
//	int shortName2 = 20781 ;
//	int shortName3 = 19686 ;
//	InfoString longName =  ((NameMap) rootNamesP1).getLong(shortName);
//	InfoString longName1 =  ((NameMap) rootNamesP1).getLong(shortName1);
//	InfoString longName2 =  ((NameMap) rootNamesP1).getLong(shortName2);
//	InfoString longName3 =  ((NameMap) rootNamesP1).getLong(shortName3);
//
//	System.out.println("20779:" + longName);
//	System.out.println("20780:" + longName1);
//	System.out.println("20781:" + longName2);
//	System.out.println("19686:" + longName3);
	cmP1.printRowMatirx();

}


}
