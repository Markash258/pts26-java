package sk.uniba.fmph.dcs.power_grid;

public final class PlayerMoney {
    private static final int[] PAYOUT = {10, 22, 33, 44, 54, 64, 73, 82, 90, 98, 105,
            112, 118, 124, 129, 134, 138, 142, 145, 148, 150};
    private final PlayerColor color;
    private int currentMoney;
    private static final int STARTING_MONEY = 50;

    public PlayerMoney(final PlayerColor color) {
        this(color, STARTING_MONEY);
    }

    public PlayerMoney(final PlayerColor color, final int startingCash) {
        if (startingCash < 0) {
            throw new IllegalArgumentException("startingCash must not be negative");
        }
        this.color = color;
        this.currentMoney = startingCash;
    }

    public boolean hasEnoughMoney(final int amount) {
        return currentMoney >= amount;
    }

    public void receiveMoney(final int numberOfTowns) {
        if (numberOfTowns < 0) {
            throw new IllegalArgumentException("numberOfTowns must not be negative");
        }
        int index = Math.min(numberOfTowns, PAYOUT.length - 1);
        currentMoney += PAYOUT[index];
    }

    public int getCurrentMoney() {
        return currentMoney;
    }

    public void spendMoney(final int amount) {
        if (!hasEnoughMoney(amount)) {
            throw new IllegalStateException("not enough money");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        currentMoney -= amount;
    }

    public PlayerColor getColor() {
        return color;
    }
}
