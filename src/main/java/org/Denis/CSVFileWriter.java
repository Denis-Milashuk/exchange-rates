package org.Denis;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class CSVFileWriter {
    private static CSVFileWriter csvFileWriter;
    private CSVFileWriter(){
    }

    public static CSVFileWriter getInstance(){
        if (csvFileWriter == null){
            return new CSVFileWriter();
        }
        return csvFileWriter;
    }

    public void WriteReportInCSVFileByCurrency(String report, Currency currency){
        String DateAndTimeCreatingFile  = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss").format(new GregorianCalendar().getTime());
        String fileName = currency + "report:" + DateAndTimeCreatingFile + ".csv";
        String [] ArrayReport = isReadyingTheReportForWritingInCSVFile(report);
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))) {
            csvWriter.writeNext(new String[]{"Course Date","Scale","Course"});
            for (String row : ArrayReport) {
                csvWriter.writeNext(row.split(":"));
            }
        } catch (IOException ioException) {
            System.out.println("Ошибка создания файла с отчетом");
        }
    }

    private String [] isReadyingTheReportForWritingInCSVFile(String report){
        String convertedReport = report.replaceAll("=",":");
        return convertedReport.split("\n");
    }

}
