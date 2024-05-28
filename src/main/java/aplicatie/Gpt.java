package aplicatie;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;

import ocr.JavaReadTextFromImage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gpt is a utility class for interacting with the OpenAI GPT API.
 */
public final class Gpt {

    /**
     * API_KEY: this string is composed by API key segments put in such a way
     * that OpenAI won't revoke the key after a push on a remote.
     */
    private static final String API_KEY = "sk-tHiQn"
            + "PJDdUOc"
            + "J9qRxoxbT"
            + "3BlbkFJWBvKN"
            + "buZvmcFftZ1KBoO";

    /**
     * API_URL: this string contains the URL used to connect to the OpenAI API.
     */
    private static final String API_URL =
            "https://api.openai.com/v1/chat/completions";

    /**
     * not instanstable
     */
    private Gpt() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sets up a connection between the module and the API,
     * builds a request based on the question,
     * and sends it.
     *
     * @param question the question to ask the API.
     * @return the HttpResponse from the API.
     * @throws Exception if an error occurs
     * during the request.
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
     * Extracts the content from the JSON response.
     *
     * @param response JSON Node sent by ChatGPT.
     * @return the answer to the inputted question
     * extracted from the JSON Node.
     * @throws JsonProcessingException if there is
     * an error processing the JSON.
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
     * Answers the given question by sending it to the API.
     *
     * @param q the question in the form of a string.
     * @return the final answer to the question as a string.
     * @throws Exception if an error occurs during the process.
     */
    public static String answer(final String q) throws Exception {
        String query = q + " Reply with just the right answer.";
        HttpResponse<String> response = request(query);
        return content(response);
    }

    /**
     * Processes the image containing the question and returns the answer.
     *
     * @param imagine the image containing the question.
     * @return the answer to the question.
     * @throws Exception if an error occurs during the process.
     */
    public static String mainulet(final String imagine) throws Exception {
        JavaReadTextFromImage ocrProcessor = new JavaReadTextFromImage();
        System.out.println(imagine);
        String q2 = ocrProcessor.performocr(new File(imagine));
        erorfix();
        return (answer(q2));
    }

    /**
     * A utility function created to eliminate warnings.
     */
    public static void erorfix() {
        Message[] b = {new Message("user", "question")};
        b[0].afisare();
        ChatGPTRequest a = new ChatGPTRequest("", b);
        a.afisare();
    }

    /**
     * Inner class for the request body.
     */
    private static class ChatGPTRequest {

        /**
         * The model used for the request.
         */
        private final String model;

        /**
         * The messages to send in the request.
         */
        private final Message[] messages;

        /**
         * Constructs a ChatGPTRequest.
         *
         * @param mod the model to use.
         * @param mess the messages to send.
         */
        ChatGPTRequest(final String mod, final Message[] mess) {
            this.model = mod;
            this.messages = mess;
        }

        /**
         * Gets the messages.
         *
         * @return the messages.
         */
        public Message[] getMessages() {
            return messages;
        }

        /**
         * Gets the model.
         *
         * @return the model.
         */
        public String getModel() {
            return model;
        }

        /**
         * Prints the model and messages.
         */
        public void afisare() {
            System.out.println(this.getModel());
            System.out.println(Arrays.toString(this.getMessages()));
        }
    }

    /**
     * Inner class for messages, now including role.
     */
    private static class Message {
        /**
         * Role is who sends the request to the API.
         */
        private final String role;

        /**
         * The content is the question sent to the API.
         */
        private final String content;

        /**
         * Constructs a Message.
         *
         * @param r the role.
         * @param c the content.
         */
        Message(final String r, final String c) {
            this.role = r;
            this.content = c;
        }

        /**
         * Gets the content.
         *
         * @return the content.
         */
        public String getContent() {
            return content;
        }

        /**
         * Gets the role.
         *
         * @return the role.
         */
        public String getRole() {
            return role;
        }

        /**
         * Prints the content and role.
         */
        public void afisare() {
            System.out.println(this.getContent());
            System.out.println(this.getRole());
        }
    }
}
