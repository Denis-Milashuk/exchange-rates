package hibernateClasses;

import javax.persistence.Entity;
import java.util.Calendar;

@Entity(name = "course_clp")
public class CourseCLP extends Course {

    public CourseCLP () {}

    public CourseCLP(double course,double scale, Calendar courseDate, Calendar updateDateStamp) {
        super(course,scale,courseDate,updateDateStamp);
    }

}
