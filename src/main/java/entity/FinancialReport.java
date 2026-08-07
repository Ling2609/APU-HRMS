package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FinancialReport extends Report{
    
    private ArrayList<BookingLog> bookingLogs = new ArrayList<>();
    private ArrayList<SalaryLog> salaryLogs = new ArrayList<>();
    
    public FinancialReport() {}
    
    public FinancialReport(long id, ReportType type, LocalDate startTime, LocalDate endTime, LocalDateTime generateTime) {
        super(id, type, startTime, endTime, generateTime);
    }
    
    public FinancialReport(Report report) {
        super(report.getId(), report.getReportType(), report.getStartTime(), report.getEndTime(), report.getGenerateTime());
    }
    
    public ArrayList<BookingLog> getBookingsLogs() { return this.bookingLogs; }
    public void setBookingsLogs(ArrayList<BookingLog> input) { this.bookingLogs = input; }
    
    public ArrayList<SalaryLog> getSalaryLogs() { return this.salaryLogs; }
    public void setSalaryLogs(ArrayList<SalaryLog> input) { this.salaryLogs = input; }
    
}
