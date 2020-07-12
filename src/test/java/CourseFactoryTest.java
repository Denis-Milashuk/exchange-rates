import hibernateClasses.Course;
import hibernateClasses.CourseFactory;
import org.Denis.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourseFactoryTest {
    @Test
    public void getCourseByCurrencyWillBeAbleToCreateAllCurrencyCourse(){
        for (Currency currencyType : Currency.values()){
            Course course = CourseFactory.getCourseByCurrency(currencyType);
            Assertions.assertNotNull(course);
        }
    }
}
