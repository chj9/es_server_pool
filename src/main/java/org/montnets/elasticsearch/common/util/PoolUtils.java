package org.montnets.elasticsearch.common.util;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: PoolUtils.java
* @Description: 该类的功能描述
*常用工具类
* @version: v1.0.0
* @author: chenhj
* @date: 2018年7月31日 下午3:37:32 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年7月31日     chenhj          v1.0.0               修改原因
 */
public final class PoolUtils {
	/** 检查是否为整型 */
	private static Pattern p = Pattern.compile("^\\d+$");

	/** 横杠 */
	private static final String ROD = "-";

	/** 第一天 */
	private static final String INIT_DAY = "01";

	/**
	 * 判断String类型的数据是否为空 null,""," " 为true "A"为false
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return (null == str || str.trim().length() == 0);
	}

	/**
	 * 判断String类型的数据是否为空 null,"", " " 为false "A", 为true
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断list类型的数据是否为空 null,[] 为 true
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(List<?> list) {
		return (null == list || list.size() == 0);
	}

	/**
	 * 判断list类型的数据是否为空 null,[] 为 false
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	/**
	 * 判断Map类型的数据是否为空 null,[] 为true
	 * 
	 * @return boolean
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (null == map || map.size() == 0);
	}

	/**
	 * 判断map类型的数据是否为空 null,[] 为 false
	 * 
	 * @return boolean
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}


	/**
	 * 字符串反转 如:入参为abc，出参则为cba
	 * 
	 * @param str
	 * @return
	 */
	public static String reverse(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return reverse(str.substring(1)) + str.charAt(0);
	}

	/**
	 * 获取当前long类型的的时间
	 * 
	 * @return long
	 */
	public static long getNowLongTime() {
		return System.currentTimeMillis();
	}

	/**
	 * long类型的时间转换成自定义时间格式
	 * 
	 * @param lo
	 *            long类型的时间
	 * @param format
	 *            时间格式
	 * @return String
	 */
	public static String longTime2StringTime(long lo, String format) {
		return new SimpleDateFormat(format).format(lo);
	}

	/**
	 * 获取当前String类型的的时间(自定义格式)
	 * 
	 * @param format
	 *            时间格式
	 * @return String
	 */
	public static String getNowTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * 格式化时间
	 * 
	 * @param format1
	 *            之前的 时间格式
	 * @param format2
	 *            之后的 时间格式
	 * @param time
	 *            时间
	 * @return String
	 * @throws ParseException
	 */
	public static String formatTime(String format1, String format2, String time) throws ParseException {
		SimpleDateFormat d1 = new SimpleDateFormat(format1);
		SimpleDateFormat d2 = new SimpleDateFormat(format2);
		time = d2.format(d1.parse(time));
		return time;
	}

	/**
	 * 格式化时间
	 * 
	 * @param ym
	 *            时间格式 yyyyMM
	 * @return String yyyy-MM-dd时间格式
	 * @throws ParseException
	 */
	public static String formatTime(int ym) throws ParseException {
		StringBuffer sb = new StringBuffer(String.valueOf(ym));
		sb.insert(4, ROD);
		sb.append(ROD).append(INIT_DAY);
		return sb.toString();
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 *            时间格式 yyyy-MM-dd
	 * @return int yyyyMM 时间格式
	 * @throws ParseException
	 */
	public static int formatTime(String time) throws ParseException {
		int ym = Integer.parseInt(time.substring(0, time.length() - 3).replaceAll(ROD, ""));
		return ym;
	}



	/**
	 * 获取几天之前的时间
	 * 
	 * @since 1.8
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getMinusDays(int day, String format) {
		return LocalDateTime.now().minusDays(day).format(DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 增加月份
	 * 
	 * @param time
	 *            格式为yyyy-MM-dd
	 * @param month
	 *            增加月份
	 * @return
	 */
	public static String addPlusMonths(String time, int month) {
		return LocalDate.parse(time).plusMonths(month).toString();
	}

	/**
	 * 时间相比得月份 如果是201711和201801相比，返回的结果是2 前面的时间要小于后面的时间
	 * 
	 * @param month
	 *            格式为yyyyMM
	 * @param toMonth
	 *            格式为yyyyMM
	 * @since jdk 1.8
	 * @return
	 */
	public static int diffMonth(String month, String toMonth) {
		int year1 = Integer.parseInt(month.substring(0, 4));
		int month1 = Integer.parseInt(month.substring(4, 6));
		int year2 = Integer.parseInt(toMonth.substring(0, 4));
		int month2 = Integer.parseInt(toMonth.substring(4, 6));
		LocalDate ld1 = LocalDate.of(year1, month1, 01);
		LocalDate ld2 = LocalDate.of(year2, month2, 01);
		return Period.between(ld1, ld2).getMonths();
	}


	/**
	 * 判断是否为整型
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean isInteger(String str) {
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 自定义位数产生随机数字
	 * 
	 * @param int
	 * @return String
	 */
	public static String random(int count) {
		char start = '0';
		char end = '9';
		Random rnd = new Random();
		char[] result = new char[count];
		int len = end - start + 1;
		while (count-- > 0) {
			result[count] = (char) (rnd.nextInt(len) + start);
		}
		return new String(result);
	}
	/**
	 * 获取本机ip
	 * 
	 * @return String
	 * @throws UnknownHostException
	 */
	public static String getLocalHostIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	/**
	 * base64 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String base64En(String str) {
		Base64 base64 = new Base64();
		byte[] encode = base64.encode(str.getBytes());
		return new String(encode);
	}
	/**
	 * 将数组转换成以逗号分隔的字符串
	 * 
	 * @param needChange
	 *            需要转换的数组
	 * @return 以逗号分割的字符串
	 */
	public static String arrayToStrWithComma(String[] needChange) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < needChange.length; i++) {
			sb.append(needChange[i]);
			if ((i + 1) != needChange.length) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * List分割
	 * 
	 * @param source
	 *            源List
	 * @param subSize
	 *            子list的长度
	 * @return
	 */
	public static <E> List<List<E>> splitList(List<E> source, int subSize) {
		List<List<E>> result = new ArrayList<List<E>>();
		if (PoolUtils.isEmpty(source)) {
			return result;
		}
		int outListSize = source.size() / subSize;
		if (source.size() % subSize != 0) {
			outListSize = outListSize + 1;
		}
		for (int i = 0; i < outListSize; i++) {
			List<E> resultItem = new ArrayList<E>();
			if (i != outListSize - 1) {
				resultItem = source.subList(i * subSize, ((i + 1) * subSize));
			} else {
				resultItem = source.subList(i * subSize, source.size());
			}
			result.add(resultItem);
		}
		return result;
	}

}
