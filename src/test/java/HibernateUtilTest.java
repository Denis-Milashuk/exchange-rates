import hibernateClasses.HibernateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HibernateUtilTest {

    @Test
    public  void addCourseShouldDoesNotThrowExceptionAndReturnNull(){
        Assertions.assertDoesNotThrow(HibernateUtil::getSessionFactory);
        Assertions.assertNotNull(HibernateUtil.getSessionFactory());
    }
}
