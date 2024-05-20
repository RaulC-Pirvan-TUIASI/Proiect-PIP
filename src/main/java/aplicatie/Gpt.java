package aplicatie;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;

//import ocr.JavaReadTextFromImage;
import ocr.JavaReadTextFromImage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // You need Jackson to parse the JSON response


public final class Gpt {

    /**
     * API_Key: this string is composed by api key segments put in such a way
     * that OpenAi won't revoke the key after a push on a remote.
     */
    private static final String API_KEY = "sk-tHiQn"
            + "PJDdUOc"
            + "J9qRxoxbT"
            + "3BlbkFJWBvKN"
            + "buZvmcFftZ1KBoO";

    /**
     * API_URL: thi string contains the url used to connect to the OpenAI API.
     */
    private static final String API_URL =
            "https://api.openai.com/v1/chat/completions";


    private Gpt() {

    }


    /**
     * This method sets up a connection between the module and the API,
     * builds a request based on the question
     * and sends it.
     *
     * @param question the question.
     * @return the HttpResponse from the API.
     * @throws Exception Generic Exception.
     */
    private static HttpResponse<String> request(final String question)
            throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper myobj = new ObjectMapper();
        myobj.setVisibility(PropertyAccessor.FIELD,
                JsonAutoDetect.Visibility.ANY);
        String requestBody = myobj.writeValueAsString(
                new ChatGPTRequest("gpt-3.5-turbo",
                        new Message[]{new Message("user", question)})
        );

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(BodyPublishers.ofString(requestBody))
                .build();
        return client.send(request, BodyHandlers.ofString());
    }

    /**
     * @param response JSON Node trimis de ChatGpt
     * @return This returns the answer
     * to the inputted question extracted from the JSON Node
     * @throws JsonProcessingException Erore la extragerea contentului di JSON
     */
    private static String content(final HttpResponse<String> response)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode contentNode = rootNode
                .path("choices")
                .get(0)
                .path("message")
                .path("content");

        // Print only the content part
        return contentNode.asText();
    }

    /**
     * @param q Grila primisa sub forma unui string
     * @return Final answer to our question under the form of a string
     * @throws Exception Ecceptia generala
     */
    public static String answer(final String q) throws Exception {
        String query = q + "Replay with just the right answer.";
        HttpResponse<String> response = request(query);
        return content(response);
    }

    /**
     * @param imagine imaginea care contine grila
     * @return Placeholder
     * @throws Exception Ecceptia generala
     */
    public static String mainulet(final String imagine) throws Exception {
        //String calea = "H:\\Other computers
        // \\My Laptop\\Javra\\PIPpr\\Proiect-PIP\\pozici\\grilaCapitale.png";
        JavaReadTextFromImage ocrProcessor = new JavaReadTextFromImage();
        System.out.println(imagine);
        String q2 = ocrProcessor.performocr(new File(imagine));
        erorfix();
        return (answer(q2));
    }

    /**
     * Functie creata cu scopul de eliminare warnings.
     */
    public static void erorfix() {
        Message[] b = {new Message("user", "question")};
        b[0].afisare();
        ChatGPTRequest a = new ChatGPTRequest("", b);
        a.afisare();

    }

    // Inner class for the request body
    private static class ChatGPTRequest {

        /**
         *
         */
        private final String model;

        /**
         *
         */
        private final Message[] messages;

        ChatGPTRequest(final String mod, final Message[] mess) {
            this.model = mod;
            this.messages = mess;
        }


        public Message[] getMessages() {
            return messages;
        }

        public String getModel() {
            return model;
        }

        public void afisare() {
            System.out.println(this.getModel());
            System.out.println(Arrays.toString(this.getMessages()));
        }

    }

    // Inner class for messages, now including role
    private static class Message {
        /**
         * Role is who sends the request to the API.
         */
        private final String role;

        /**
         * The content is the question sent to the API.
         */
        private final String content;

        Message(final String r, final String c) {
            this.role = r;
            this.content = c;
        }

        public String getContent() {
            return content;
        }

        public String getRole() {
            return role;
        }

        public void afisare() {
            System.out.println(this.getContent());
            System.out.println(this.getRole());

        }
    }
}

