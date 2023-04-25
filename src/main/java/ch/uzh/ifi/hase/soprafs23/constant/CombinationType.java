package ch.uzh.ifi.hase.soprafs23.constant;

public enum CombinationType {

    // 1->3 2->4 3->5 4->6 5->7 6->8 7->9 8->10 9->j 10->Q 11->K 12->A 13->2 14->small joker 15->big joker

    //单 -Single Card
    ONE(1),
    //对 -Double Cards
    TWO(1),
    //三不带 - Triple Cards
    THREE(1),
    //三代一 Triple Cards and one
    THREEANDONE(1),
    //顺子 - chain
    CONTINUATION(1),
    //连队 -Consecutive Double Cards
    DOUBLECONTINUATION(1),
    //飞机 - double three
    DOUBLETHREE(1),
    //四带二 - four and two
    FOURANDTWO(1),
    //炸弹 Bomb
    FOUR(2),
    //对王 - double jokers
    DOUBLEKING(3);
    private int level;
    CombinationType(int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
