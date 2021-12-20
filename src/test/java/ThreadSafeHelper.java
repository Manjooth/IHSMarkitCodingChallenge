import java.time.LocalDate;
import java.util.stream.IntStream;

public class ThreadSafeHelper extends Thread
{

    private final BookingManager bookingManager;

    public ThreadSafeHelper(final BookingManager bookingManager)
    {
        this.bookingManager = bookingManager;
    }

    @Override
    public void run()
    {
        final String name = currentThread().getName();
        IntStream.range(0, 10).forEach(i -> {
            bookingManager.addRoom(i);
            System.out.println(i);
            bookingManager.addBooking(name, i, LocalDate.now());
        });
    }

}
