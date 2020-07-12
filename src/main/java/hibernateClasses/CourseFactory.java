package hibernateClasses;
import org.Denis.Currency;

public class CourseFactory {
    public static Course getCourseByCurrency(Currency currency){
        Course course = null;
        switch (currency){
            case USD:
                course = new CourseUSD ();
                break;
            case EUR:
                course = new CourseEUR ();
                break;
            case RUB:
                course = new CourseRUB ();
                break;
            case CLP:
                course = new CourseCLP ();
                break;
        }
        return course;
    }
}
