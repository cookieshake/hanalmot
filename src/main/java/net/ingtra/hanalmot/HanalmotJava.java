package net.ingtra.hanalmot;


import net.ingtra.hanalmot.component.HanalmotToken;
import net.ingtra.hanalmot.component.HanalmotTokenJava;

import java.util.LinkedList;

public class HanalmotJava {
    public static HanalmotTokenJava[] tokenize(String text) {
        HanalmotToken[] tokenized = Hanalmot.tokenize(text);
        LinkedList<HanalmotTokenJava> tokenBuffer = new LinkedList<>();

        for (HanalmotToken token : tokenized) {
            tokenBuffer.add(HanalmotTokenJava.fromHanalmotToken(token));
        }

        return tokenBuffer.toArray(new HanalmotTokenJava[0]);
    }

    public static String[] nouns(String text) {
        return Hanalmot.nouns(text);
    }
}
