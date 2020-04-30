package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Event;
import com.lowes.lowesForGeeks.model.Member;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Optional<Event> findById(Integer id, Member member);

    List<Event> findAll(Member member);

    List<Event> viewUpcomingEvents(Member member);

    List<Event> viewTrendingEvents(Member member);

    List<Event> viewPopularEvents(Member member);

    ResponseEntity create(Event event, Member member);

    ResponseEntity delete(Event event, Member member);

    ResponseEntity participate(Event event, Member member);

    ResponseEntity unParticipate(Event event, Member member);

    Event unwatch(Event event);

    Event watch(Event event);

    Event like(Event event);

    Event unlike(Event event);
}
