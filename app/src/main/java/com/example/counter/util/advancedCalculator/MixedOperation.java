package com.example.counter.util.advancedCalculator; /**
 * ���������
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MixedOperation { //���������
    private final int accuracy; //�������õ����㾫��
    // private final Pattern p = Pattern.compile("(^-|(?<=\\D)-)?[\\d.]+|[+\\-*\\/%!()��^��e]|log|ln|a?sin|a?cos|a?tan"); //����ƥ��������Ͳ�����������ʽ
    private final Pattern p = Pattern.compile("(^-|(?<=\\D)-)?[\\d.]+|[+\\-*\\/%!()��^]|log"); //����ƥ��������Ͳ�����������ʽ
    private final HashMap<String, Integer> prio = new HashMap<String, Integer>(); //����������Ӧ�����ȼ������ֵ��ж���������ȼ�
    private ArrayList<String> eq1 = new ArrayList<String>(); //������ʽ���б�
    private ArrayList<String> eq2 = new ArrayList<String>(); //������ʽ���м��б�
    private Stack<String> stk = new Stack<String>(); //ʹ�øö�ջ����׺���ʽתΪ��׺���ʽ�������Stack���Ϳ��Ըĳ�ʹ�ó�BigDecimal
    // public BigDecimalMath bdm = new BigDecimalMath(34); //��ȷ��С�����34λ
    public BigDecimalMath bdm;

    public MixedOperation(int accuracy) { //���캯������ʼ����������ȼ�������
        this.accuracy = accuracy;
        bdm = new BigDecimalMath(accuracy); //����BigDecimal�Ĺ��캯���������㾫��

        prio.put("(", 0); //���������ȼ���ͣ���������������ֱ����ջ
        prio.put(")", 0); //�����õ������ŵ����ȼ�����Ϊ�����Ų�������ջ���ڴ�ֻ�������ŵ���һ�������

        //˫Ŀ�����
        prio.put("+", 1); //�ӷ����ȼ�Ϊ1
        prio.put("-", 1); //�������ȼ�Ϊ1
        prio.put("*", 2); //�˷����ȼ�Ϊ2
        prio.put("/", 2); //�������ȼ�Ϊ2
        prio.put("%", 2); //ȡ�����ȼ�Ϊ2
        prio.put("^", 3); //n�η����ȼ�Ϊ3
        prio.put("��", 4); //���������ȼ�Ϊ4
        prio.put("log", 4); //log���ȼ�Ϊ4
    }

    //Ԥ���������������ȡ��������Ϊ����һ���ַ���
    //��������"1.23+4.56"������Ϊһ���ַ�������������󱻷ָ�������ַ���"1.23","+","4.56"
    private void pretreatment(String s) {
        Matcher m = p.matcher(s); //���������ʽs��������ʽ����ƥ��

        while (m.find()) {
            eq1.add(m.group()); //������ʽ���������λ��
        }
    }

    //��׺���ʽת��׺���ʽ
    //��������Ϊ1+5*7��תΪ��׺���ʽΪ157*+
    private void Infix2Postfix() {
        for (int i = 0; i < eq1.size(); i++) {
            if (prio.get(eq1.get(i)) != null) { //����������д���
                String c = eq1.get(i);
                if (stk.empty() || c.equals("(")) { //���ջΪ�ջ����������ţ�ֱ����ջ��Ϊʲô������ҲҪֱ����ջ�����ǵ�����Ƕ�����⣬��6+((1+2)*3+4)*5
                    stk.push(c);
                } else if (c.equals(")")) { //���������ţ���ջ��Ԫ��һֱ��ջֱ������������Ϊֹ�������������ų�ջ
                    while (!stk.peek().equals("(")) //ջ��Ԫ�ز�Ϊ�����ţ������������ջ����ӵ��б���
                        eq2.add(stk.pop());
                    stk.pop(); //�������ų�ջ
                } else if (prio.get(c) > prio.get(stk.peek())) {//�������������ȼ�����ջ����������ȼ�,ֱ����ջ
                    stk.push(c);
                } else { //����������ȼ����ڻ����ջ����������ȼ������������
                    while (!stk.empty() && !stk.peek().equals("(") && prio.get(stk.peek()) >= prio.get(c)) {
                        eq2.add(stk.pop());
                    }
                    stk.push(c);
                }
            } else { //�Բ������д�������ֱ����ӽ��б�
                eq2.add(eq1.get(i));
            }
        }
        while (!stk.empty()) {
            eq2.add(stk.pop());
        }
    }

    //�Ժ�׺���ʽ����
    private String postfixCalculate() {
        BigDecimal p1, p2;
        BigDecimal p3 = new BigDecimal("0");

        for (int i = 0; i < eq2.size(); i++) {
            if (prio.get(eq2.get(i)) == null) { //�Բ������д���
                stk.push(eq2.get(i)); //����ֱ����ջ
            } else {
                String c = eq2.get(i);

                    p1 = new BigDecimal(stk.pop());
                    p2 = new BigDecimal(stk.pop());

                    switch (c) {
                        case "+":
                            p3 = p2.add(p1);
                            break;

                        case "-":
                            p3 = p2.subtract(p1);
                            break;

                        case "*":
                            p3 = p2.multiply(p1);
                            break;

                        case "/":
                            p3 = p2.divide(p1, 32, BigDecimal.ROUND_HALF_EVEN);
                            break;

                        case "%":
                            p3 = p2.remainder(p1);
                            break;

                        case "^":
                            p3 = bdm.pow(p2, p1);
                            break;

                        case "��":
                            p3 = bdm.pow(p1, BigDecimal.ONE.divide(p2, bdm.getAccuracy(), BigDecimal.ROUND_HALF_EVEN));
                            break;

                        case "log":
                            p3 = bdm.log(p1, p2);
                            break;

                        default:
                            p3 = BigDecimal.ZERO;
                            break;
                    }
                    stk.push(p3.toString());
                }
            }

        return stk.pop();
    }

    //���øú���������
    public BigDecimal getMixedOperationRes(String s) {
        //����ǰ����һЩ��ʼ��������������õ��б�Ͷ�ջ
        eq1.clear();
        eq2.clear();
        while (!stk.empty()) {
            stk.pop();
        }

        pretreatment(s);
        Infix2Postfix();
        return new BigDecimal(postfixCalculate());
    }

}
