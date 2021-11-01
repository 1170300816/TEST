import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
/**
 * 计算器核心逻辑。这个逻辑只能处理 1~2 个数的运算。
 */
class listen {

    private String displayText = "0";   // 要显示的文本

    private String  result = "result";
    private String  boland = "";

    boolean reset = true;

    private BigDecimal number1, number2;

    private String operator;

    private HashMap<String, Operator> operators = new HashMap<String, Operator>();

    private HashMap<String, Processor> processors = new HashMap<String, Processor>();

    listen() {
        setupOperators();
        setupProcessors();
    }

    // 为每种命令添加处理方式
    private void setupProcessors() {
        processors.put("[0-9]", new Processor() {
            public void calculate(String command) {
                numberClicked(command);
            }
        });
        processors.put("\\.", new Processor() {
            public void calculate(String command) {
                dotClicked();
            }
        });
        processors.put("=", new Processor() {
            public void calculate(String command) {
                equalsClicked();
            }
        });
        processors.put("[+\\-*/]", new Processor() {
            public void calculate(String command) {
                operatorClicked(command);
            }
        });
        processors.put("C", new Processor() {
            public void calculate(String command) {
                clearClicked();
            }
        });
        processors.put("CE", new Processor() {
            public void calculate(String command) {
                clearErrorClicked();
            }
        });
    }

    // 为每种 operator 添加处理方式
    private void setupOperators() {
        operators.put("+", new Operator() {
            public BigDecimal process(BigDecimal number1, BigDecimal number2) {
                return number1.add(number2);
            }
        });
        operators.put("-", new Operator() {
            public BigDecimal process(BigDecimal number1, BigDecimal number2) {
                return number1.subtract(number2);
            }
        });
        operators.put("*", new Operator() {
            public BigDecimal process(BigDecimal number1, BigDecimal number2) {
                return number1.multiply(number2);
            }
        });
        operators.put("/", new Operator() {
            public BigDecimal process(BigDecimal number1, BigDecimal number2) {
                return number1.divide(number2, 30, RoundingMode.HALF_UP);
            }
        });
    }

    // 根据命令处理。这里的命令实际上就是按钮文本。
    public String process(String command) {
        for (String pattern : processors.keySet()) {
            if (command.matches(pattern)) {
                processors.get(pattern).calculate(command);
                break;
            }
        }

        return displayText;
    }

    // 当按下 CE 时
    private void clearErrorClicked() {
        if (operator == null) {
            number1 = null;
        } else {
            number2 = null;
        }
        displayText = "0";
        result = "0";
        reset = true;
    }

    // 当按下 C 时，将计算器置为初始状态。
    private void clearClicked() {
        number1 = null;
        number2 = null;
        operator = null;
        displayText = "0";
        result = "0";
        reset = true;
    }

    // 当按下 = 时
    private void equalsClicked() {
        calculateResult();
        number1 = null;
        number2 = null;
        operator = null;
        reset = true;
    }

    // 计算结果
    private void calculateResult() {
        number2 = new BigDecimal(displayText);
        Operator oper = operators.get(operator);
        if (oper != null) {
            BigDecimal result = oper.process(number1, number2);
            displayText = result.toString();
        }
    }

    // 当按下 +-*/ 时（可以扩展成其他中间操作符）
    private void operatorClicked(String command) {
        if (operator != null) {
            calculateResult();
        }

        number1 = new BigDecimal(displayText);
        operator = command;

        reset = true;
    }

    // 当按下 . 时
    private void dotClicked() {
        if (displayText.indexOf(".") == -1) {
            displayText += ".";
            result  += ".";
            boland +=".";
        } else if (reset) {
            displayText = "0.";
            result = "0.";
            boland +="0.";
        }
        reset = false;
    }

    // 当按下 0-9 时
    private void numberClicked(String command) {
        if (reset) {
            displayText = command;
            result = command;
            boland += command;
        } else {
            displayText += command;
            result = command;
            boland += command;
        }
        reset = false;
    }

    // 运算符处理接口
    interface Operator {

        BigDecimal process(BigDecimal number1, BigDecimal number2);
    }

    // 按钮处理接口
    interface Processor {

        void calculate(String command);
    }

}










