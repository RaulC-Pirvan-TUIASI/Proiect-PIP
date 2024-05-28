package aplicatie;

import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * GPT test class contains unit tests for the Gpt application.
 */
public class GptTest {

    /**
     * Default constructor for GptTest.
     * This constructor is provided for documentation purposes and
     * to comply with the code quality guidelines.
     */
    public GptTest() {
        // Default constructor
    }

    /**
     * Test for the answer method in the Gpt class.
     *
     * @throws Exception if the answer method throws an exception
     */
    @Test
    void testanswer() throws Exception {
        // Test pentru functia answer
        String q;
        q = """
                Neil Armstrong becomes the first person to walk on the moon
                +1969
                *1973
                *1977
                *1983
                """;
        assertEquals("1969", Gpt.answer(q));
        q = """
                4. The Wright Brothers fly the first airplane
                ,1888
                .1903
                '1917
                `1931
                """;
        assertEquals("1903", Gpt.answer(q));
        q = """
                Which Italian city is also known as the City of Canals?
                +Florence
                *Venice
                *Milan
                """;
        assertEquals("Venice", Gpt.answer(q));
    }

    /**
     * Test for the mainulet method in the Gpt class.
     *
     * @throws Exception if the mainulet method throws an exception
     */
    @Test
    void testmaingpt() throws Exception {
        // Test pentru mainulet
        assertEquals("Roma", Gpt.mainulet("testImages/grilaCapitale.png"));
        assertEquals("Adevarat", Gpt.mainulet("testImages/MT.png"));
    }
}
