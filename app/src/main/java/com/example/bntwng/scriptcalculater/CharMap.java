package com.example.bntwng.scriptcalculater;

/**
 * Created by bntwng on 2017/03/07.
 */
import java.util.HashMap;

public class CharMap {
    public enum nonterminalSymbol{
        OPERATOR,
        NUMBER,
    }

    public static final HashMap<Integer,nonterminalSymbol> charMap;

    static{
        charMap = new HashMap<Integer,nonterminalSymbol>();

        charMap.put((int)'+', nonterminalSymbol.OPERATOR);
        charMap.put((int)'-', nonterminalSymbol.OPERATOR);
        charMap.put((int)'*', nonterminalSymbol.OPERATOR);
        charMap.put((int)'/', nonterminalSymbol.OPERATOR);
        charMap.put((int)'%', nonterminalSymbol.OPERATOR);
        charMap.put((int)'^', nonterminalSymbol.OPERATOR);//これは乗算で用いる
        charMap.put((int)'P', nonterminalSymbol.OPERATOR);
        //charMap.put((int)'p', nonterminalSymbol.OPERATOR);piの処理がめんどくさいのでこれは削除
        charMap.put((int)'C', nonterminalSymbol.OPERATOR);
        //charMap.put((int)'c', nonterminalSymbol.OPERATOR);

        charMap.put((int)'0', nonterminalSymbol.NUMBER);
        charMap.put((int)'1', nonterminalSymbol.NUMBER);
        charMap.put((int)'2', nonterminalSymbol.NUMBER);
        charMap.put((int)'3', nonterminalSymbol.NUMBER);
        charMap.put((int)'4', nonterminalSymbol.NUMBER);
        charMap.put((int)'5', nonterminalSymbol.NUMBER);
        charMap.put((int)'6', nonterminalSymbol.NUMBER);
        charMap.put((int)'7', nonterminalSymbol.NUMBER);
        charMap.put((int)'8', nonterminalSymbol.NUMBER);
        charMap.put((int)'9', nonterminalSymbol.NUMBER);
        charMap.put((int)'0', nonterminalSymbol.NUMBER);
        charMap.put((int)'.', nonterminalSymbol.NUMBER);//一応'.'も数字として扱う


    }

    private CharMap(){}//中身は作らない

    public static nonterminalSymbol symbolInspection(char c){
        nonterminalSymbol t = charMap.get((int)c);

        return t;

    }
}
