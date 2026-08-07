package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ArrivalDepartureReport extends Report{
    
    private ArrayList<BookingLog> bookingLogs = new ArrayList<>();
    private ArrayList<RoomLog> roomLogs = new ArrayList<>();
    
    public ArrivalDepartureReport() {}
    
    public ArrivalDepartureReport(long id, Report.ReportType type, LocalDate startTime, LocalDate endTime, LocalDateTime generateTime) {
        super(id, type, startTime, endTime, generateTime);
    }
    
    public ArrivalDepartureReport(Report report) {
        super(report.getId(), report.getReportType(), report.getStartTime(), report.getEndTime(), report.getGenerateTime());
    }
    
    public ArrayList<BookingLog> getBookingsLogs() { return this.bookingLogs; }
    public void setBookingsLogs(ArrayList<BookingLog> input) { this.bookingLogs = input; }
    
    public ArrayList<RoomLog> getRoomLogs() { return this.roomLogs; }
    public void setRoomLogs(ArrayList<RoomLog> input) { this.roomLogs = input; }
    
}
