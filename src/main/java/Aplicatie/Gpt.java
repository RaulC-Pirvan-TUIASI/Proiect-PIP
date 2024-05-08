package Aplicatie;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

//import OCR.javaOCR;
import OCR.javaOCR;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // You need Jackson to parse the JSON response


public class Gpt {
    String answer;
    private static final String API_KEY1 = "sk-tHiQn";
    private static final String API_KEY2 = "PJDdUOc";
    private static final String API_KEY3 = "J9qRxoxbT";
    private static final String API_KEY4= "3BlbkFJWBvKN";
    private static final String API_KEY5 = "buZvmcFftZ1KBoO";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    Gpt(){
    }
    Gpt(String[] data) {
    }
    /*
    private static String createQuestion(){
        String question;
        question=questionData[0]+" ";
        for(int i=1;i<5;i++){
            question=question+i+") "+questionData[i]+" ";
        }
        question=question+"Answer only with the letter of the answer you think that is correct, there may be multiple correct answers";
        return  question;
    }
 */

////

    private static HttpResponse request(String question) throws  Exception{
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = new ObjectMapper().writeValueAsString(
                new ChatGPTRequest("gpt-3.5-turbo", new Message[]{new Message("user", question)})
        );

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY1+API_KEY2+API_KEY3+API_KEY4+API_KEY5)
                .POST(BodyPublishers.ofString(requestBody))
                .build();
        return  client.send(request, BodyHandlers.ofString());
    }
    private static String content(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");

        // Print only the content part
        return contentNode.asText();
    }
    public static String answer(String q) throws Exception {
        HttpResponse<String> response = request(q);
        return content(response);
    }
    public static String mainulet(String imagine) throws Exception {
        //String calea = "H:\\Other computers\\My Laptop\\Javra\\PIPpr\\Proiect-PIP\\pozici\\grilaCapitale.png";
        javaOCR ocrProcessor = new javaOCR();
        System.out.println(imagine);
        String q2=ocrProcessor.performOCR(new File(imagine));


        //String q="Which is the Capital of Italy?\n" + "\nRome;\nLondon\nBarcelona\nBucharest.\n";
        //String Q2 = createQuestion();
        return (answer(q2));
    }
    // Inner class for the request body
    private static class ChatGPTRequest {
        public String model;
        public Message[] messages;

        public ChatGPTRequest(String model, Message[] messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    // Inner class for messages, now including role
    private static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}

