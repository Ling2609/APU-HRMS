package controller.common;

import entity.*;
import entity.RoomType.RoomTypeName;
import entity.Room.RoomStatus;
import entity.User.Role;
import entity.Booking.BookingStatus;
import entity.BookingUser.BookingUserRole;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import session.*;
import java.time.LocalDateTime;

@Singleton
@Startup
public class StartupData {

    @EJB private UserFacade userFacade;
    @EJB private RoomTypeFacade roomTypeFacade;
    @EJB private RoomFacade roomFacade;
    @EJB private BookingFacade bookingFacade;
    @EJB private BookingUserFacade bookingUserFacade;

    @PostConstruct
    public void init() {
        if (userFacade.count() > 0) return; // already seeded

        // ── Users ──────────────────────────────────────────────
        User manager1 = new User("manager1", "pass123", "Female", "IC001", 601111111, "alice@hotel.com", "KL", Role.MANAGER, 5000.0);

        User staff1 = new User("staff1", "pass123", "Male", "IC002", 601111112, "bob@hotel.com", "KL", Role.COUNTER_STAFF, 3000.0);
        User staff2 = new User("staff2", "pass123", "Female", "IC003", 601111113, "carol@hotel.com", "KL", Role.COUNTER_STAFF, 3000.0);

        User hk1 = new User("hk1", "pass123", "Male", "IC004", 601111114, "david@hotel.com", "KL", Role.HOUSEKEEPER, 2000.0);
        User hk2 = new User("hk2", "pass123", "Female", "IC005", 601111115, "eve@hotel.com", "KL", Role.HOUSEKEEPER, 2000.0);
        User hk3 = new User("hk3", "pass123", "Male", "IC006", 601111116, "frank@hotel.com", "KL", Role.HOUSEKEEPER, 2000.0);
        User hk4 = new User("hk4", "pass123", "Female", "IC007", 601111117, "grace@hotel.com", "KL", Role.HOUSEKEEPER, 2000.0);
        User hk5 = new User("hk5", "pass123", "Male", "IC008", 601111118, "henry@hotel.com", "KL", Role.HOUSEKEEPER, 2000.0);

        User c1  = new User("customer1",  "pass123", "Male",   "IC011", 601111121, "c1@mail.com",  "PJ", Role.CUSTOMER, null);
        User c2  = new User("customer2",  "pass123", "Female", "IC012", 601111122, "c2@mail.com",  "PJ", Role.CUSTOMER, null);
        User c3  = new User("customer3",  "pass123", "Male",   "IC013", 601111123, "c3@mail.com",  "PJ", Role.CUSTOMER, null);
        User c4  = new User("customer4",  "pass123", "Female", "IC014", 601111124, "c4@mail.com",  "PJ", Role.CUSTOMER, null);
        User c5  = new User("customer5",  "pass123", "Male",   "IC015", 601111125, "c5@mail.com",  "PJ", Role.CUSTOMER, null);
        User c6  = new User("customer6",  "pass123", "Female", "IC016", 601111126, "c6@mail.com",  "PJ", Role.CUSTOMER, null);
        User c7  = new User("customer7",  "pass123", "Male",   "IC017", 601111127, "c7@mail.com",  "PJ", Role.CUSTOMER, null);
        User c8  = new User("customer8",  "pass123", "Female", "IC018", 601111128, "c8@mail.com",  "PJ", Role.CUSTOMER, null);
        User c9  = new User("customer9",  "pass123", "Male",   "IC019", 601111129, "c9@mail.com",  "PJ", Role.CUSTOMER, null);
        User c10 = new User("customer10", "pass123", "Female", "IC020", 601111130, "c10@mail.com", "PJ", Role.CUSTOMER, null);
        userFacade.create(manager1);
        userFacade.create(staff1); userFacade.create(staff2);
        userFacade.create(hk1); userFacade.create(hk2); userFacade.create(hk3);
        userFacade.create(hk4); userFacade.create(hk5);
        userFacade.create(c1); userFacade.create(c2); userFacade.create(c3);
        userFacade.create(c4); userFacade.create(c5); userFacade.create(c6);
        userFacade.create(c7); userFacade.create(c8); userFacade.create(c9);
        userFacade.create(c10);

        // ── Room Types ─────────────────────────────────────────
        RoomType single  = new RoomType(RoomTypeName.SINGLE_STANDARD, 250.0);
        RoomType dbl     = new RoomType(RoomTypeName.DOUBLE_STANDARD, 300.0);
        RoomType twin    = new RoomType(RoomTypeName.TWIN_STANDARD,   350.0);
        RoomType quad    = new RoomType(RoomTypeName.QUAD_STANDARD,   550.0);
        RoomType deluxe  = new RoomType(RoomTypeName.DELUXE_SUITE,    650.0);
        RoomType vip     = new RoomType(RoomTypeName.VIP_SUITE,       850.0);

        roomTypeFacade.create(single); roomTypeFacade.create(dbl);
        roomTypeFacade.create(twin);   roomTypeFacade.create(quad);
        roomTypeFacade.create(deluxe); roomTypeFacade.create(vip);

        // ── Rooms ──────────────────────────────────────────────
        // Floor 1: 1001-1005 Single, 1006-1010 Double
        roomFacade.create(new Room(1001, single, RoomStatus.FREE));
        roomFacade.create(new Room(1002, single, RoomStatus.FREE));
        roomFacade.create(new Room(1003, single, RoomStatus.FREE));
        roomFacade.create(new Room(1004, single, RoomStatus.FREE));
        roomFacade.create(new Room(1005, single, RoomStatus.FREE));
        roomFacade.create(new Room(1006, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(1007, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(1008, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(1009, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(1010, dbl, RoomStatus.FREE));

        // Floor 2: 2001-2005 Double, 2006-2010 Twin
        roomFacade.create(new Room(2001, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(2002, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(2003, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(2004, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(2005, dbl, RoomStatus.FREE));
        roomFacade.create(new Room(2006, twin, RoomStatus.FREE));
        roomFacade.create(new Room(2007, twin, RoomStatus.FREE));
        roomFacade.create(new Room(2008, twin, RoomStatus.FREE));
        roomFacade.create(new Room(2009, twin, RoomStatus.FREE));
        roomFacade.create(new Room(2010, twin, RoomStatus.FREE));

        // Floor 3: 3001-3005 Twin, 3006-3010 Quad
        roomFacade.create(new Room(3001, twin, RoomStatus.FREE));
        roomFacade.create(new Room(3002, twin, RoomStatus.FREE));
        roomFacade.create(new Room(3003, twin, RoomStatus.FREE));
        roomFacade.create(new Room(3004, twin, RoomStatus.FREE));
        roomFacade.create(new Room(3005, twin, RoomStatus.FREE));
        roomFacade.create(new Room(3006, quad, RoomStatus.FREE));
        roomFacade.create(new Room(3007, quad, RoomStatus.FREE));
        roomFacade.create(new Room(3008, quad, RoomStatus.FREE));
        roomFacade.create(new Room(3009, quad, RoomStatus.FREE));
        roomFacade.create(new Room(3010, quad, RoomStatus.FREE));

        // Floor 4: 4001-4005 Quad, 4006-4010 Deluxe
        roomFacade.create(new Room(4001, quad, RoomStatus.FREE));
        roomFacade.create(new Room(4002, quad, RoomStatus.FREE));
        roomFacade.create(new Room(4003, quad, RoomStatus.FREE));
        roomFacade.create(new Room(4004, quad, RoomStatus.FREE));
        roomFacade.create(new Room(4005, quad, RoomStatus.FREE));
        roomFacade.create(new Room(4006, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(4007, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(4008, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(4009, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(4010, deluxe, RoomStatus.FREE));

        // Floor 5: 5001-5005 Deluxe, 5006-5010 VIP
        roomFacade.create(new Room(5001, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(5002, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(5003, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(5004, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(5005, deluxe, RoomStatus.FREE));
        roomFacade.create(new Room(5006, vip, RoomStatus.FREE));
        roomFacade.create(new Room(5007, vip, RoomStatus.FREE));
        roomFacade.create(new Room(5008, vip, RoomStatus.FREE));
        roomFacade.create(new Room(5009, vip, RoomStatus.FREE));
        roomFacade.create(new Room(5010, vip, RoomStatus.FREE));

        // ── Sample Bookings ────────────────────────────────────
        LocalDateTime now = LocalDateTime.now();

        Room r1001 = roomFacade.findByRoomNumber(1001);
        Room r1006 = roomFacade.findByRoomNumber(1006);
        Room r2001 = roomFacade.findByRoomNumber(2001);
        Room r2006 = roomFacade.findByRoomNumber(2006);
        Room r3001 = roomFacade.findByRoomNumber(3001);
        Room r3006 = roomFacade.findByRoomNumber(3006);
        Room r4001 = roomFacade.findByRoomNumber(4001);

        // 2 ongoing (CHECKED_IN)
        Booking b1 = new Booking(c1, staff1, now.minusDays(1), now.plusDays(2),
                r1001.getRoomType().getRoomTypePrice() * 3, r1001, BookingStatus.CHECKED_IN);
        b1.setCheckInTime(now.minusDays(1));
        r1001.setRoomStatus(RoomStatus.OCCUPIED);
        roomFacade.edit(r1001);

        Booking b2 = new Booking(c2, staff1, now.minusDays(2), now.plusDays(1),
                r1006.getRoomType().getRoomTypePrice() * 3, r1006, BookingStatus.CHECKED_IN);
        b2.setCheckInTime(now.minusDays(2));
        r1006.setRoomStatus(RoomStatus.OCCUPIED);
        roomFacade.edit(r1006);

        // 5 completed (CHECKED_OUT)
        Booking b3 = new Booking(c3, staff1, now.minusDays(5), now.minusDays(3),
                r2001.getRoomType().getRoomTypePrice() * 2, r2001, BookingStatus.CHECKED_OUT);
        b3.setCheckInTime(now.minusDays(5)); b3.setCheckOutTime(now.minusDays(3));

        Booking b4 = new Booking(c4, staff2, now.minusDays(6), now.minusDays(4),
                r2006.getRoomType().getRoomTypePrice() * 2, r2006, BookingStatus.CHECKED_OUT);
        b4.setCheckInTime(now.minusDays(6)); b4.setCheckOutTime(now.minusDays(4));

        Booking b5 = new Booking(c5, staff2, now.minusDays(7), now.minusDays(5),
                r3001.getRoomType().getRoomTypePrice() * 2, r3001, BookingStatus.CHECKED_OUT);
        b5.setCheckInTime(now.minusDays(7)); b5.setCheckOutTime(now.minusDays(5));

        Booking b6 = new Booking(c6, staff1, now.minusDays(8), now.minusDays(6),
                r3006.getRoomType().getRoomTypePrice() * 2, r3006, BookingStatus.CHECKED_OUT);
        b6.setCheckInTime(now.minusDays(8)); b6.setCheckOutTime(now.minusDays(6));

        Booking b7 = new Booking(c7, staff2, now.minusDays(9), now.minusDays(7),
                r4001.getRoomType().getRoomTypePrice() * 2, r4001, BookingStatus.CHECKED_OUT);
        b7.setCheckInTime(now.minusDays(9)); b7.setCheckOutTime(now.minusDays(7));

        bookingFacade.create(b1); bookingFacade.create(b2);
        bookingFacade.create(b3); bookingFacade.create(b4);
        bookingFacade.create(b5); bookingFacade.create(b6);
        bookingFacade.create(b7);

        // BookingUser links
        bookingUserFacade.create(new BookingUser(b1, c1, BookingUserRole.CUSTOMER));
        bookingUserFacade.create(new BookingUser(b2, c2, BookingUserRole.CUSTOMER));
        bookingUserFacade.create(new BookingUser(b3, c3, BookingUserRole.CUSTOMER));
        bookingUserFacade.create(new BookingUser(b4, c4, BookingUserRole.CUSTOMER));
        bookingUserFacade.create(new BookingUser(b5, c5, BookingUserRole.CUSTOMER));
        bookingUserFacade.create(new BookingUser(b6, c6, BookingUserRole.CUSTOMER));
        bookingUserFacade.create(new BookingUser(b7, c7, BookingUserRole.CUSTOMER));
    }
}