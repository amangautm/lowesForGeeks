package com.lowes.lowesForGeeks.repository;

import com.lowes.lowesForGeeks.model.Event;
import com.lowes.lowesForGeeks.model.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event,Integer> {

    @Query(value=
            "select u from Event u where u.creatorTeamId = ?1")
    Iterable<Event> findAll(Integer id);

    @Query(value=
            "select u from Event u where u.creatorTeamId = ?1 and u.eventType != ?2 and u.isRecurring = false and (?3 - u.creationTime) > 5184000000 and (u.startDateInMills-?3) > 604800000")
    Iterable<Event> findTrendingEvents(Integer userTeamId, EventType eventType, long today) ;

    @Query(value=
            "Select u from Event u where u.eventType != ?1 and (?2 - u.creationTime) > 5184000000 and (u.startDateInMills-?2) > 604800000")
    Iterable<Event> findTrendingEvents(EventType eventType, long today);

    @Query(value=
            "select u from Event u where u.creatorTeamId = ?1 and u.eventType != ?2 and u.isRecurring = false and (?3 - u.creationTime) > 2592000000 and (u.startDateInMills-?3) > 172800000")
    Iterable<Event> findPopularEvents(Integer userTeamId, EventType eventType, long today);

    @Query(value=
            "Select u from Event u where u.eventType != ?1 and (?2 - u.creationTime) > 2592000000 and (u.startDateInMills-?2) > 172800000")
    Iterable<Event> findPopularEvents(EventType eventType, long today);

    @Query(value=
            "select u from Event u where u.creatorTeamId = ?1 and u.eventType != ?2 and u.isRecurring = false and (?3 - u.creationTime) > 604800000 and (u.startDateInMills-?3) > 86400000")
    Iterable<Event> findUpcomingEvents(Integer userTeamId, EventType aPrivate, long today);

    @Query(value=
            "Select u from Event u where u.eventType != ?1 and (?2 - u.creationTime) > 608400000 and (u.startDateInMills-?2) > 86400000")
    Iterable<Event> findUpcomingEvents(EventType aPrivate, long today);

}
