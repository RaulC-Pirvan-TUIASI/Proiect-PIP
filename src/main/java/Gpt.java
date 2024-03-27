import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // You need Jackson to parse the JSON response
import com.sun.net.httpserver.Request;

public class Gpt {
    String[] questionData;

    String answer;
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    Gpt(){
        this.questionData=new String[5];
    }
    Gpt(String[] data){
        this.questionData=data;
    }
    private String createQuestion(){
        String question;
        question=questionData[0]+" ";
        for(int i=1;i<5;i++){
            question=question+i+") "+questionData[i]+" ";
        }
        question=question+"Answer only with the letter of the answer you think that is correct, there may be multiple correct answers";
        return  question;
    }
    private HttpResponse request(String question) throws  Exception{
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = new ObjectMapper().writeValueAsString(
                new ChatGPTRequest("gpt-3.5-turbo", new Message[]{new Message("user", question)})
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
    private String content(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");

        // Print only the content part
        return contentNode.asText();
    }
    public  String answer(String q) throws Exception {
        HttpResponse<String> response = request(q);
        return content(response);
    }
    public static void main(String[] args) throws Exception {
        String q="Which is the Capital of Italy? "
                + "The answers are a) Rome; b) London; c) Barcelona; d) Bucharest. ";
        String Q2 = createQuestion();
        System.out.println(answer(q));
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