package com.mudosa.musinsa.notification.controller;

import com.mudosa.musinsa.notification.dto.NotificationDTO;
import com.mudosa.musinsa.notification.event.SpringNotificationEventPublisher;
import com.mudosa.musinsa.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final SpringNotificationEventPublisher springNotificationEventPublisher;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<NotificationDTO>> readNotification(@PathVariable Long userId, Pageable pageable) {
        Page<NotificationDTO> notificationDTOs = notificationService.readNotification(userId, pageable);
        if(notificationDTOs.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificationDTOs);
    }

    @PatchMapping("/read")
    public ResponseEntity<Integer> updateNotification(@RequestBody NotificationDTO notificationDTO) {
        Integer result = notificationService.updateNotificationState(notificationDTO.getNotificationId());
        if (result <= 0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> createNotification(@RequestBody NotificationDTO notificationDTO) {
        springNotificationEventPublisher.publishCreateNotificationEvent(notificationDTO.getUserId(), notificationDTO.getNotificationId());
        return ResponseEntity.ok(200);
    }

//    @PostMapping("/create/test")
//    public NotificationDTO createNotification(@RequestBody NotificationDTO notificationDTO) {
//        notificationService.createNotificationFromDTO(notificationDTO);
//        return notificationDTO;
//    }

}