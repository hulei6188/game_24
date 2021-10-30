package com.boot.web.hulei;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Game_24 implements Game {

    private volatile Pocket card_1;
    private volatile Pocket card_2;
    private volatile Pocket card_3;
    private volatile Pocket card_4;
    private static final int BOUND = 13;
    private static final int CORRECT_ANSWER = 24;
    final static int TARGET = 24;
    final static double EPSILON = 1e-6;
    final static int ADD = 0, MULTIPLY = 1, SUBTRACT = 2, DIVIDE = 3;
    private final GameThread thread = new GameThread();

    public void setCard_1(Pocket card_1) {
        this.card_1 = card_1;
    }

    public void setCard_2(Pocket card_2) {
        this.card_2 = card_2;
    }

    public void setCard_3(Pocket card_3) {
        this.card_3 = card_3;
    }

    public void setCard_4(Pocket card_4) {
        this.card_4 = card_4;
    }

    public Pocket getCard_1() {
        return card_1;
    }

    public Pocket getCard_2() {
        return card_2;
    }

    public Pocket getCard_3() {
        return card_3;
    }

    public Pocket getCard_4() {
        return card_4;
    }

    private final Random random = new Random();

    public Game_24() {
        setRandomCards();
    }

    @Override
    public void start() {
        thread.start();

    }

    @Override
    public void stop() {
        thread.interrupt();
    }

    private static class GameThread extends Thread {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            Game_24 game = new Game_24();
            game.setRandomCards();

            do {
                System.out.println("这4张牌是:" + game.getCards());
                System.out.print("请输入你要组合的结果：");
                String line = scanner.nextLine();
                while (game.judgeInput(line)) {
                    System.out.println("结果不正确，请重新输入:");
                    line = scanner.nextLine();
                }
                System.out.println("恭喜你输入正确，计算机得到的结果是：" + game.getResult());
                game.setRandomCards();
            } while (!Thread.currentThread().isInterrupted());
        }
    }

    private static class Fragment {
        private final String formula;
        private final double value;

        public Fragment(String formula, double result) {
            this.formula = formula;
            this.value = result;
        }

        public String getFormula() {
            return formula;
        }


        public double getValue() {
            return value;
        }

    }

    public void setRandomCards() {
        do {
            setCard_1(Pocket.values()[random.nextInt(BOUND)]);
            setCard_2(Pocket.values()[random.nextInt(BOUND)]);
            setCard_3(Pocket.values()[random.nextInt(BOUND)]);
            setCard_4(Pocket.values()[random.nextInt(BOUND)]);
        } while (getResult().size() <= 0);
    }

    public void setCards(Pocket card_1, Pocket card_2, Pocket card_3, Pocket card_4) {
        if (getResult().size() <= 0) {
            System.out.println("The four cards you give don't make 24 points!");
            return;
        }
        setCard_1(card_1);
        setCard_2(card_2);
        setCard_3(card_3);
        setCard_4(card_4);
    }

    public List<Pocket> getCards() {
        return Arrays.asList(getCard_1(), getCard_2(), getCard_3(), getCard_4());
    }

    public List<Integer> getCardsPointList() {
        return Arrays.asList(getCard_1().getPoint(), getCard_2().getPoint(), getCard_3().getPoint(), getCard_4().getPoint());
    }

    public int[] getCardsPoint() {
        return new int[]{getCard_1().getPoint(), getCard_2().getPoint(), getCard_3().getPoint(), getCard_4().getPoint()};
    }

    public List<String> getResult() {
        return judgePoint24(getCardsPoint());
    }


    public boolean judgeInput(String expression) {
        return GameUtil.calculate(expression) == CORRECT_ANSWER;
    }


    public List<String> judgePoint24(int[] nums) {
        List<String> result = new ArrayList<>();
        List<Fragment> list = new ArrayList<Fragment>();
        for (int num : nums) {
            list.add(new Fragment(Integer.toString(num), (double) num));
        }
        solve(list, result);
        return result;
    }

    public void solve(List<Fragment> list, List<String> result) {
        if (list.size() == 0) {
            return;
        }
        if (list.size() == 1 && Math.abs(list.get(0).getValue() - TARGET) < EPSILON) {
            String formula = list.get(0).getFormula();
            result.add(formula.substring(1, formula.length() - 1));
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    List<Fragment> list2 = new ArrayList<Fragment>();
                    for (int k = 0; k < size; k++) {
                        if (k != i && k != j) {
                            list2.add(list.get(k));
                        }
                    }
                    for (int k = 0; k < 4; k++) {
                        if (k < 2 && i > j) {
                            continue;
                        }
                        StringBuilder builder = new StringBuilder();
                        builder.append("(");
                        builder.append(list.get(i).getFormula());
                        if (k == ADD) {
                            builder.append("+");
                            builder.append(list.get(j).getFormula());
                            builder.append(")");
                            list2.add(new Fragment(builder.toString(), list.get(i).getValue() + list.get(j).getValue()));
                        } else if (k == MULTIPLY) {
                            builder.append("*");
                            builder.append(list.get(j).getFormula());
                            builder.append(")");
                            list2.add(new Fragment(builder.toString(), list.get(i).getValue() * list.get(j).getValue()));
                        } else if (k == SUBTRACT) {
                            builder.append("-");
                            builder.append(list.get(j).getFormula());
                            builder.append(")");
                            list2.add(new Fragment(builder.toString(), list.get(i).getValue() - list.get(j).getValue()));
                        } else {
                            if (Math.abs(list.get(j).getValue()) < EPSILON) {
                                continue;
                            } else {
                                builder.append("/");
                                builder.append(list.get(j).getFormula());
                                builder.append(")");
                                list2.add(new Fragment(builder.toString(), list.get(i).getValue() / list.get(j).getValue()));
                            }
                        }
                        solve(list2, result);
                        list2.remove(list2.size() - 1);
                    }
                }
            }
        }
    }

    public static boolean check_pocket(int point) {
        return point > 0 && point <= 13;
    }

}
