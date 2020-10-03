import hibernateClasses.Course;
import hibernateClasses.CourseUSD;
import hibernateClasses.HibernateHelper;
import org.Denis.Currency;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HibernateHelperTest {
    private static final Course TEST_COURSE = new CourseUSD(2.5,5,1,new GregorianCalendar(2020, Calendar.MAY,22),new GregorianCalendar());
    private static HibernateHelper hibernateHelper;
    private SessionFactory sessionFactory;

    @BeforeAll
    public static void createNewHibernateHelper(){
        hibernateHelper = HibernateHelper.getInstance();
    }

    @BeforeEach
    public void getSessionFactory(){
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder
                .configure("hibernate.cfg.xml")
                .applySetting("hibernate.hbm2ddl.auto","create-drop")
                .applySetting("hibernate.format_sql","true")
                .applySetting("hibernate.use_sql_comments","true");
        sessionFactory = new MetadataSources(serviceRegistryBuilder.build()).buildMetadata().buildSessionFactory();
    }

    @Test
    public void addCourseShouldAddOneCourse(){
        hibernateHelper.addCourse(TEST_COURSE);
        Session session = sessionFactory.openSession();
        List<CourseUSD> courseUSDList = session.createCriteria(CourseUSD.class).list();
        Assertions.assertEquals( 1,courseUSDList.size());
        Assertions.assertEquals(TEST_COURSE, courseUSDList.get(courseUSDList.size() - 1));
        session.close();
    }

    @Test
    public void getLastCourseDateOrIfEmptyReturnThirtyDayAgoDateShouldReturnRightDate(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Calendar testCourseDate = new GregorianCalendar(2019,Calendar.JANUARY,1);
        for (int dayOfMonth = 1;dayOfMonth <= 25;dayOfMonth++){
            session.save(new CourseUSD(2.5,0,1,testCourseDate,new GregorianCalendar()));
        }
        session.save(TEST_COURSE);
        session.getTransaction().commit();
        session.close();
        Assertions.assertEquals(TEST_COURSE.getCourseDate(),hibernateHelper.getLastCourseDateOrIfEmptyReturnThirtyDayAgoDate(Currency.USD));

    }

    @Test
    public void getLastCourseDateOrIfEmptyReturnThirtyDayAgoDateShouldReturnThirtyDayAgoDate (){
        Calendar thirtyDayAgoDate = new GregorianCalendar();
        thirtyDayAgoDate.add(Calendar.DAY_OF_MONTH,-30);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Assertions.assertEquals(dateFormat.format(thirtyDayAgoDate.getTime()),dateFormat.format(hibernateHelper.getLastCourseDateOrIfEmptyReturnThirtyDayAgoDate(Currency.USD).getTime()));
    }

    @Test
    public void getDynamicOfCoursesForCurrencyShouldReturnCorrectLastCourse(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(TEST_COURSE);
        session.getTransaction().commit();
        Assertions.assertEquals(TEST_COURSE.getCourse(),hibernateHelper.getDynamicOfCoursesForCurrency(Currency.USD));
    }

    @Test
    public void getReportForNDaysByCurrencyShouldNotThrowException (){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(TEST_COURSE);
        Assertions.assertDoesNotThrow(()->hibernateHelper.getReportForNDaysByCurrency(Currency.USD,1));
        session.close();
    }


    @AfterEach
    public void closeSessionAndSessionFactory(){
        sessionFactory.close();
    }
}
