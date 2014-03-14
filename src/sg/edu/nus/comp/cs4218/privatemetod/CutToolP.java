package sg.edu.nus.comp.cs4218.privatemetod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
/*
 * 
 * Args[0] = cut;
 * Args[1] = -c;
 * Args[2] = List;
 * Args[3] = file
 * 
 * Args[0] = cut;
 * Args[1] = -c;
 * Args[2] = List;
 * Args[3] = -d;
 * Args[4] = delim;
 * Args[5] = file;
 *
 */
public class CutToolP extends CutTool implements ICutTool {
    public CutToolP(String[] args) {
		super(args);
		if (!args[0].equals("cut")) {
			setStatusCode(127);
		}
	}

		
    /**
     * Parameter a string on fromation [d]|[d1-d2] where d is a int
     * output a linkedlist with Integers d or d1-d2
     */
	public LinkedList<Integer> parseString(String currentPart) {
		String[] partsOfString= currentPart.split("-");
		Integer firstPart;
		LinkedList<Integer> returnList = new LinkedList<Integer>();
		firstPart = parseSingelNumber(partsOfString[0]);
		if (firstPart == -1) {
			returnList.clear();
			return returnList;
		}
		if(partsOfString.length==1){
			returnList.add(firstPart);
		}
		else if(partsOfString.length==2){
			Integer secondPart=parseSingelNumber(partsOfString[1]);
			if (secondPart == -1) {
				returnList.clear();
				return returnList;
			}
			while(firstPart<=secondPart){
				returnList.add(firstPart);
				firstPart++;
			}
		}
		else {
			setStatusCode(67);
		}
		return returnList;
	}
	/**
	 * Check if the string can be parsed to a int > -1 
	 * @param number
	 * @return number as int 
	 * @return -1 if number is not an int 
	 */
	
	public Integer parseSingelNumber(String number) {
		if(number.length() == 0){
			setStatusCode(67);
			return -1;
		}
		char tmp;
		for(int increment=0; increment<number.length(); increment++){
			tmp = number.charAt(increment);
			if(!Character.isDigit(tmp)){
				setStatusCode(67);
				return -1;
			}
		}
		return Integer.parseInt(number);
	}



}
