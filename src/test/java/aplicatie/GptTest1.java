package aplicatie;

import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GptTest1 {

    @Test
    void testanswer() throws Exception {
        // Test pentru functia answer
        String q;
        q = "Neil Armstrong becomes the first person to walk on the moon\n+1969\n*1973\n*1977\n*1983\n";
        assertEquals("1969", Gpt.answer(q));
        q = "4. The Wright Brothers fly the first airplane\n,1888\n.1903\n'1917\n`1931\n";
        assertEquals("1903", Gpt.answer(q));
        q = "Which Italian city is also known as the City of Canals?\n+Florence\n*Venice\n*Milan\n";
        assertEquals("Venice", Gpt.answer(q));

    }
    @Test
    void testmaingpt() throws Exception {
        // Test pentru mainulet
        assertEquals("Roma", Gpt.mainulet("testImages/grilaCapitale.png"));
        assertEquals("Adevarat", Gpt.mainulet("testImages/MT.png"));

    }

}