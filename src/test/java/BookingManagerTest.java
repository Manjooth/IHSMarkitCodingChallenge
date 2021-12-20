import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class BookingManagerTest
{

    private BookingManager bookingManager;

    @BeforeEach
    void setUp()
    {
        bookingManager = new BookingManager();
        bookingManager.addRoom(1);
        bookingManager.getRoom(1).addBookingDate(LocalDate.now());
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
        final boolean response = bookingManager.isRoomAvailable(100, LocalDate.now());
        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenRoomIsNotAvailable()
    {
        final boolean response = bookingManager.isRoomAvailable(1, LocalDate.now());
        assertFalse(response);
    }

    @Test
    void shouldReturnTrueWhenRoomIsAvailable()
    {
        bookingManager.addRoom(2);
        final boolean response = bookingManager.isRoomAvailable(2, LocalDate.now());

        assertTrue(response);
    }

    @Test
    void shouldThrowErrorWhenRoomIsNotAvailableForBooking()
    {
        final BookingException thrown = assertThrows(BookingException.class, () -> {
            bookingManager.addBooking("Kler", 1, LocalDate.now());
        });
        assertEquals("Room 1 is already booked", thrown.getMessage());
    }

    @Test
    void shouldNotThrowErrorWhenRoomIsAvailableForBooking()
    {
        bookingManager.addRoom(2);
        assertDoesNotThrow(() ->
                bookingManager.addBooking("Kler", 2, LocalDate.now())
        );
    }

    @Test
    void shouldBeAbleToAddBooking()
    {
        bookingManager.addRoom(101);
        bookingManager.addBooking("Wallis", 101, LocalDate.now());

        assertTrue(bookingManager.getRoom(101).getDatesBooked().contains(LocalDate.now()));
    }

    @Test
    void shouldReturnNoRoomsWhenGetAvailableRoomsAndNoneAreFree()
    {
        assertEquals(Collections.emptyList(), bookingManager.getAvailableRooms(LocalDate.now()));
    }

    @Test
    void shouldReturnRoomsWhenGetAvailableRoomsAndRoomsAreFree()
    {
        bookingManager.addRoom(2);
        bookingManager.addRoom(3);

        final List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);

        final Iterable<Integer> expectedIterable = expected;

        assertIterableEquals(expectedIterable, bookingManager.getAvailableRooms(LocalDate.now()));
    }

    @Test
    void shouldBeThreadSafe() throws InterruptedException
    {
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
            assertFalse(bookingManagerThreads.isRoomAvailable(i, LocalDate.now()));
        });

    }

}
