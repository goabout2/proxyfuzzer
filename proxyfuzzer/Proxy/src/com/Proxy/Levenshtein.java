package com.Proxy;

import java.io.Flushable;

public class Levenshtein {
	private int compare(String str, String target) {
		  int d[][]; // 矩阵
		  int n = str.length();
		  int m = target.length();
		  int i; // 遍历str的
		  int j; // 遍历target的
		  char ch1; // str的
		  char ch2; // target的
		  int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		  if (n == 0) {
		   return m;
		  }
		  if (m == 0) {
		   return n;
		  }
		  d = new int[n + 1][m + 1];
		  for (i = 0; i <= n; i++) { // 初始化第一列
		   d[i][0] = i;
		  }
		  for (j = 0; j <= m; j++) { // 初始化第一行
		   d[0][j] = j;
		  }
		  for (i = 1; i <= n; i++) { // 遍历str
		   ch1 = str.charAt(i - 1);
		   // 去匹配target
		   for (j = 1; j <= m; j++) {
		    ch2 = target.charAt(j - 1);
		    if (ch1 == ch2) {
		     temp = 0;
		    } else {
		     temp = 1;
		    }
		    // 左边+1,上边+1, 左上角+temp取最小
		    d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
		      + temp);
		   }
		  }
		  return d[n][m];
		 }		
	
		 private int min(int one, int two, int three) {
		  return (one = one < two ? one : two) < three ? one : three;
		 }		 
		 /**
		 * 获取两字符串的相似度
		 *
		 * @param str

		 * @param target

		 * @return
		 */
		 public float getSimilarityRatio(String str, String target) {
		  return 1 - (float) compare(str, target)
		    / Math.max(str.length(), target.length());
		 }
		 
		 public float getSimilarityLength(String str,String target){
			 int lenghtOfstr = str.length();
			 int lengthOftarget = target.length();
			 System.out.println("lenghtOfstr is:"+lenghtOfstr);
			 System.out.println("lengthOftarget is:"+lengthOftarget);
			 if(lengthOftarget == 0){
				 return 0;
			 }
			 return (float)lenghtOfstr/(float)lengthOftarget;
		 }
		 
		 public float getSimilarityWrong(String target){
			 String[] sensitive = new String[]{"sql","exception","wrong","error","错误","select"};
			 int i =0;
			 float result;
			 for(int j = 0;j < sensitive.length;j++){
				 String tmp = sensitive[j];
				 if(target.contains(tmp)){
					 i++;
				 }else{
					 continue;
				 }
			 }
			 result = (float)i/(float)sensitive.length;
			 return result;
		 }

		 public static void main(String[] args) {
		  Levenshtein lt = new Levenshtein();
		  String str = "AB";
		  String target = "FECFEC";
		  //System.out.println("similarityRatio=" + lt.getSimilarityRatio(str, target));
		  //System.out.println(lt.getSimilarityLength(str, target));
		  System.out.println(lt.getSimilarityWrong("his message is print by ExceptionHandler,something maybe wrong!the exception message is"));
		 }
}
