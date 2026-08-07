package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class RoomStatusReport extends Report{
    
    private ArrayList<BookingLog> bookingLogs = new ArrayList<>();
    private ArrayList<RoomLog> roomLogs = new ArrayList<>();
    
    public RoomStatusReport() {}
    
    public RoomStatusReport(long id, Report.ReportType type, LocalDate startTime, LocalDate endTime, LocalDateTime generateTime) {
        super(id, type, startTime, endTime, generateTime);
    }
    
    public RoomStatusReport(Report report) {
        super(report.getId(), report.getReportType(), report.getStartTime(), report.getEndTime(), report.getGenerateTime());
    }
    
    public ArrayList<BookingLog> getBookingsLogs() { return this.bookingLogs; }
    public void setBookingsLogs(ArrayList<BookingLog> input) { this.bookingLogs = input; }
    
    public ArrayList<RoomLog> getRoomLogs() { return this.roomLogs; }
    public void setRoomLogs(ArrayList<RoomLog> input) { this.roomLogs = input; }
    
    public RoomLog findRoomLogByID(Long id) {
        
        for(RoomLog log : roomLogs) {
            if (Objects.equals(log.getId(), id)) {
                return log;
            }
        }
        return new RoomLog();
        
    }
    
}