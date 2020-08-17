package hibernateClasses;
import org.Denis.Currency;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class HibernateHelper {
    private static HibernateHelper hibernateHelper;
    private final SessionFactory sessionFactory;

    private HibernateHelper(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static HibernateHelper getInstance(){
        if (hibernateHelper == null){
            hibernateHelper = new HibernateHelper();
        }
        return hibernateHelper;
    }

    public void addCourse (Course course){
        Session session = sessionFactory.openSession(); //withOptions().jdbcTimeZone(TimeZone.getTimeZone("UTC"))
        session.beginTransaction();
        session.save(course);
        session.getTransaction().commit();
        session.close();
    }

    public Calendar getLastCourseDateOrIfEmptyReturnThirtyDayAgoDate(Currency currency){
        Session session = sessionFactory.openSession();
        String hql = MessageFormat.format("FROM {0} WHERE id = (SELECT max(id) FROM {0})", getNameEntityByCurrency(currency));
        Query<Course> query = session.createQuery(hql);
        Course course = query.uniqueResult();
        session.close();
        if (course == null){
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            return calendar;
        }
        return course.getCourseDate();
    }

    public double getDynamicOfCoursesForCurrency(Currency currency){
        Session session = sessionFactory.openSession();
        String hql = MessageFormat.format("FROM {0} WHERE id = (SELECT max(id) FROM {0})", getNameEntityByCurrency(currency));
        Query<Course> query = session.createQuery(hql);
        Course course = query.uniqueResult();
        if (course == null){
            return 0;
        }
        return course.getCourse();
    }

    public String getReportForNDaysByCurrency(Currency currency, int n){
        Session session = sessionFactory.openSession();
        String hql = MessageFormat.format("FROM {0} c ORDER BY c.courseDate DESC",getNameEntityByCurrency(currency));
        Query<Course> query = session.createQuery(hql).setFirstResult(0).setMaxResults(n);
        StringBuilder reportForTenDaysBuilder = new StringBuilder();
        SimpleDateFormat courseDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        query.list().stream()
                .sorted()
                .map(x -> courseDateFormat.format(x.getCourseDate().getTime()) + ": " + x.getScale() + " " + currency + " = " + x.getCourse() + " BYN" + " (" + x.getDynamic() + ")" + "\n")
                .forEach(reportForTenDaysBuilder::append);
        return reportForTenDaysBuilder.toString();
    }

    private String getNameEntityByCurrency(Currency currency){
        String currencyNameToLowerCase = currency.toString().toLowerCase();
        return "course_" + currencyNameToLowerCase;
    }
}

