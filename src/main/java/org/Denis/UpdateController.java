package org.Denis;


import hibernateClasses.Course;
import hibernateClasses.CourseFactory;
import hibernateClasses.HibernateHelper;
import urlConnectionClasses.Rate;
import urlConnectionClasses.UrlUpdater;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UpdateController {
    private static UpdateController updateController;

    private UpdateController(){}

    public static UpdateController getInstance(){
        if (updateController == null){
            updateController = new UpdateController();
        }
        return updateController;
    }
    public synchronized void updateAllData() throws IOException {
        for (Currency temp : Currency.values()){
            updateByCurrency(temp);
        }
        //Arrays.stream(Currency.values()).forEach(this::updateByCurrency);
    }

    private void updateByCurrency(Currency currency) throws IOException {
        HibernateHelper hibernateHelper = HibernateHelper.getInstance();
        Calendar currentDateWithoutTime = getCurrentDateWithoutTime();
        Calendar courseDate = hibernateHelper.getLastCourseDateOrIfEmptyReturnThirtyDayAgoDate(currency);
        while (courseDate.before(currentDateWithoutTime)){
            courseDate.add(Calendar.DAY_OF_MONTH, 1);
            hibernateHelper.addCourse(getCourseByDate(courseDate,currency));
        }
    }

    private Course getCourseByDate (Calendar calendar,Currency currency) throws IOException {
        UrlUpdater urlUpdater = UrlUpdater.getInstance();
        Rate rate = urlUpdater.getDataByDateAndCurrency(calendar, currency);
        return fromRateToCourseByCurrency(rate, currency);
    }

    private Course fromRateToCourseByCurrency(Rate rate, Currency currency){
        Calendar courseDate = new GregorianCalendar();
        courseDate.setTime(rate.Date);
        Course course =  CourseFactory.getCourseByCurrency(currency);
        course.setCourse(rate.Cur_OfficialRate);
        double dynamic = 0.00;
        double lastCourse = HibernateHelper.getInstance().getDynamicOfCoursesForCurrency(currency);
        if(lastCourse !=0) {
            dynamic = course.getCourse() - lastCourse;
        }
        BigDecimal roundingBigDecimal = new BigDecimal(dynamic).setScale(3,RoundingMode.HALF_UP);
        course.setDynamic(roundingBigDecimal.doubleValue());
        course.setScale(rate.Cur_Scale);
        course.setCourseDate(courseDate);
        course.setUpdateDateStamp(new GregorianCalendar());
        return course;
    }

    private Calendar getCurrentDateWithoutTime() {
        Calendar currentDateWithoutTime = new GregorianCalendar();

        currentDateWithoutTime.set(Calendar.HOUR_OF_DAY, 0);
        currentDateWithoutTime.set(Calendar.MINUTE, 0);
        currentDateWithoutTime.set(Calendar.SECOND, 0);
        currentDateWithoutTime.set(Calendar.MILLISECOND, 0);

        return currentDateWithoutTime;
    }
    static class AutoUpdater implements Runnable{
        private final LocalTime MORNING_UPDATE_TIME = LocalTime.of(20,27);//7:20
        private final LocalTime EVENING_UPDATE_TIME = LocalTime.of(15,30);//15:30
        private static final long HOURS_TO_MILLISECONDS = 60 * 60 * 1000;
        private static final long MINUTES_TO_MILLISECONDS = 60 * 1000;
        private static final long SECONDS_TO_MILLISECONDS = 1000;
        @Override
        public void run() {
            long sleepTimeInMilliseconds;
            while (true) {
                LocalTime currentTime = LocalTime.now();
                if (currentTime.isBefore(MORNING_UPDATE_TIME)){
                    sleepTimeInMilliseconds = (
                            ((MORNING_UPDATE_TIME.getHour() - currentTime.getHour()) * HOURS_TO_MILLISECONDS) +
                            ((MORNING_UPDATE_TIME.getMinute() - currentTime.getMinute()) * MINUTES_TO_MILLISECONDS) +
                            ((MORNING_UPDATE_TIME.getSecond() - currentTime.getSecond()) * SECONDS_TO_MILLISECONDS));
                }else if (currentTime.isBefore(EVENING_UPDATE_TIME)){
                    sleepTimeInMilliseconds = (
                            ((EVENING_UPDATE_TIME.getHour() - currentTime.getHour()) * HOURS_TO_MILLISECONDS) +
                                    ((EVENING_UPDATE_TIME.getMinute() - currentTime.getMinute()) * MINUTES_TO_MILLISECONDS) +
                                    ((EVENING_UPDATE_TIME.getSecond() - currentTime.getSecond()) * SECONDS_TO_MILLISECONDS));
                }else {
                    sleepTimeInMilliseconds = (
                            (24 * HOURS_TO_MILLISECONDS - currentTime.getHour() * HOURS_TO_MILLISECONDS) -
                                    currentTime.getMinute() * MINUTES_TO_MILLISECONDS -
                                    currentTime.getSecond() * SECONDS_TO_MILLISECONDS +
                                    MORNING_UPDATE_TIME.getHour() * HOURS_TO_MILLISECONDS +
                                    MORNING_UPDATE_TIME.getMinute() * MINUTES_TO_MILLISECONDS +
                                    MORNING_UPDATE_TIME.getSecond() * SECONDS_TO_MILLISECONDS);
                }
                try {
                    Thread.sleep(sleepTimeInMilliseconds);
                } catch (InterruptedException e) {
                    System.out.println("AutoUpdate's sleep is failed");
                    e.printStackTrace();
                }
                try {
                    updateController.updateAllData();
                } catch (IOException ioException) {
                    try {
                        throw ioException;
                    } catch (IOException exception) {
                        System.out.println("AutoUpdate Thread cannot throws exception");
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
}
