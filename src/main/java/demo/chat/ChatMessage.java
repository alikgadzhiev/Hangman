package demo.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String room;
    private String sender;
    private String word;
    private MessageType type;

}
