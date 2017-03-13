package com.example.bntwng.scriptcalculator.functions;

import com.example.bntwng.scriptcalculator.Node;

/**
 * Created by bntwng on 2017/03/12.
 */

public class FunctionNode extends Node {
    public enum functions3{
        //三角関数
        SIN,
        COS,
        TAN,
        CSC,
        SEC,
        COT,
        //log
        LOG,
        //abs
        ABS,
        //exp
        EXP,
    }
    protected double logCalculate(String formula) throws IllegalArgumentException{
        try {
            double d1 = calculate(formula);//とりあえず消しきれなかったときはエラーを吐いておく、後で,区切りに関して実装
            if (right >= 0) {
                double d2 = calculate(formula.substring(0,right));//カンマ後はsubstringを取り、自動でrightが減るため、ここでrightを触る必要は無い
                if(right>=0)throw new IllegalArgumentException("log関数内に文法エラー");
                return Math.log10(d1) / Math.log10(d2);
            }
            return Math.log(d1);
        }catch (Exception e){
            throw e;
        }
    }

    public FunctionNode(String formula,functions3 f) throws IllegalArgumentException{
        try {
            switch (f) {
                //三角関数
                case SIN:
                    value = Math.sin(calculate(formula));
                    break;
                case COS:
                    value = Math.cos(calculate(formula));
                    break;
                case TAN:
                    value = Math.tan(calculate(formula));
                    break;
                case CSC:
                    value = 1/Math.sin(calculate(formula));
                    break;
                case SEC:
                    value = 1/Math.cos(calculate(formula));
                    break;
                case COT:
                    value = 1/Math.tan(calculate(formula));
                    break;
                //log
                case LOG:
                    value = logCalculate(formula);
                    break;
                //abs
                case ABS:
                    value = Math.abs(calculate(formula));
                    break;
                case EXP:
                    value = Math.exp(calculate(formula));
            }
        }catch (Exception e){
            throw e;
        }
    }
}
