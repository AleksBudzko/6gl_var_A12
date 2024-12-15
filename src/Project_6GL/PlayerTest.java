package Project_6GL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    // Subclass for testing
    static class TestPlayer extends Player {
        public TestPlayer(String name) {
            super(name);
        }
    }

    private TestPlayer player;

    @BeforeEach
    void setUp() {
        player = new TestPlayer("TestPlayer");
    }

    @Test
    void testGetName() {
        assertEquals("TestPlayer", player.getName(), "Name should match the initialized value");
    }

    @Test
    void testGetType() {
        assertEquals("TestPlayer", player.getType(), "Type should be the class name");
    }

    @Test
    void testIsTurnedOnInitially() {
        assertFalse(player.IsTurnedOn(), "Player should be off initially");
    }

    @Test
    void testChangeCondition() {
        // Check the state before changing
        assertFalse(player.IsTurnedOn(), "Player should be off initially");

        // Change state to "on"
        player.ChangeCondition();
        assertTrue(player.IsTurnedOn(), "Player should be on after first change");

        // Change state back to "off"
        player.ChangeCondition();
        assertFalse(player.IsTurnedOn(), "Player should be off after second change");
    }
}