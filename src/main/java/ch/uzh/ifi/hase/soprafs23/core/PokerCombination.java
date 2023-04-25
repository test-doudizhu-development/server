package ch.uzh.ifi.hase.soprafs23.core;

import ch.uzh.ifi.hase.soprafs23.constant.CombinationType;
import ch.uzh.ifi.hase.soprafs23.model.Poker;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 牌组对象
 */
@Data
public class PokerCombination {


    /**
     * 牌型
     */
    private CombinationType combinationType;

    private List<Poker> card;

    /**
     * 出牌用户
     */
    private Integer userId;


    /**
     * 牌组类型定义
     */
    public void initType() {
        if (card.size() == 1) {
            this.combinationType = CombinationType.ONE;
        }
        else if (isTwo(card)) {
            this.combinationType = CombinationType.TWO;
        }
        else if (isThree(card)) {
            this.combinationType = CombinationType.THREE;
        }
        else if (isFour(card)) {
            this.combinationType = CombinationType.FOUR;
        }
        else if (isThreeAndOne(card)) {
            this.combinationType = CombinationType.THREEANDONE;
        }
        else if (isFourAndTwo(card)) {
            this.combinationType = CombinationType.FOURANDTWO;
        }
        else if (isContinuation(card)) {
            this.combinationType = CombinationType.CONTINUATION;
        }
        else if (isDoubleContinuation(card)) {
            this.combinationType = CombinationType.DOUBLECONTINUATION;
        }
        else if (isDoubleKing(card)) {
            this.combinationType = CombinationType.DOUBLEKING;
        }
        else if (isDoubleThree(card)) {
            this.combinationType = CombinationType.DOUBLETHREE;
        }
        else {
            throw new RuntimeException("Unknown Combination");
        }
    }

    /**
     * 比较牌型
     * @param o
     * @return true可出牌，false不可出牌
     */

    public boolean compareTo(PokerCombination o) {
        initType();
        o.initType();
        //判断牌型是否一致，如果不一致比较牌型大小
        if (!this.combinationType.equals(o.getCombinationType())) {
            return this.combinationType.getLevel() < o.getCombinationType().getLevel();
        }

        //牌型一致，比较组合大小
        switch (this.combinationType) {
            //三带一
            case THREEANDONE:
                return this.card.size() == o.getCard().size() && getIdentical(this) < getIdentical(o);
            //顺子
            case CONTINUATION:
                return this.card.size() == o.getCard().size() && Collections.max(this.card).getValue() < Collections.max(o.getCard()).getValue();
            //飞机
            case DOUBLETHREE:
                return this.card.size() == o.getCard().size() && getDoubleThreeMaxValue(this) < getDoubleThreeMaxValue(o);
            //连对
            case DOUBLECONTINUATION:
                return this.card.size() == o.getCard().size() && isDoubleContinuationMaxValue(this) < isDoubleContinuationMaxValue(o);
            //四带二
            case FOURANDTWO:
                return this.card.size() == o.getCard().size() && isFourAndTwoMaxValue(this) < isFourAndTwoMaxValue(o);
            case ONE:
            case TWO:
            case THREE:
                return card.get(0).getValue() < o.getCard().get(0).getValue();
            case FOUR:
            case DOUBLEKING:
            default:
                return false;

        }
    }

    /**
     * 三带一找到三张的牌值
     * @param pokerCombination
     * @return
     */
    private Integer getIdentical(PokerCombination pokerCombination){
        //获取该牌型的所有牌
        List<Poker> num = pokerCombination.getCard();
        //按照牌的点数进行分组，生成一个 Map<Integer, List<Poker>> 对象
        //其中键表示牌的点数，值为点数相同的牌的集合。
        Map<Integer, List<Poker>> collect = num.stream()
                .collect(Collectors.groupingBy(Poker::getValue));
        //获取这个 Map 中的所有值（即所有点数相同的牌的集合）
        Collection<List<Poker>> values = collect.values();
        //遍历。如果集合中包含三张牌，则返回这三张牌中的任意一张的点数。
        for (List<Poker> value : values) {
            if(value.size()==3){
                return value.get(0).getValue();
            }
        }
        throw new RuntimeException("不是三带一");
    }

    /**
     * wather the value of 'four and thre' is biggest
     *
     * @param pokerCombination
     */
    public Integer isFourAndTwoMaxValue(PokerCombination pokerCombination) {
        List<Poker> num = pokerCombination.getCard();
        Map<Integer, List<Poker>> collect = num.stream().collect(Collectors.groupingBy(Poker::getValue));
        List<Integer> head = Lists.newArrayList();
        for (Map.Entry<Integer, List<Poker>> integerListEntry : collect.entrySet()) {
            if (integerListEntry.getValue().size() == 4) {
                head.add(integerListEntry.getKey());
            }
        }
        return Collections.max(head);
    }

    /**
     * check whether it's double cards
     */
    private boolean isTwo(List<Poker> card) {
        if (card.size() != 2) {
            return false;
        }
        return card.get(0).getValue() == card.get(1).getValue() && card.get(0).getValue() != 14 && card.get(1).getValue() != 15;
    }

    /**
     * check whether it's double joker - a greatest bomb
     */
    private boolean isDoubleKing(List<Poker> card) {
        if (card.size() != 2) {
            return false;
        }
        List<Integer> integers = Lists.newArrayList(14, 15);
        return integers.contains(card.get(0).getValue()) && integers.contains(card.get(1).getValue());
    }

    /**
     * 是否炸弹 determine weather it is bomb
     */
    private boolean isFour(List<Poker> card) {
        if (card.size() != 4) {
            return false;
        }
        Integer lastNum = null;
        for (Poker next : card) {
            if (Objects.isNull(lastNum)) {
                lastNum = next.getValue();
            }
            else {
                if (lastNum != next.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否三代一
     */
    private boolean  isThreeAndOne( List<Poker> card){
        if(card.size()>5){
            return false;
        }
        Map<Integer, List<Poker>> collect = card.stream()
                .collect(Collectors.groupingBy(Poker::getValue));
        if(collect.size()!=2){
            return false;
        }
        for (List<Poker> value : collect.values()) {
            if(value.size()==3){
                return true;
            }
        }
        return  false;
    }

    /**
     * 是否三不带
     */
    private boolean  isThree( List<Poker> card){
        if(card.size()!=3){
            return false;
        }
        Integer lastNum = null;
        for (Poker next : card) {
            if(Objects.isNull(lastNum)){
                lastNum = next.getValue();
            }else {
                if(lastNum != next.getValue()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * whether the combination type is 'Chain'-是否是顺子
     */
    private boolean isContinuation(List<Poker> card) {
        if (card.size() < 5) {
            return false;
        }
        Collections.sort(card);
        Integer tempNumber = null;
        for (Poker next : card) {
            if (Objects.nonNull(tempNumber)) {
                if (tempNumber + 1 != next.getValue()) {
                    return false;
                }
            }
            tempNumber = next.getValue();
        }
        //13 为牌2
        return tempNumber < 13;
    }

    /**
     * whether it is 'four and THREE'是否是四带SAN
     *
     * @param card
     * @return
     */
    private boolean isFourAndTwo(List<Poker> card) {
        if (card.size() < 6) {
            return false;
        }
        //通过数值分组
        Map<Integer, List<Poker>> cardMap = card.stream().collect(Collectors.groupingBy(Poker::getValue));

        //带牌
        List<Integer> tail = Lists.newArrayList();
        //四张的牌
        List<Integer> head = Lists.newArrayList();
        for (Map.Entry<Integer, List<Poker>> c : cardMap.entrySet()) {
            if (c.getValue().size() == 4) {
                head.add(c.getKey());
            }
            else {
                tail.add(c.getKey());
            }
        }
        if (head.size() == 0) {
            return false;
        }

        //判断情况一 四带二            判断情况二  四带二对
        if (card.size() != 6 && tail.size() != 2) {
            return false;
        }

        //13 为牌2
        return true;
    }

    /**
     * 是否是飞机
     * @param card
     * @return
     */
    private boolean isDoubleThree(List<Poker> card) {
        if (card.size() < 8) {
            return false;
        }
        //通过数值分组
        Map<Integer, List<Poker>> cardMap = card.stream().collect(Collectors.groupingBy(Poker::getValue));


        //带牌
        List<Integer> tail = Lists.newArrayList();
        //三张的牌
        List<Integer> head = Lists.newArrayList();
        for (Map.Entry<Integer, List<Poker>> c : cardMap.entrySet()) {
            if (c.getValue().size() == 3) {
                head.add(c.getKey());
            }
            else {
                tail.add(c.getKey());
            }
        }
        if (head.size() < 2) {
            return false;
        }
        if ((card.size() - head.size() * 3) != head.size() && tail.size() != head.size()) {
            return false;
        }
        Collections.sort(head);
        Integer tempNumber = null;
        for (Integer next : head) {
            if (Objects.nonNull(tempNumber)) {
                if (tempNumber + 1 != next) {
                    return false;
                }
            }
            tempNumber = next;
        }
        return tempNumber < 13;
    }

    /**
     * 飞机最大值
     * @param pokerCombination
     * @return
     */
    private Integer getDoubleThreeMaxValue(PokerCombination pokerCombination){
        List<Poker> num = pokerCombination.getCard();
        Map<Integer, List<Poker>> collect = num.stream().collect(Collectors.groupingBy(Poker::getValue));
        List<Integer> head = Lists.newArrayList();
        for (Map.Entry<Integer, List<Poker>> integerListEntry: collect.entrySet()){
            if(integerListEntry.getValue().size()==3){
                head.add(integerListEntry.getKey());
            }
        }
        return Collections.max(head);
    }

    /**
     * 判断是否是连对
     * @param card
     * @return
     */
    private boolean isDoubleContinuation(List<Poker> card){
        if(card.size()<6 || card.size()%2==1){
            return false;
        }
        Map<Integer, List<Poker>> cardMap = card.stream().collect(Collectors.groupingBy(Poker::getValue));
        //判断是否都是对子
        for (Map.Entry<Integer, List<Poker>> c : cardMap.entrySet()){
            if(c.getValue().size()!=2){
                return false;
            }
        }
        //判断是否是顺序 是否都为顺
        List<Integer> key = cardMap.keySet().stream().collect(Collectors.toList());
        Collections.sort(key);
        Integer tempNumber = null;
        for (Integer next : key){
            if(Objects.nonNull(tempNumber)){
                if(tempNumber+1 != next){
                    return false;
                }
            }
            tempNumber = next;
        }
        //13为纸牌2
        return tempNumber<13;
    }

    /**
     * 连对最大值
     * @param pokerCombination
     * @return
     */
    public Integer isDoubleContinuationMaxValue(PokerCombination pokerCombination){
        List<Poker> num = pokerCombination.getCard();
        Map<Integer, List<Poker>> collect = num.stream().collect(Collectors.groupingBy(Poker::getValue));
        return Collections.max(Lists.newArrayList(collect.keySet().iterator()));
    }
}
