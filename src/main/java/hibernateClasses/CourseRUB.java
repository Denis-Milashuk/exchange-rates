package hibernateClasses;

import javax.persistence.Entity;
import java.util.Calendar;

@Entity(name = "course_rub")
public class CourseRUB extends Course {

    public CourseRUB(){}

    public CourseRUB(double course, double scale, Calendar courseDate, Calendar updateDateStamp) {
        super(course, scale, courseDate,updateDateStamp);
    }

}