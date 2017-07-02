package net.ingtra.hanalmot.component;

public class HanalmotTokenJava {
    private String letter;
    private String pos;

    public HanalmotTokenJava(String letter, String pos) {
        this.letter = letter;
        this.pos = pos;
    }

    public static HanalmotTokenJava fromHanalmotToken(HanalmotToken token) {
        return new HanalmotTokenJava(token.letter(), token.pos());
    }

    public String getPos() {
        return pos;
    }

    public String getLetter() {
        return letter;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", this.letter, this.pos);
    }
}
