package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "message")
public class Message implements Serializable {

    public enum MessageType {
        COMMENT, FEEDBACK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_user_id", nullable = false)
    private BookingUser bookingUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @Column(nullable = false)
    private String messageContent;

    private Integer rating;

    public Message() {}

    public Message(BookingUser bookingUser, MessageType messageType,
                   String messageContent, Integer rating) {
        this.bookingUser = bookingUser;
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.rating = rating;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BookingUser getBookingUser() { return bookingUser; }
    public void setBookingUser(BookingUser bookingUser) { this.bookingUser = bookingUser; }
    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}