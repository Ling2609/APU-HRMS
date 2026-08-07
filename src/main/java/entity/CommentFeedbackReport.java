package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommentFeedbackReport extends Report{
    
    private ArrayList<BookingLog> bookingLogs = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    
    public CommentFeedbackReport() {}
    
    public CommentFeedbackReport(long id, Report.ReportType type, LocalDate startTime, LocalDate endTime, LocalDateTime generateTime) {
        super(id, type, startTime, endTime, generateTime);
    }
    
    public CommentFeedbackReport(Report report) {
        super(report.getId(), report.getReportType(), report.getStartTime(), report.getEndTime(), report.getGenerateTime());
    }
    
    public ArrayList<BookingLog> getBookingsLogs() { return this.bookingLogs; }
    public void setBookingsLogs(ArrayList<BookingLog> input) { this.bookingLogs = input; }
    
    public ArrayList<Message> getMessages() { return this.messages; }
    public void setMessages(ArrayList<Message> input) { this.messages = input; }
    
}
