import org.junit.jupiter.api.Test;
import ru.bachev.random.Forecast;

public class ForecastTest {
    @Test
    public void testBasic() {
        var forecast = new Forecast("Alex", "Basic");
        forecast.print();
    }

    @Test
    public void testSecure() {
        var forecast = new Forecast("Ivan", "Secure");
        forecast.print();
    }
}
