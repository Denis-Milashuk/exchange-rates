import org.Denis.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import urlConnectionClasses.UrlUpdater;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UrlUpdaterTest {

    @Timeout(2000)
    @RepeatedTest(20)
    public void getDataByDateAndCurrencyShouldNotOverdueTimeOut() throws InterruptedException, IOException {
        UrlUpdater.getInstance().getDataByDateAndCurrency(new GregorianCalendar(2020, Calendar.MAY,22), Currency.USD);
        Thread.sleep(100);
    }

    @RepeatedTest(20)
    public void getDataByDateAndCurrencyShouldNotThrowException() throws InterruptedException {
        Assertions.assertDoesNotThrow(() ->
        {
            UrlUpdater.getInstance().getDataByDateAndCurrency(new GregorianCalendar(2020, Calendar.MAY,22), Currency.USD);
        }
        );
        Thread.sleep(100);
    }

}
