package hibernateClasses;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

@MappedSuperclass
public class Course implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private double course;

    @Column(nullable = false)
    private double scale;

    @Column(nullable = false,name = "course_date")
    @Basic
    @Temporal(TemporalType.DATE)
    private Calendar courseDate;

    @Column(nullable = false,name = "update_date_stamp")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateDateStamp;

    Course(){}

    Course(double course,double scale, Calendar courseDate, Calendar updateDateStamp){
        this.course = course;
        this.scale = scale;
        this.courseDate = courseDate;
        this.updateDateStamp = updateDateStamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Calendar getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Calendar courseDate) {
        this.courseDate = courseDate;
    }

    public Calendar getUpdateDateStamp() {
        return updateDateStamp;
    }

    public void setUpdateDateStamp(Calendar updateDateStamp) {
        this.updateDateStamp = updateDateStamp;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", course=" + course +
                ", scale=" + scale +
                ", courseDate=" + courseDate +
                ", updateDateStamp=" + updateDateStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course1 = (Course) o;
        return id == course1.id &&
                Double.compare(course1.course, course) == 0 &&
                Double.compare(course1.scale, scale) == 0 &&
                courseDate.equals(course1.courseDate) &&
                updateDateStamp.equals(course1.updateDateStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, scale, courseDate, updateDateStamp);
    }

    @Override
    public int compareTo(Object o) {
        if(this.getCourseDate().before(((Course)o).getCourseDate())) return -1;
        return 1;
    }
}
