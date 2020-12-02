package com.jiuqi.budget.common.utils;

import com.jiuqi.budget.common.consts.CommonConst;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 数字转中文。最大支持到万亿级别。
 *
 * @author alex
 */
public class Num2Ch {
    /**
     * 单位进位，中文默认为4位即（万、亿）
     */
    private static final int UNIT_STEP = 4;

    /**
     * 单位
     */
    private static final String[] CN_UNITS = new String[]{"个", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"};

    /**
     * 汉字
     */
    private static final String[] CN_CHARS = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 数值转换为中文字符串
     *
     * @param num 需要转换的数值
     */
    public static String cvt(long num) {
        return cvt(num, true);
    }

    /**
     * 数值转换为中文字符串(口语化)
     *
     * @param num          需要转换的数值
     * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
     */
    public static String cvt(long num, boolean isColloquial) {
        ArrayList<String> convert = convert(num, isColloquial);
        StringBuilder strs = new StringBuilder(convert.size());
        for (String str : convert) {
            strs.append(str);
        }
        return strs.toString();
    }

    /**
     * 将数值转换为中文
     *
     * @param num          需要转换的数值
     * @param isColloquial 是否口语化。例如12转换为'十二'而不是'一十二'。
     */
    private static ArrayList<String> convert(long num, boolean isColloquial) {
        // 10以下直接返回对应汉字
        if (num < 10) {
            ArrayList<String> result = new ArrayList<>(1);
            // ASCII2int
            result.add(CN_CHARS[(int) num]);
            return result;
        }

        char[] chars = String.valueOf(num).toCharArray();
        // 超过单位表示范围的返回空
        if (chars.length > CN_UNITS.length) {
            return CommonConst.EMPTY_ARRAYLIST;
        }

        // 记录上次单位进位
        boolean isLastUnitStep = false;
        // 创建数组，将数字填入单位对应的位置
        ArrayList<String> cnchars = new ArrayList<>(chars.length * 2);
        // 从低位向高位循环
        for (int pos = chars.length - 1; pos >= 0; pos--) {
            char ch = chars[pos];
            // ascii2int 汉字
            String cnChar = CN_CHARS[ch - '0'];
            // 对应的单位坐标
            int unitPos = chars.length - pos - 1;
            // 单位
            String cnUnit = CN_UNITS[unitPos];
            // 是否为0
            boolean isZero = (ch == '0');
            // 是否低位为0
            boolean isZeroLow = (pos + 1 < chars.length && chars[pos + 1] == '0');
            // 当前位是否需要单位进位
            boolean isUnitStep = (unitPos >= UNIT_STEP && (unitPos % UNIT_STEP == 0));
            // 去除相邻的上一个单位进位
            if (isUnitStep && isLastUnitStep) {
                int size = cnchars.size();
                cnchars.remove(size - 1);
                // 补0
                if (!CN_CHARS[0].equals(cnchars.get(size - 2))) {
                    cnchars.add(CN_CHARS[0]);
                }
            }
            // 单位进位(万、亿)，或者非0时加上单位
            if (isUnitStep || !isZero) {
                cnchars.add(cnUnit);
                isLastUnitStep = isUnitStep;
            }
            // 当前位为0低位为0，或者当前位为0并且为单位进位时进行省略
            if (isZero && (isZeroLow || isUnitStep)) {
                continue;
            }
            cnchars.add(cnChar);
            isLastUnitStep = false;
        }

        Collections.reverse(cnchars);
        // 清除最后一位的0
        int chSize = cnchars.size();
        String chEnd = cnchars.get(chSize - 1);
        if (CN_CHARS[0].equals(chEnd) || CN_UNITS[0].equals(chEnd)) {
            cnchars.remove(chSize - 1);
        }

        // 口语化处理
        if (isColloquial) {
            String chFirst = cnchars.get(0);
            String chSecond = cnchars.get(1);
            // 是否以'一'开头，紧跟'十'
            if (chFirst.equals(CN_CHARS[1]) && chSecond.startsWith(CN_UNITS[1])) {
                cnchars.remove(0);
            }
        }
        return cnchars;
    }

//    public static void main(String[] args) {
//        String[] testData = ("0,1,2,3,4,5,6,7,8,9,"
//                + "10,11,20,91,"
//                + "110,102,1234,1023,1002,"
//                + "10000,12345,10234,10023,10002,12034,12003,12000,12300,12340,"
//                + "100000,123456,102345,200234,100023,100002,120345,120034,120003,120000,123000,123400,123450,"
//                + "1000000,1234567,2023456,1002345,3000234,1000023,1000002,1203456,1200345,1200034,1200003,1200000,1230000,1234000,1234500,1234560,"
//                + "10000000,12345678,10234567,10023456,10002345,10000234,10000023,12034567,12003456,12000345,12000034,12000003,12000000,12300000,12340000,12345600,12345670,"
//                + "1000000000,1234567890,1023456789,1002300078,1000230067,1000023456,1000002345,1203456789,1200345678,1200034567,1200003456,1200000345,1200000034,1200000003,1230000000,1234000000,1234500000,1234560000,1234567000,1234567800,1234567890,"
//
//                + String.valueOf(Integer.MAX_VALUE)
//
//                + ",9223372036854,9002233007200").split(",");
//        String[] testData = {"53"};
//        for (String data : testData) {
//            long num = Long.parseLong(data);
//
//            if (num < 0) {
//                continue;
//            }
//
//            System.out.println(String.format("%-12s \t %s", data,
//                    Num2Ch.cvt(num, true)));
//
//        }
//
//    }
}

