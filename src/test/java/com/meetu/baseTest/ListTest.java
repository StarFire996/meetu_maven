package com.meetu.baseTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ListTest {

	public static void main(String[] args) {
		List<String> test = new ArrayList<String>();
		test.add("a0");
		test.add("b0");
		test.add("c0");
		test.add("d0");
		test.add("a1");
		test.add("b1");
		test.add("c1");
		test.add("d1");
		test.add("a2");
		test.add("b2");
		test.add("c2");
		test.add("d2");
		test.add("a3");
		test.add("b3");
		test.add("c3");
		test.add("d3");
		
		//run(test);
		List<String> test2 = new ArrayList<String>();
		test2.add("a0");
		test2.add("a0");
		test2.add("a0");
		test2.add("a0");
		test2.add("a0");
		test.removeAll(test2);
		System.out.println(test);
	}
	
	public static void run(List<String> test){
		List<String> removeList = new ArrayList<String>();
		for (String string : test) {
			if (StringUtils.contains(string, 'a')) {
				removeList.add(string);
				continue;
			}
		}
		test.removeAll(removeList);
	}
	
	@Test
	public void run(){
		System.out.println(new Date());
	}
}	
