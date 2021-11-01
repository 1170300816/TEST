import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class boland {



    static enum Action {
        PUSH, /* 将算符入栈，移动字符指针 */
        POP_AND_CALC, /* 算符出栈并计算，字符指针不移动 */
        ERR, /* 不合理的情况 */
        POP_AND_NEXT /* 算符出栈，移动字符指针(主要是为了`)`算符) */
    }


    static class ResultAndIndex {
        double result = 0.0;
        int index = 0;
    }


    private static Action[][] actionTable = new Action[][]
            {
                    {
                            Action.POP_AND_CALC, Action.POP_AND_CALC, Action.POP_AND_CALC, Action.POP_AND_CALC,Action.PUSH,Action.ERR
                    },
                    {
                            Action.POP_AND_CALC, Action.POP_AND_CALC, Action.POP_AND_CALC, Action.POP_AND_CALC,Action.PUSH,Action.ERR
                    },
                    {
                            Action.PUSH, Action.PUSH, Action.POP_AND_CALC, Action.POP_AND_CALC, Action.PUSH, Action.ERR
                    },
                    {
                            Action.PUSH, Action.PUSH, Action.POP_AND_CALC, Action.POP_AND_CALC, Action.PUSH, Action.ERR
                    },
                    {
                            Action.PUSH, Action.PUSH, Action.PUSH, Action.PUSH,Action.PUSH,Action.ERR
                    },
                    {
                            Action.POP_AND_CALC,Action.POP_AND_CALC,Action.POP_AND_CALC,Action.POP_AND_CALC,Action.POP_AND_NEXT,Action.ERR
                    }
            };


    private static HashMap<Character, Integer> operatorIndexMap = new HashMap<>();
    private static Deque<Short> iStack = new ArrayDeque<>();
    private static Deque<Short> fStack = new ArrayDeque<>();
    private static Deque<Character> operatorStack = new ArrayDeque<>();
    private static Deque<Double> dataStack = new ArrayDeque<>();

    /* 编码运算符 */
    static
    {
        operatorIndexMap.put('+', 0);
        operatorIndexMap.put('-', 1);
        operatorIndexMap.put('*', 2);
        operatorIndexMap.put('/', 3);
        operatorIndexMap.put('(', 4);
        operatorIndexMap.put(')', 5);
    }


    /* 从字符串中解析出一个实数 */
    private static ResultAndIndex nextRealNumber(String str, int startIndex) {
        ResultAndIndex res = new ResultAndIndex();
        boolean isDouble = false;
        char ch;
        int i, strLen = str.length(),index = startIndex;

        while (index < strLen) {
            ch = str.charAt(index);
            if (ch >= '0' && ch <= '9') {
                if (isDouble) {
                    fStack.addFirst((short)(ch - '0'));
                } else {
                    iStack.addFirst((short)(ch - '0'));
                }
            }else if (ch == '.') {
                isDouble = true;
            } else {
                break;
            }
            index++;
        }

        i = 0;
        while (!iStack.isEmpty()) {
            res.result += iStack.pollFirst() * Math.pow(10, i++);
        }
        i = 0;
        while (!fStack.isEmpty()) {
            res.result += fStack.pollFirst() * Math.pow(10, --i);
        }

        res.index = index;
        return res;
    }


    /* 字符是否是数字 */
    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /* 字符是否是我们支持的运算符 */
    private static boolean isSupportOperator(char ch) {
        return  ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(' || ch == ')';
    }


    /* 根据指定的算符，进行相应的运算 */
    private static double calc(char opt) {
        double res = 0;
        double var1,var2;
        if (opt == '+') {
            var1 = dataStack.pollFirst();
            var2 = dataStack.pollFirst();
            res = var1 + var2;
        } else if (opt == '-') {
            var1 = dataStack.pollFirst();
            var2 = dataStack.pollFirst();
            res = var2 - var1;
        } else if (opt == '*') {
            var1 = dataStack.pollFirst();
            var2 = dataStack.pollFirst();
            res = var1 * var2;
        } else if (opt == '/') {
            var1 = dataStack.pollFirst();
            var2 = dataStack.pollFirst();
            res = var2 / var1;
        }
        return res;

    }

    /* 中缀表达式求解 */
    double mecalc(String content) {
        content = content + ")";

        int i = 0, len = content.length();
        char ch;
        operatorStack.addFirst('(');
        /* 循环迭代整个中缀表达式 */
        while (i < len) {
            ch = content.charAt(i);
            if (isDigit(ch)) {
                ResultAndIndex resultAndIndex = nextRealNumber(content, i);
                // System.out.println("parse => " + resultAndIndex.result);
                dataStack.addFirst(resultAndIndex.result);
                i = resultAndIndex.index;
            } else if (isSupportOperator(ch)) {

                int waiting = operatorIndexMap.get(ch);
                int top = operatorIndexMap.get(operatorStack.peekFirst());
                // System.out.println(actionTable[waiting][top]);


                switch (actionTable[waiting][top]) {
                    case PUSH :
                        operatorStack.addFirst(ch);
                        i++;
                        break;
                    case POP_AND_CALC:
                        char opt = operatorStack.pollFirst();
                        double v = calc(opt);
                        dataStack.addFirst(v);
                        break;
                    case POP_AND_NEXT:
                        operatorStack.pollFirst();
                        i++;
                        break;
                    case ERR:
                        throw new RuntimeException();

                }
            } else {
                i++;
            }
        }
        return dataStack.pollFirst();
    }
}

