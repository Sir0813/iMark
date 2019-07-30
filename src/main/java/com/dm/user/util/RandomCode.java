package com.dm.user.util;

import java.util.Random;

public class RandomCode {

	public static String getCode() {
		char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'G', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int count = 0;
		while (true) {
			// 定义随机数的范围并且产生一个随机的下标
			int index = random.nextInt(codeSequence.length);
			// 随机的取出一个数
			char c = codeSequence[index];
			// 不让字符重复,并且把一个字符变成字符串
			if (sb.indexOf(c + "") == -1) {
				sb.append(c);
				count++;
				if (count == 4) {
					break;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 生成随机N位数
	 * @param n
	 * @return
	 */
	public static long generateRandomNumber(int n){
        return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
    }
	
}
