package com.htquach.lottery;

import java.util.Arrays;
import java.util.Random;


public class MegaMillions extends LotteryTicket {
    private static final String NAME = "Mega Millions";
    private static final int COST = 1;
    private static final int JACKPOT = 15000000;
    private static final int MAIN_NUMBER_MAX = 75;
    private static final int MEGABALL_MAX = 15;
    private static final int NUMBER_COUNT = 5;
    private final int[] MAIN_NUMBERS;
    private final int MEGABALL_NUMBER;

    @Override
    public String get_name() {
        return NAME;
    }

    @Override
    public int get_cost() {
        return COST;
    }

    /**
     * Check the ticket result and return the winning amount in dollar.
     * @return amount won in dollar
     */
    public MegaMillionsWinningType checkPrize(int[][] gameResult) {
        int matchedCount = 0;
        boolean matchedMegaball = false;

        for (int i = 0; i < NUMBER_COUNT; i++) {
            for (int j = 0; j < NUMBER_COUNT; j++) {
                if (gameResult[0][i] < MAIN_NUMBERS[j]) {
                    //Next number because this number is too high
                    break;
                    //TODO:  more cases to optimized
                }
                if (gameResult[0][i] == MAIN_NUMBERS[j]) {
                    matchedCount++;
                }
            }
        }
        matchedMegaball = (gameResult[1][0] == MEGABALL_NUMBER);

        if (matchedCount == 0 && !matchedMegaball) {
            return MegaMillionsWinningType.None;
        } else if (matchedCount == 0 && matchedMegaball) {
            return MegaMillionsWinningType.Megaball;
        } else if (matchedCount == 1 && matchedMegaball) {
            return MegaMillionsWinningType.OnePlusMegaball;
        } else if (matchedCount == 2 && matchedMegaball) {
            return MegaMillionsWinningType.TwoPlusMegaball;
        } else if (matchedCount == 3 && !matchedMegaball) {
            return MegaMillionsWinningType.Three;
        } else if (matchedCount == 3 && matchedMegaball) {
            return MegaMillionsWinningType.ThreePlusMegaball;
        } else if (matchedCount == 4 && !matchedMegaball) {
            return MegaMillionsWinningType.Four;
        } else if (matchedCount == 4 && matchedMegaball) {
            return MegaMillionsWinningType.FourPlusMegaball;
        } else if (matchedCount == 5 && !matchedMegaball) {
            return MegaMillionsWinningType.Five;
        } else if (matchedCount == 5 && matchedMegaball) {
            return MegaMillionsWinningType.Jackpot;
        }
        return MegaMillionsWinningType.None;
    }

    public static int getNumberCount() {
        return NUMBER_COUNT;
    }

    public static int getMainNumberMax() {
        return MAIN_NUMBER_MAX;
    }

    public static int getMegaballMax() {
        return MEGABALL_MAX;
    }

    public int[] getMainNumbers() {
        return MAIN_NUMBERS;
    }

    public int getMegaballNumber() {
        return MEGABALL_NUMBER;
    }

    public int getJackpot() {
        return JACKPOT;
    }

    /**
     * Quick pick
     */
    public MegaMillions() {
        int[][] quickPick = quick_pick();
        MAIN_NUMBERS = quickPick[0];
        MEGABALL_NUMBER = quickPick[1][0];
    }

    public MegaMillions(int[] mainNumber, int megaballNumber) {
        MAIN_NUMBERS = mainNumber;
        MEGABALL_NUMBER = megaballNumber;
    }

    /**
     * Pick the numbers for a game
     * @return The first array is the first give number between 1 and 75.
     * The second array is a single element of Megaball between 1 and 15
     */
    private static int[][] quick_pick() {
        Random random = new Random();
        int[][] numbers = new int[2][];
        int nextNumber;
        int numberRange;
        int nextRandomNumber;

        //Making a 1 based index array and simply not using the 0 index.
        int[] numberSet = new int[MAIN_NUMBER_MAX + 1];
        for (int j = 1; j <= MAIN_NUMBER_MAX; j++) {
            numberSet[j] = j;
        }

        for (int i = 0; i < NUMBER_COUNT; i++) {
            numberRange = MAIN_NUMBER_MAX - i;
            //Offset nextInt() to return all inclusive between 1 to MAIN_NUMBER_MAX
            nextRandomNumber = random.nextInt(numberRange) + 1;
            //Swap position with the last element
            nextNumber = numberSet[nextRandomNumber];
            numberSet[nextRandomNumber] = numberSet[numberRange];
            numberSet[numberRange] = nextNumber;
        }



        //The randomly selected numbers are at the back of the array.
        numbers[0] = Arrays.copyOfRange(numberSet, MAIN_NUMBER_MAX - NUMBER_COUNT + 1, MAIN_NUMBER_MAX + 1);
        Arrays.sort(numbers[0]);
        numbers[1] = new int[1];
        numbers[1][0] = random.nextInt(MEGABALL_MAX) + 1;

        for (int j=0;j<numbers.length;j++)
            for (int k=j+1;k<numbers.length;k++)
                if (k!=j && numbers[k] == numbers[j])
                    System.out.println("FOUND DUPLICATE" + numbers);

        return numbers;
    }

    /**
     * Draw the winning number
     * @return The first array is the first give number between 1 and 75.
     * The second array is a single element of Megaball between 1 and 15
     */
    public static int[][] drawing() {
        return quick_pick();
    }

    @Override
    public String toString() {
        return "MegaMillions{" + mainNumbersToString(MAIN_NUMBERS) +
                ", " + megaballNumberToString(MEGABALL_NUMBER) + "}";
    }

    public static String toString(int[][] gameNumbers) {
        return "MegaMillions{" + mainNumbersToString(gameNumbers[0]) +
                ", " + megaballNumberToString(gameNumbers[1][0]) + "}";
    }

    public static String mainNumbersToString(int[] mainNumbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mainNumbers.length; i++) {
            sb.append(String.format("%02d", mainNumbers[i])).append(" ");
        }
        return sb.toString();
    }

    public static String megaballNumberToString(int megaballNumber) {
        return "Megaball " + String.format("%02d", megaballNumber);
    }
}
