package hibernateClasses;

import javax.persistence.Entity;
import java.util.Calendar;

@Entity(name = "course_usd")
public class CourseUSD extends Course {

    public CourseUSD(){}

    public CourseUSD(double course, double dynamic, double scale, Calendar courseDate, Calendar updateDateStamp) {
        super(course, dynamic, scale, courseDate, updateDateStamp);
    }

}
