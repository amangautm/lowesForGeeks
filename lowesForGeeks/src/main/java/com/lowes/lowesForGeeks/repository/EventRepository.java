package com.lowes.lowesForGeeks.repository;

import com.lowes.lowesForGeeks.model.Event;
import com.lowes.lowesForGeeks.model.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event,Integer> {

    @Query(value=
            "select u from Event u where u.creatorTeamId = ?1")
    List<Event> findAll(Integer id);

    @Query(value=
            "Select u from Event u where u.eventType != ?1 and (?2 - u.creationTime) > 5184000000 and (u.startDateInMills-?2) > 604800000")
    List<Event> queryByTrendingEvents(EventType eventType, long today);

    @Query(value=
            "Select u from Event u where u.eventType != ?1 and (?2 - u.creationTime) > 2592000000 and (u.startDateInMills-?2) > 172800000")
    List<Event> queryByPopularEvents(EventType eventType, long today);

    @Query(value=
            "Select u from Event u where  (?1 - u.creationTime) > 608400000 and (u.startDateInMills-?1) > 86400000")
    List<Event> queryByUpcomingEvents(long today);

}
