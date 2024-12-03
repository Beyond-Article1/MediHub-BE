package mediHub_be.notify.dto;

import lombok.*;
import mediHub_be.notify.entity.Notify;

public class NotifyDTO {

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        String id;
        String name;
        String content;
        String type;
        String createdAt;
        public static Response createResponse(Notify notify) {
            return Response.builder()
                    .content(notify.getNotiContent())
                    .id(notify.getNotiSeq().toString())
                    .name(notify.getReceiver().getUserName())
                    .createdAt(notify.getCreatedAt().toString())
                    .build();
        }
    }
}
