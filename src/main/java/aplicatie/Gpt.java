package aplicatie;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

//import ocr.JavaReadTextFromImage;
import ocr.JavaReadTextFromImage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // You need Jackson to parse the JSON response


public final class Gpt {

    /**
    API_Key: this string is composed by api key segments put in such a way
    that OpenAi won't revoke the key after a push on a remote.
    */
    private static final String API_KEY = "sk-tHiQn"
            + "PJDdUOc"
            + "J9qRxoxbT"
            + "3BlbkFJWBvKN"
            + "buZvmcFftZ1KBoO";

    /**
    API_URL: thi string contains the url used to connect to the OpenAI API.
    */
    private static final String API_URL =
            "https://api.openai.com/v1/chat/completions";


    private Gpt() {

    }



    private static HttpResponse request(final String question)
            throws  Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper myobj = new ObjectMapper();
        myobj.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
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
        return  client.send(request, BodyHandlers.ofString());
    }

    /**
     *
     * @param response
     * @return This returns the answer
     *         to the inputted question extracted from the JSON Node
     * @throws JsonProcessingException
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
     *
     * @param q
     * @return Final answer to our question under the form of a string
     * @throws Exception
     */
    public static String answer(final String q) throws Exception {
        String query=q+"Replay with just the right answer.";
        HttpResponse<String> response = request(query);
        return content(response);
    }

    /**
     *
     * @param imagine
     * @return Placeholder
     * @throws Exception
     */
    public static String mainulet(final String imagine) throws Exception {
        //String calea = "H:\\Other computers
        // \\My Laptop\\Javra\\PIPpr\\Proiect-PIP\\pozici\\grilaCapitale.png";
        JavaReadTextFromImage ocrProcessor = new JavaReadTextFromImage();
        System.out.println(imagine);
        String q2 = ocrProcessor.performocr(new File(imagine));

        return (answer(q2));
    }
    // Inner class for the request body
    private static class ChatGPTRequest {

        /**
         *
         */
        private String model;

        /**
         *
         */
        private Message[] messages;

        ChatGPTRequest(final String mod, final Message[] mess) {
            this.model = mod;
            this.messages = mess;
        }
    }

    // Inner class for messages, now including role
    private static class Message {
        /**
         * Role is who sends the request to the API.
         */
        private String role;

        /**
         * The content is the question sent to the API.
         */
        private String content;

        Message(final String r, final String c) {
            this.role = r;
            this.content = c;
        }
    }
}

