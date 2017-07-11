# hanalmot
Korean Tokenizer based on n-gram

## How to use

### with Scala

Code:
``` scala
object HanalmotExample {  
  def main(args: Array[String]): Unit = {
    val text = "오버워치는 블리자드에서 만든 게임이다."
    Hanalmot.tokenize(text).foreach(print)
    println()
    print(Hanalmot.tokenize(text).groupBy(_.pos).mapValues(_.length))
  }
}
```

Result:
```
(오버워치,NNP)(는,JX)( ,ZSP)(블리자드,NNP)(에서,JKB)( ,ZSP)(만들,VV)(ᆫ,ETM)( ,ZSP)(게임,NNG)(이,VCP)(다,EF)(.,SF)
Map(ETM -> 1, JKB -> 1, JX -> 1, NNP -> 2, VV -> 1, VCP -> 1, ZSP -> 3, EF -> 1, SF -> 1, NNG -> 1)
```

### with Java

Code:
``` java
public class HanalmotExample {
  public static void main(String[] args) {
    String text = "왕차오 중국 외교부 부부장은 리커창 총리가 31일부터 다음달 2일까지 독일과 벨기에를 방문한다고 밝혔다.";
    HanalmotTokenJava[] tokens = HanalmotJava.tokenize(text);        
    for (HanalmotTokenJava token: tokens) {      
      System.out.print(token);    
    } 
  }
}
```

Result:
```
(왕차오,NNP)( ,ZSP)(중국,NNP)( ,ZSP)(외교부,NNG)( ,ZSP)(부부장,NNG)(은,JX)( ,ZSP)(리커창,NNP)( ,ZSP)(총리,NNG)(가,JKS)( ,ZSP)(31,SN)(일,NNB)(부터,JX)( ,ZSP)(다음달,NNG)( ,ZSP)(2,SN)(일,NNB)(까지,JX)( ,ZSP)(독일,NNP)(과,JC)( ,ZSP)(벨기에,NNP)(를,JKO)( ,ZSP)(방문,NNG)(하,XSV)(ᆫ다고,EC)( ,ZSP)(밝히,VV)(었,EP)(다,EF)(.,SF)
```

## Reference
- 심광섭. (2013). 품사 태깅 말뭉치에서 추출한 n-gram을 이용한 음절 단위의 한국어 형태소 분석. 정보과학회논문지 : 소프트웨어 및 응용, 40(12), 869-876.
- 자연 언어 처리, 김영택, 생능출판사, 2001
