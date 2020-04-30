package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Event;
import com.lowes.lowesForGeeks.model.EventType;
import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service("eventService")
public class EventServiceImpl implements EventService {

    private static long day = 86400000; //total milliseconds
    private String mssg = "Not have access";

    @Autowired
    EventRepository eventRepository;

    @Autowired
    TeamService teamService;

    @Override
    public List<Event> findAll(Member member) {
        Iterable<Event> eventList = eventRepository.findAll(member.getTeamId());
        for( Event event : eventList) {
            if(event.getNumberOfViews()!=null) {
                event.setNumberOfViews(event.getNumberOfViews() + 1);
            }
            else {
                event.setNumberOfViews(1);
            }
            eventRepository.save(event);
        }
        return getIteratorList(eventList);
    }

    private static List<Event> getIteratorList(Iterable<Event> iterator){
        List<Event> list = new ArrayList<>();
        iterator.forEach(list::add);
        return list;
    }

    @Override
    public Optional<Event> findById(Integer id, Member member) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isPresent()) {
            if (event.get().getNumberOfViews() != null) {
                event.get().setNumberOfViews(event.get().getNumberOfViews() + 1);
            } else {
                event.get().setNumberOfViews(1);
            }
            eventRepository.save(event.get());
        }
        return  eventRepository.findById(id);
    }

    @Override
    public List<Event> viewUpcomingEvents(Member member) {
        long today = Calendar.getInstance().getTimeInMillis();
        if (member.isOrganizationAdmin()) {
            return getIteratorList(eventRepository.findUpcomingEvents(EventType.Private, today));
        }
        else {
            return getIteratorList(eventRepository.findUpcomingEvents(member.getTeamId(), EventType.Private, today));
        }
    }

    @Override
    public List<Event> viewTrendingEvents(Member member) {
        long today = Calendar.getInstance().getTimeInMillis();
        if(member.isOrganizationAdmin()) {
            return getIteratorList(eventRepository.findTrendingEvents(EventType.Private, today));
        }
        else {
            return getIteratorList(eventRepository.findTrendingEvents(member.getTeamId(), EventType.Private, today));
        }
    }

    @Override
    public List<Event> viewPopularEvents(Member member) {
        long today = Calendar.getInstance().getTimeInMillis();
        if(member.isOrganizationAdmin()) {
            return getIteratorList(eventRepository.findPopularEvents(EventType.Private, today));
        }
        else {
            return getIteratorList(eventRepository.findPopularEvents(member.getTeamId(), EventType.Private, today));
        }
    }

    @Override
    public ResponseEntity create(Event event, Member member) {
        String start = event.getStartDateTime();
        Calendar calendar = DatatypeConverter.parseDateTime(start);
        long eventDate = calendar.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
        long diff = eventDate - cal.getTimeInMillis();
        event.setCreatedBy(member);
        event.setCreatorTeamId(member.getTeamId());
        event.setCreationTime(cal.getTimeInMillis());
        event.setStartDateInMills(eventDate);
        calendar = DatatypeConverter.parseDateTime(event.getEndDateTime());
        event.setEndDateInMills(calendar.getTimeInMillis());
        if(event.getEventType() == EventType.Private && diff > (day*2) )
            return new ResponseEntity(eventRepository.save(event), HttpStatus.OK);
        if(event.getEventType()==EventType.Team && (member.isTeamAdmin()||member.isOrganizationAdmin()) && diff>(day*7))
            return new ResponseEntity(eventRepository.save(event), HttpStatus.OK);
        if(event.getEventType()==EventType.Org && member.isOrganizationAdmin() && diff>(day*60))
            return new ResponseEntity(eventRepository.save(event),  HttpStatus.OK);
        throw new ValidationException(mssg);
    }

    @Override
    public ResponseEntity delete(Event event, Member member) {
        if(event.getEventType() == EventType.Team || event.getEventType()==EventType.Private){
            if(event.getCreatedBy().getId() == member.getId()) {
                eventRepository.delete(event);
                return new ResponseEntity("Delete successful", HttpStatus.OK);
            }
            else {
                throw new ValidationException(mssg);
            }
        }
        else if(event.getEventType()==EventType.Org){
            if(member.isOrganizationAdmin()){
                eventRepository.delete(event);
                return new ResponseEntity("Delete successful", HttpStatus.OK);
            }
            throw new ValidationException(mssg);
        }
        throw  new ValidationException(mssg);
    }

    @Override
    public ResponseEntity participate(Event event, Member member) {
        event.getParticipants().add(member);
        return new ResponseEntity(eventRepository.save(event), HttpStatus.OK);
    }

    @Override
    public ResponseEntity unParticipate(Event event, Member member) {
        event.getParticipants().remove(member);
        return new ResponseEntity(eventRepository.save(event), HttpStatus.OK);
    }

    @Override
    public Event unwatch(Event event) {
        if(event.getNumberOfWatchers()!=null) {
            event.setNumberOfWatchers(event.getNumberOfWatchers() - 1);
        }
        return eventRepository.save(event);
    }

    @Override
    public Event watch(Event event) {
        if(event.getNumberOfWatchers()!=null) {
            event.setNumberOfWatchers(event.getNumberOfWatchers() + 1);
        }
        else {
            event.setNumberOfWatchers(1);
        }
        return eventRepository.save(event);
    }

    @Override
    public Event like(Event event) {
        if(event.getNumberOfLikes()!=null) {
            event.setNumberOfLikes(event.getNumberOfLikes() + 1);
        }
        else {
            event.setNumberOfLikes(1);
        }
        return eventRepository.save(event);
    }

    @Override
    public Event unlike(Event event) {
        if(event.getNumberOfLikes()!=null) {
            event.setNumberOfLikes(event.getNumberOfLikes() - 1);
        }
        return eventRepository.save(event);
    }
}
