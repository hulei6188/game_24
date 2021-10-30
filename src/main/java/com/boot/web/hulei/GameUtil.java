package com.boot.web.hulei;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameUtil {
    public static String process(String s) {
        return "pocket_" + s.charAt(s.length() - 1);
    }

    /**
     * 检查表达式是否合法，合法返回true
     */
    public static boolean checkExpression(String expression) {
        //去除空格
        expression = expression.replaceAll(" ", "");

        Set<Character> operate_set = new HashSet<>();
        operate_set.add('-');
        operate_set.add('+');
        operate_set.add('*');
        operate_set.add('/');

        //拆分字符串
        char[] arr = expression.toCharArray();
        int len = arr.length;

        //前后括号计数，用来判断括号是否合法
        int checkNum = 0;

        //数字集合
        List<Character> digit_list = new ArrayList<>();
        //循环判断
        for (int i = 0; i < len; i++) {
            if (Character.isDigit(arr[i]) || arr[i] == '.') { //数字和小数点判断
                //把数字和小数点加入到集合中，为了下一步判断数字是否合法
                digit_list.add(arr[i]);
            } else { //非数字和小数点
                //如果集合中有值，取出来判断这个数字整体是否合法
                if (digit_list.size() > 0) {
                    //判断字符串是否合法
                    boolean result = getNumberStringFromList(digit_list);
                    if (result) {
                        //如果合法，清空集合，为了判断下一个
                        digit_list.clear();
                    } else {
                        //不合法，返回false
                        return false;
                    }
                }

                if (arr[i] == '+' || arr[i] == '*' || arr[i] == '/') {
                    //判断规则(1.不能位于首位 2.不能位于末尾 3.后面不能有其他运算符 4.后面不能有后括号)
                    if (i == 0 || i == (len - 1) || operate_set.contains(arr[i + 1]) || arr[i + 1] == ')') {
                        System.out.println("error type : '+' or '*' or '/' ->" + arr[i]);
                        return false;
                    }
                } else if (arr[i] == '-') {
                    //减号判断规则(1.不能位于末尾 2.后面不能有其他运算符 3.后面不能有后括号)
                    if (i == (len - 1) || operate_set.contains(arr[i + 1]) || arr[i + 1] == ')') {
                        System.out.println("error type : '-' ->" + arr[i]);
                        return false;
                    }

                } else if (arr[i] == '(') {
                    checkNum++;
                    //判断规则(1.不能位于末尾 2.后面不能有+，*，/运算符和后括号 3.前面不能为数字)
                    if (i == (len - 1) || arr[i + 1] == '+' || arr[i + 1] == '*' || arr[i + 1] == '/' || arr[i + 1] == ')' || (i != 0 && Character.isDigit(arr[i - 1]))) {
                        System.out.println("error type : '(' ->" + arr[i]);
                        return false;
                    }
                } else if (arr[i] == ')') {
                    checkNum--;
                    //判定规则(1.不能位于首位 2.后面不能是前括号 3.括号计数不能小于0，小于0说明前面少了前括号)
                    if (i == 0 || (i < (len - 1) && arr[i + 1] == '(') || checkNum < 0) {
                        System.out.println("error type : ')' ->" + arr[i]);
                        return false;
                    }
                } else {
                    //非数字和运算符
                    System.out.println("error type : not number and operator ->" + arr[i]);
                    return false;
                }
            }
        }

        //如果集合中有值，取出来判断这个数字整体是否合法
        if (digit_list.size() > 0) {
            //判断字符串是否合法
            boolean result = getNumberStringFromList(digit_list);
            if (result) {
                //如果合法，清空集合，为了判断下一个
                digit_list.clear();
            } else {
                //不合法，返回false
                return false;
            }
        }

        //不为0,说明括号不对等，可能多前括号
        return checkNum == 0;
    }

    /**
     * 把集合中的字符，拼接成字符串，并校验字符串是否是数值
     */
    public static boolean getNumberStringFromList(List<Character> list) {
        //如果集合中有值，取出来判断这个数字整体是否合法
        if (list != null) {
            StringBuilder stringBuffer = new StringBuilder();
            for (Character character : list) {
                stringBuffer.append(character);
            }
            //正则判断数字是否合法
            boolean result = isNumber(stringBuffer.toString());

            if (!result) {
                System.out.println("error type: digit ->" + stringBuffer.toString());
            }
            return result;
        }
        return false;
    }

    /**
     * 判断字符串是否为整数，浮点数类型，是返回true * @param str * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^-?([1-9]\\d*\\.\\d+|0\\.\\d*[1-9]\\d*|[1-9]\\d*|0)$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean useNums(String input, int[] nums) {
        int[] map = new int[13];
        for (int num : nums) {
            map[num - 1]++;
        }
//        System.out.println(Arrays.toString(map));
        int i = 0;
        while (i < input.length()) {
            int num = 0;
            while (i < input.length() && Character.isDigit(input.charAt(i))) {
                num = num * 10 + (input.charAt(i) - '0');
                i++;
            }
            if (num != 0) {
                if (num > 0 && num <= 13)
                    map[num - 1]--;
                i--;
            }
            i++;
        }

        for (int num :
                map) {
            if (num != 0)
                return false;
        }
        return true;
    }

    public static double calculate(String s) {
        //去空格
        s = s.replaceAll(" ", "");
        if (s.length() == 0) return 0;
        Stack<Double> stack = new Stack<>();//数据栈
        char sign = '+'; //符号
        double res = 0, pre = 0;
        int i = 0;
        while (i < s.length()) {
            char ch = s.charAt(i);
            //处理两位数的问题
            if (Character.isDigit(ch)) {
                pre = pre * 10 + (ch - '0');
            }
            //碰到左括号 就把括号里面当成一个 新的被加数
            if (ch == '(') {
                int j = findClosing(s.substring(i));
                pre = calculate(s.substring(i + 1, i + j));
                i += j;
            }
            if (i == s.length() - 1 || !Character.isDigit(ch)) {
                //将所有的结果压栈 最后统一加起来
                switch (sign) {
                    case '+':
                        stack.push(pre);
                        break;
                    case '-':
                        stack.push(-pre);
                        break;
                    case '*':
                        stack.push(stack.pop() * pre);
                        break;
                    case '/':
                        stack.push(stack.pop() / pre);
                        break;
                }
                pre = 0;
                //记录当前的符号
                sign = ch;
            }
            i++;
        }
        // 本质上说全都是加法
        while (!stack.isEmpty()) res += stack.pop();
        return res;
    }

    //删除所有的括号对，并返回右括号的位置
    private static int findClosing(String s) {
        int level = 0, i = 0;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') level++;
            else if (s.charAt(i) == ')') {
                level--;
                if (level == 0) break;
            }
        }
        return i;

    }


}
