package com.jiuqi.budget.common.utils;

import com.jiuqi.budget.common.consts.CommonConst;
import com.jiuqi.budget.common.domain.SimpleCell;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 预算公共方法类
 *
 * @author wangxing <br> 2019/7/2 16:42
 **/
public class CommonUtil {

    /**
     * 将1234转换为ABCD
     *
     * @param num 数值
     * @return
     */
    public static String changeNum2Letter(int num) {
        String s = "";
        while (num > 0) {
            //26个字母
            int m = num % 26;
            if (m == 0) {
                m = 26;
            }
            //65是A开始的位置
            s = (char) (m + 64) + s;
            num = (num - m) / 26;
        }
        return s;
    }

    /**
     * 坐标转换 将Excel坐标转换为行列号坐标
     *
     * @param excelCoordinate excel坐标，如A12
     * @return
     */
    public static SimpleCell coordinateTranslation(String excelCoordinate) {
        int length = excelCoordinate.length();
        int a = 0;
        int b = 0;
        int colNum = 0;
        int rowNum = 0;
        for (int index = length - 1; index >= 0; index--) {
            char c = excelCoordinate.charAt(index);
            if (c >= '0' && c <= '9') {
                rowNum = rowNum + (int) ((c - '0') * Math.pow(10, a));
                a++;
            } else {
                colNum = colNum + (int) ((c - 'A' + 1) * Math.pow(26, b));
                b++;
            }
        }
        return new SimpleCell(rowNum, colNum);
    }

    /**
     * 序列化异常栈
     *
     * @param e
     */
    public static String serializeException(Throwable e) {
        StringBuilder log = new StringBuilder();
        log.append(e.toString()).append("\n");
        StackTraceElement[] stackElements = e.getStackTrace();
        if (stackElements != null) {
            for (StackTraceElement stackTraceElement : stackElements) {
                log.append("\tat ").append(stackTraceElement).append("\r\n");
            }
        }
        return log.toString();
    }

    /**
     * 拆分字符串成数组
     *
     * @param str
     * @param separator
     */
    public static ArrayList<String> split(String str, String separator) {
        if (StringUtils.isEmpty(str)) {
            return CommonConst.EMPTY_ARRAYLIST;
        }
        int len = separator.length();
        if (len == 1) {
            return splitStr(str, separator.charAt(0));
        } else {
            ArrayList<String> list = new ArrayList<>();
            int fromIndex = 0;
            int pos = str.indexOf(separator, fromIndex);
            if (pos > -1) {
                while (pos > -1) {
                    list.add(str.substring(fromIndex, pos));
                    fromIndex = pos + len;
                    pos = str.indexOf(separator, fromIndex);
                }
                if (fromIndex != str.length()) {
                    list.add(str.substring(fromIndex));
                }
            } else {
                list.add(str);
            }
            return list;
        }
    }

    /**
     * 字符串替换
     *
     * @param str         原始字符串
     * @param target      目标字符串
     * @param replacement 要替换的内容
     */
    public static String replace(String str, String target, String replacement) {
        if (StringUtils.isEmpty(target)) {
            return str;
        }
        int startIndex = 0;
        int pos = 0;
        int targetLength = target.length();
        StringBuilder buffer = new StringBuilder(str.length());
        while (pos > -1) {
            pos = str.indexOf(target, startIndex);
            if (pos > -1) {
                buffer.append(str, startIndex, pos).append(replacement);
                startIndex = pos + targetLength;
            } else {
                buffer.append(str.substring(startIndex));
            }
        }
        return buffer.toString();
    }

    /**
     * 字符串移除
     *
     * @param str    原始字符串
     * @param target 目标字符串
     */
    public static String remove(String str, char target) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        int startIndex = 0;
        int pos = 0;
        StringBuilder buffer = new StringBuilder(str.length());
        while (pos > -1) {
            pos = str.indexOf(target, startIndex);
            if (pos > -1) {
                buffer.append(str, startIndex, pos);
                startIndex = pos + 1;
            } else {
                buffer.append(str.substring(startIndex));
            }
        }
        return buffer.toString();
    }

    /**
     * 超级高效的字符串替换
     *
     * @param str         原始字符串
     * @param target      目标字符串
     * @param replacement 要替换的内容
     */
    public static String replace(String str, char target, char replacement) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        char[] emptyChars = new char[str.length()];
        for (int index = 0, length = str.length(); index < length; index++) {
            char c = str.charAt(index);
            if (c == target) {
                emptyChars[index] = replacement;
            } else {
                emptyChars[index] = c;
            }
        }
        return new String(emptyChars);
    }

    /**
     * 拆分字符串成数组
     *
     * @param str
     * @param separator
     */
    public static String[] splitStr(String str, String separator) {
        ArrayList<String> list = split(str, separator);
        return list.toArray(new String[0]);

    }

    public static ArrayList<String> splitStr(String str, char ch) {
        if (StringUtils.isEmpty(str)) {
            return CommonConst.EMPTY_ARRAYLIST;
        }
        ArrayList<String> result = new ArrayList<>(5);
        int start = 0;
        int index = 0;
        while (true) {
            index = str.indexOf(ch, start);
            if (index == -1) {
                if (start != str.length()) {
                    result.add(str.substring(start, str.length()));
                }
                break;
            } else {
                result.add(str.substring(start, index));
                start = index + 1;
            }
        }
        return result;
    }


    /**
     * 判断一个对象是否为空，空列表、数组也会被判定为空
     * <br>
     * <b>字符串“null”不会被判定为true</b>
     *
     * @param obj 被判断的对象
     */
    public static boolean isEmpty(Object obj) {
        if (StringUtils.isEmpty(obj)) {
            return true;
        }
        if (obj instanceof List) {
            return ((List) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

    /**
     * 标准的UUID
     * 32位16进制的数字，用分隔符分成8-4-4-4-12的格式
     */
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");

    /**
     * 判断一个字符串是否为uuid类型
     */
    public static boolean isValidUUID(String uuid) {
        Matcher matcher = UUID_PATTERN.matcher(uuid);
        return matcher.matches();
    }

    /**
     * 纯字符串判定，str1是否以str2开头
     *
     * @param str1
     * @param str2
     * @return str1.startWith(str2)
     */
    public static boolean startWith(String str1, String str2) {
        if (str2 == null && str1 == null) {
            return true;
        }
        if (str2 == null || str1 == null) {
            return false;
        }
        int length = str2.length();
        if (str1.length() >= length) {
            for (int i = 0; i < length; i++) {
                if (str1.charAt(i)!=str2.charAt(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式为key=value,key=value,...
     * 转为map
     *
     * @param str
     * @return
     */
    public static Map<String, String> stringConvertMap(String str) {
        if (!StringUtils.isEmpty(str)) {
            String[] split = CommonUtil.splitStr(str, ",");
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0, length = split.length; i < length; i++) {
                String[] splitStr = CommonUtil.splitStr(split[i], "=");
                map.put(splitStr[0], splitStr[1]);
            }
            return map;
        }
        return null;
    }
}
