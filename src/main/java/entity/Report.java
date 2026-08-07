package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report implements Serializable {

    public enum ReportType {
        NONE, FINANCIAL, TRANSACTION, ARRIVAL_DEPARTURE, ROOM_STATUS, COMMENT_FEEDBACK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq", allocationSize = 1)
    private Long id;

    private ReportType reportType;
    private LocalDate startTime;
    private LocalDate endTime;
    private LocalDateTime generateTime;

    public Report() {}

    public Report(long id, ReportType type, LocalDate startTime, LocalDate endTime, LocalDateTime generateTime) {
        this.id = id;
        this.reportType = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.generateTime = generateTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ReportType getReportType() { return this.reportType; }
    public void setReportType(ReportType input) { this.reportType = input; }
    
    public LocalDate getStartTime() { return this.startTime; }
    public void setStartTime(LocalDate input) { this.startTime = input; }
    
    public LocalDate getEndTime() { return this.endTime; }
    public void setEndTime(LocalDate input) { this.endTime = input; }
    
    public LocalDateTime getGenerateTime() { return this.generateTime; }
    public void setGenerateTime(LocalDateTime input) { this.generateTime = input; }
    
    public static ReportType getReportType(String input) {
                
        ReportType returnType = ReportType.NONE;
        
        switch(input.toUpperCase()) {
            case "FINANCIAL": 
                returnType = ReportType.FINANCIAL;
                break;
            case "ARRIVAL_DEPARTURE": 
                returnType = ReportType.ARRIVAL_DEPARTURE;
                break;
            case "ROOM_STATUS": 
                returnType = ReportType.ROOM_STATUS;
                break;
            case "COMMENT_FEEDBACK": 
                returnType = ReportType.COMMENT_FEEDBACK;
                break;
            case "TRANSACTION": 
                returnType = ReportType.TRANSACTION;
                break;
        }
        return returnType;
    }
    
    @Override
    public String toString() {
        return "Report: " + this.getId() + " " + this.getReportType() + " " + this.getStartTime() + " " + this.getEndTime() + " " + this.getGenerateTime();
    }
        
}