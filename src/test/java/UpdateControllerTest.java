import hibernateClasses.Course;
import org.Denis.Currency;
import org.Denis.UpdateController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class UpdateControllerTest {
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void getSessionFactory(){
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder
                .configure("hibernate.cfg.xml")
                .applySetting("hibernate.hbm2ddl.auto","create-drop")
                .applySetting("hibernate.format_sql","true")
                .applySetting("hibernate.use_sql_comments","true");
        sessionFactory = new MetadataSources(serviceRegistryBuilder.build()).buildMetadata().buildSessionFactory();
    }

    @Test
    public void updateAllDataShouldWork() throws IOException {
        UpdateController updateController = UpdateController.getInstance();
        Assertions.assertNotNull(updateController);
        Assertions.assertDoesNotThrow(updateController::updateAllData);
        Session session = sessionFactory.openSession();

        for (Currency temp : Currency.values()){
            String tableName = "course_" + temp.toString().toLowerCase();
            String hql = MessageFormat.format("FROM {0}",tableName);
            Query <Course> query = session.createQuery("FROM course_usd ");
            List<Course> courseList = query.getResultList();
            Assertions.assertTrue(courseList.size() >= 30);
        }

        session.close();
    }

    @AfterAll
    public static void closeSessionFactory(){
        sessionFactory.close();
    }
}
