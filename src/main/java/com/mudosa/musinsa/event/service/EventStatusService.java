package com.mudosa.musinsa.event.service;

import com.mudosa.musinsa.event.model.Event;
import com.mudosa.musinsa.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 이벤트 상태 변경 로직 (시스템 단) 분리
 *
 * 이 서비스는 스케줄러를 통해 주기적으로 이벤트의 상태를 자동으로 업데이트합니다.
 * - PLANNED → OPEN: started_at 시간이 되면 자동으로 이벤트 시작
 * - OPEN → ENDED: ended_at 시간이 지나면 자동으로 이벤트 종료
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventStatusService {

    private final EventRepository eventRepository;

    /**
     * 이벤트 상태를 자동으로 업데이트하는 스케줄러
     * 매 1분마다 실행되어 이벤트의 시작/종료 시간을 확인하고 상태를 업데이트합니다.
     */
    @Scheduled(cron = "0 * * * * *") // 매 분 00초에 실행
    @Transactional
    public void updateEventStatuses() {
        log.debug("이벤트 상태 자동 업데이트 시작");

        LocalDateTime now = LocalDateTime.now();

        // 1. PLANNED → OPEN: 시작 시간이 된 이벤트들
        int startedCount = startScheduledEvents(now);

        // 2. OPEN → ENDED: 종료 시간이 지난 이벤트들
        int endedCount = endExpiredEvents(now);

        if (startedCount > 0 || endedCount > 0) {
            log.info("이벤트 상태 업데이트 완료 - 시작: {}건, 종료: {}건", startedCount, endedCount);
        }
    }

    /**
     * PLANNED 상태이면서 started_at 시간이 된 이벤트들을 OPEN으로 변경
     */
    private int startScheduledEvents(LocalDateTime now) {
        List<Event> eventsToStart = eventRepository.findAllByStatusAndStartedAtBefore(
                Event.EventStatus.PLANNED,
                now
        );

        for (Event event : eventsToStart) {
            event.open();
            log.info("이벤트 시작 - ID: {}, 제목: '{}', 시작시간: {}",
                    event.getId(), event.getTitle(), event.getStartedAt());
        }

        return eventsToStart.size();
    }

    /**
     * OPEN 상태이면서 ended_at 시간이 지난 이벤트들을 ENDED로 변경
     */
    private int endExpiredEvents(LocalDateTime now) {
        List<Event> eventsToEnd = eventRepository.findAllByStatusAndEndedAtBefore(
                Event.EventStatus.OPEN,
                now
        );

        for (Event event : eventsToEnd) {
            event.end();
            log.info("이벤트 종료 - ID: {}, 제목: '{}', 종료시간: {}",
                    event.getId(), event.getTitle(), event.getEndedAt());
        }

        return eventsToEnd.size();
    }

    /**
     * 즉시 이벤트 상태를 업데이트합니다. (수동 호출용)
     * 테스트나 관리자 기능에서 사용할 수 있습니다.
     */
    @Transactional
    public void updateEventStatusesNow() {
        log.info("수동 이벤트 상태 업데이트 실행");
        updateEventStatuses();
    }
}
