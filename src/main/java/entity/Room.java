package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room")
public class Room implements Serializable {

    public enum RoomStatus {
        FREE, OCCUPIED, CLEANING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    @SequenceGenerator(name = "room_seq", sequenceName = "room_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private int roomNumber;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus roomStatus;

    public Room() {}

    public Room(int roomNumber, RoomType roomType, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }
    public RoomStatus getRoomStatus() { return roomStatus; }
    public void setRoomStatus(RoomStatus roomStatus) { this.roomStatus = roomStatus; }
}