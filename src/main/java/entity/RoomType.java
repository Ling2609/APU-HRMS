package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room_type")
public class RoomType implements Serializable {

    public enum RoomTypeName {
        SINGLE_STANDARD, DOUBLE_STANDARD, TWIN_STANDARD,
        QUAD_STANDARD, DELUXE_SUITE, VIP_SUITE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomtype_seq")
    @SequenceGenerator(name = "roomtype_seq", sequenceName = "roomtype_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoomTypeName roomTypeName;

    @Column(nullable = false)
    private double roomTypePrice;

    public RoomType() {}

    public RoomType(RoomTypeName roomTypeName, double roomTypePrice) {
        this.roomTypeName = roomTypeName;
        this.roomTypePrice = roomTypePrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RoomTypeName getRoomTypeName() { return roomTypeName; }
    public void setRoomTypeName(RoomTypeName roomTypeName) { this.roomTypeName = roomTypeName; }
    public double getRoomTypePrice() { return roomTypePrice; }
    public void setRoomTypePrice(double roomTypePrice) { this.roomTypePrice = roomTypePrice; }
}