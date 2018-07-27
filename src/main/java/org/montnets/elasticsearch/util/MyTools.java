package org.montnets.elasticsearch.util;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
/**
 * 
 * @Title: MyTools
 * @Description:常用工具类
 * @Version:1.0.1
 * @author pancm
 * @date 2017年9月26日
 */
public final class MyTools {
	/** 时间格式包含毫秒 */
	private static final String sdfm = "yyyy-MM-dd HH:mm:ss SSS";
	/** 普通的时间格式 */
	private static final String sdf = "yyyy-MM-dd HH:mm:ss";
	/** 时间戳格式 */
	private static final String sd = "yyyyMMddHHmmss";
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
	 * long类型的时间转换成 yyyyMMddHHmmss String类型的时间
	 * 
	 * @param lo
	 *            long类型的时间
	 * @return
	 */
	public static String longTime2StringTime(long lo) {
		return longTime2StringTime(lo, sd);
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
	 * 获取设置的时间
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getSetTime(int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.HOUR_OF_DAY, hour); // 控制时
		calendar.set(calendar.MINUTE, minute); // 控制分
		calendar.set(calendar.SECOND, second); // 控制秒
		return calendar.getTime();
	}

	/**
	 * String类型的时间转换成 long
	 * 
	 * @param lo
	 * @return String
	 * @throws ParseException
	 */
	public static long stringTime2LongTime(String time, String format) throws ParseException {
		if (isEmpty(format)) {
			format = sdf;
		}
		if (isEmpty(time)) {
			time = getNowTime(format);
		}
		SimpleDateFormat sd = new SimpleDateFormat(format);
		Date date = sd.parse(time);
		return date.getTime();
	}

	/**
	 * 时间补全 例如将2018-04-04补全为2018-04-04 00:00:00.000
	 * 
	 * @param time
	 *            补全的时间
	 * @return
	 */
	public static String complementTime(String time) {
		return complementTime(time, sdfm, 1);

	}

	/**
	 * 时间补全 例如将2018-04-04补全为2018-04-04 00:00:00.000
	 * 
	 * @param time
	 *            补全的时间
	 * @param format
	 *            补全的格式
	 * @param type
	 *            类型 1:起始;2:终止
	 * @return
	 */
	public static String complementTime(String time, String format, int type) {
		if (isEmpty(time) || isEmpty(format)) {
			return null;
		}
		int tlen = time.length();
		int flen = format.length();
		int clen = flen - tlen;
		if (clen <= 0) {
			return time;
		}
		StringBuffer sb = new StringBuffer(time);
		if (clen == 4) {
			if (type == 1) {
				sb.append(".000");
			} else {
				sb.append(".999");
			}
		} else if (clen == 9) {
			if (type == 1) {
				sb.append(" 00:00:00");
			} else {
				sb.append(" 23:59:59");
			}
		} else if (clen == 13) {
			if (type == 1) {
				sb.append(" 00:00:00.000");
			} else {
				sb.append(" 23:59:59.999");
			}
		}
		return sb.toString();

	}

	/**
	 * 获取当前String类型的的时间 使用默认格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getNowTime() {
		return getNowTime(sdf);
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
	 * @return
	 */
	public static String getMinusDays(int day) {
		return getMinusDays(day, sdf);
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
	 * 两个日期带时间比较 第二个时间大于第一个则为true，否则为false
	 * 
	 * @param String
	 * @return boolean
	 * @throws ParseException
	 */
	public static boolean isCompareDay(String time1, String time2, String format) throws ParseException {
		if (isEmpty(format)) {// 如果没有设置格式使用默认格式
			format = sdf;
		}
		SimpleDateFormat s1 = new SimpleDateFormat(format);
		Date t1 = s1.parse(time1);
		Date t2 = s1.parse(time2);
		return t2.after(t1);// 当 t2 大于 t1 时，为 true，否则为 false
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
	 * 获取自定义长度的随机数(含字母)
	 * 
	 * @param len
	 *            长度
	 * @return String
	 */
	public static String random2(int len) {
		int random = Integer.parseInt(random(5));
		Random rd = new Random(random);
		final int maxNum = 62;
		StringBuffer sb = new StringBuffer();
		int rdGet;// 取得随机数
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
				'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9' };
		int count = 0;
		while (count < len) {
			rdGet = Math.abs(rd.nextInt(maxNum));// 生成的数最大为62-1
			if (rdGet >= 0 && rdGet < str.length) {
				sb.append(str[rdGet]);
				count++;
			}
		}
		return sb.toString();
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
	 * MD5加密
	 * 
	 * @param message
	 * @return
	 */
	public static String md5Encode(String message) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(message.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		int length = 32 - md5code.length();
		for (int i = 0; i < length; i++) {
			md5code = "0" + md5code;
		}
		return md5code;
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
	 * base64解密
	 * 
	 * @param encodeStr
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String base64De(String encodeStr) {
		Base64 base64 = new Base64();
		byte[] decodeStr = base64.decodeBase64(encodeStr);
		return new String(decodeStr);
	}

	/**
	 * 输入流转字符串
	 */
	public static String inputstr2Str(InputStream in, String encode) {
		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			// 默认以utf-8形式
			if (encode == null || encode.equals(""))
				encode = "utf-8";
			while ((len = in.read(b)) != -1) {
				sb.append(new String(b, 0, len, encode));
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			return "";
		}
		return "";
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
		if (MyTools.isEmpty(source)) {
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
