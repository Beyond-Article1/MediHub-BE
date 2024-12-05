package mediHub_be.notify.dto;

import lombok.*;
import mediHub_be.notify.entity.NotiType;
import mediHub_be.notify.entity.Notify;

public class NotifyDTO {

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Data
    public static class Response {
        String id;
        String name;
        String content;
        NotiType type;
        String createdAt;
        public static Response createResponse(Notify notify) {
            return Response.builder()
                    .content(notify.getNotiContent())
                    .id(notify.getNotiSeq().toString())
                    .name(notify.getReceiver().getUserName())
                    .type(notify.getNotiType())
                    .createdAt(notify.getCreatedAt().toString())
                    .build();
        }
    }
}
