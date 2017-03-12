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
        COT
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
            }
        }catch (Exception e){
            throw e;
        }
    }
}
