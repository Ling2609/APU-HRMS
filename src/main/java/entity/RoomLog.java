package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roomlog")
public class RoomLog implements Serializable  { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomlog_seq")
    @SequenceGenerator(name = "roomlog_seq", sequenceName = "roomlog_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Room.RoomStatus roomStatus;
    
    @Column(nullable = false)
    private double roomPrice;

    public RoomLog() {}

    public RoomLog(Room room, Report report) {
        this.room = room;
        this.roomPrice = room.getRoomType().getRoomTypePrice();
        this.roomStatus = room.getRoomStatus();
        this.report = report;
    }
    
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    
    public Room getRoom() { return this.room; }
    public void setRoom(Room room) { this.room = room; }
    
    public Room.RoomStatus getRoomStatus() { return this.roomStatus; }
    public void setRoomStatus(Room.RoomStatus status) { this.roomStatus = status; }
    
    public double getRoomPrice() { return this.roomPrice; }
    public void setRoomPrice(double price) { this.roomPrice = price; }
    
    public Long getReportID() { return this.report.getId(); }
    public void setReportID(Long input) { this.report.setId(input); }
    
}