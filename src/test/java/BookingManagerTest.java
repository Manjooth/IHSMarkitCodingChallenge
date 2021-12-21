import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class BookingManagerTest
{

    public static final LocalDate BOOKED_MONDAY_DATE = LocalDate.of(2021, 12, 20); // MONDAY
    private BookingManager bookingManager;

    @BeforeEach
    void setUp()
    {
        bookingManager = new BookingManager();
        bookingManager.addRoom(1);
        bookingManager.getRoom(1).addBookingDate(BOOKED_MONDAY_DATE);
    }

    @Test
    void shouldReturnErrorWhenAddingRoomWithNonUniqueNumber()
    {
        final String response = bookingManager.addRoom(1);
        assertEquals("Invalid - Room already exists", response);
    }

    @Test
    void shouldReturnSuccessWhenAddingRoomWithUniqueNumber()
    {
        final String response = bookingManager.addRoom(2);
        assertEquals("SUCCESS", response);
    }

    @Test
    void shouldAddNewRoomToHashMap()
    {
        final Room room = bookingManager.getRoom(1);
        assertNotNull(room);
    }

    @Test
    void shouldReturnFalseWhenRoomDoesNotExist()
    {
        final boolean response = bookingManager.isRoomAvailable(100, BOOKED_MONDAY_DATE);
        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenRoomIsNotAvailable()
    {
        final boolean response = bookingManager.isRoomAvailable(1, BOOKED_MONDAY_DATE);
        assertFalse(response);
    }

    @Test
    void shouldReturnTrueWhenRoomIsAvailable()
    {
        bookingManager.addRoom(2);
        final boolean response = bookingManager.isRoomAvailable(2, BOOKED_MONDAY_DATE);

        assertTrue(response);
    }

    @Test
    void shouldThrowErrorWhenRoomIsNotAvailableForBooking()
    {
        final BookingException thrown = assertThrows(BookingException.class, () -> {
            bookingManager.addBooking("Kler", 1, BOOKED_MONDAY_DATE);
        });
        assertEquals("Room 1 is already booked", thrown.getMessage());
    }

    @Test
    void shouldNotThrowErrorWhenRoomIsAvailableForBooking()
    {
        bookingManager.addRoom(2);
        assertDoesNotThrow(() ->
                bookingManager.addBooking("Kler", 2, BOOKED_MONDAY_DATE)
        );
    }

    @Test
    void shouldBeAbleToAddBooking()
    {
        bookingManager.addRoom(101);
        bookingManager.addBooking("Wallis", 101, BOOKED_MONDAY_DATE);

        assertTrue(bookingManager.getRoom(101).getDatesBooked().contains(BOOKED_MONDAY_DATE));
    }

    @Test
    void shouldReturnNoRoomsAreAvailableWhenNonRoomsAreFree()
    {
        assertEquals(Collections.emptyList(), bookingManager.getAvailableRooms(BOOKED_MONDAY_DATE));
    }

    @Test
    void shouldReturnRoomsAvailableWhenRoomsAreFree()
    {
        bookingManager.addRoom(2);
        bookingManager.addRoom(3);

        final List<Integer> expected = Arrays.asList(2,3);
        final Iterable<Integer> expectedIterable = expected;

        assertIterableEquals(expectedIterable, bookingManager.getAvailableRooms(BOOKED_MONDAY_DATE));
    }

    @Test
    void shouldReturnRoomAvailableWhenBookingIsFreeBetweenTwoDates()
    {
        bookingManager.getRoom(1).addBookingDate(LocalDate.of(2021, 12, 22)); // Monday already booked, also book Wednesday

        final List<Integer> expected = Arrays.asList(1);
        final Iterable<Integer> expectedIterable = expected;

        assertTrue(bookingManager.isRoomAvailable(1, LocalDate.of(2021, 12, 21))); // Try and book Tuesday
        assertIterableEquals(expectedIterable, bookingManager.getAvailableRooms(LocalDate.of(2021, 12, 21)));
    }

    @Test
    void shouldBeThreadSafe() throws InterruptedException {
        final BookingManager bookingManagerThreads = new BookingManager();

        final ThreadSafeHelper thread1 = new ThreadSafeHelper(bookingManagerThreads);
        final ThreadSafeHelper thread2 = new ThreadSafeHelper(bookingManagerThreads);
        final ThreadSafeHelper thread3 = new ThreadSafeHelper(bookingManagerThreads);

        thread1.start(); thread2.start(); thread3.start();

        while(thread1.isAlive() || thread2.isAlive() || thread3.isAlive()){
            Thread.sleep(10);
        }

        IntStream.range(0, 10).forEach(i -> {
            // expected, actual
            assertFalse(bookingManagerThreads.isRoomAvailable(i, BOOKED_MONDAY_DATE));
        });

    }
}
