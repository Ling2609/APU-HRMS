package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionReport extends Report{
    
    private ArrayList<BookingLog> bookingLogs = new ArrayList<>();
    
    public TransactionReport() {}
    
    public TransactionReport(long id, Report.ReportType type, LocalDate startTime, LocalDate endTime, LocalDateTime generateTime) {
        super(id, type, startTime, endTime, generateTime);
    }
    
    public TransactionReport(Report report) {
        super(report.getId(), report.getReportType(), report.getStartTime(), report.getEndTime(), report.getGenerateTime());
    }
    
    public ArrayList<BookingLog> getBookingsLogs() { return this.bookingLogs; }
    public void setBookingsLogs(ArrayList<BookingLog> input) { this.bookingLogs = input; }
    
}
