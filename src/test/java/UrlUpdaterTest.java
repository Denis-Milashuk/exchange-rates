import org.Denis.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import urlConnectionClasses.UrlUpdater;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class UrlUpdaterTest {

    @Timeout(2000)
    @RepeatedTest(50)
    public void getDataByDateAndCurrencyShouldNotOverdueTimeOut() throws InterruptedException {
        new UrlUpdater().getDataByDateAndCurrency(new GregorianCalendar(2020, Calendar.MAY,22), Currency.USD);
        Thread.sleep(100);
    }

    @RepeatedTest(50)
    public void getDataByDateAndCurrencyShouldNotThrowException() throws InterruptedException {
        Assertions.assertDoesNotThrow(() ->
        {
            new UrlUpdater().getDataByDateAndCurrency(new GregorianCalendar(2020, Calendar.MAY,22), Currency.USD);
        }
        );
        Thread.sleep(100);
    }

}
