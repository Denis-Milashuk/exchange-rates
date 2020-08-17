package hibernateClasses;

import javax.persistence.Entity;
import java.util.Calendar;

@Entity(name = "course_eur")
public class CourseEUR extends Course {

    public CourseEUR () {}

    public CourseEUR(double course, double dynamic, double scale, Calendar courseDate, Calendar updateDateStamp) {
        super(course, dynamic, scale, courseDate, updateDateStamp);
    }

}
