import com.sentinel.deeptrace.core.SentinelCore
import org.junit.Test

class SentinelCoreTest {
    @Test
    fun `Yen Break unter 150 triggert Crash Score`() {
        val score = SentinelCore.calculateScore(149.0, 200.0, 8.0,)
        assert(score < 3.0) // Sollte niedrigen Score (Exit) liefern
    }
}