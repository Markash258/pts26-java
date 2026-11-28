package sk.uniba.fmph.dcs.power_grid;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PlayerMoneyTest {
    @Test
    public void startsWithFiftyByDefault() {
        final PlayerMoney money = new PlayerMoney(PlayerColor.RED);
        assertEquals(50, money.getCurrentMoney());
    }
    @Test
    public void receivesPayoutForPoweredTowns() {
        final PlayerMoney money = new PlayerMoney(PlayerColor.RED, 0);
        money.receiveMoney(3);
        assertEquals(44, money.getCurrentMoney());
    }
    @Test
    public void paysMaximumAboveTwentyTowns() {
        final PlayerMoney money = new PlayerMoney(PlayerColor.RED, 0);
        money.receiveMoney(25);
        assertEquals(150, money.getCurrentMoney());
    }
    @Test
    public void spendsMoneyItHas() {
        final PlayerMoney money = new PlayerMoney(PlayerColor.RED, 50);
        money.spendMoney(20);
        assertEquals(30, money.getCurrentMoney());
    }
    @Test
    public void refusesToSpendMoreThanItHas() {
        final PlayerMoney money = new PlayerMoney(PlayerColor.RED, 10);
        assertThrows(IllegalStateException.class, () -> money.spendMoney(20));
    }

}
