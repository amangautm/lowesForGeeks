package com.lowes.lowesForGeeks.controller;

import com.lowes.lowesForGeeks.model.Event;
import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.service.EventService;
import com.lowes.lowesForGeeks.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class EventController {

    private String mssg ="Not Such Event";

    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;

    @Autowired
    public void setEventService(EventService eventService){ this.eventService = eventService; }

    @Autowired
    public void setMemberService(MemberService memberService){ this.memberService = memberService; }

    @GetMapping("lowesforgeeks/event")
    public ResponseEntity read(@RequestHeader(name = "loggedInMemberId") Integer id)
    {
        Member member = memberService.findById(id).get();
        List<Event> eventList =  eventService.findAll(member);
        if(eventList.size()>0) {
            return new ResponseEntity(eventList, HttpStatus.OK);
        }
        else {
            throw new NoSuchElementException(mssg);
        }
    }

    @GetMapping("lowesforgeeks/event/{eventId}")
    public ResponseEntity read(
            @RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable Integer eventId){
        Member member = memberService.findById(id).get();
        Optional<Event> event =  eventService.findById(eventId,member);
        if(event.isPresent()) {
            return new ResponseEntity(event, HttpStatus.OK);
        }
        else {
            throw new NoSuchElementException(mssg);
        }
    }

    @GetMapping("lowesforgeeks/event/trending")
    public ResponseEntity trending(
            @RequestHeader(name = "loggedInMemberId") Integer id) {
        Member member = memberService.findById(id).get();
        List<Event> eventList = eventService.viewTrendingEvents(member);
        if (eventList.size() > 0) {
            return new ResponseEntity(eventList, HttpStatus.OK);
        }
        else{
            throw new NoSuchElementException("No event found by this id");
        }
    }

    @GetMapping("lowesforgeeks/event/popular")
    public ResponseEntity popular(
            @RequestHeader(name = "loggedInMemberId") Integer id){
        Member member = memberService.findById(id).get();
        List<Event> eventList =  eventService.viewPopularEvents(member);
        if(eventList.size()>0) {
            return new ResponseEntity(eventList, HttpStatus.OK);
        }
        else {
            throw new NoSuchElementException("No event found by this id");
        }
    }

    @GetMapping("lowesforgeeks/event/upcoming")
    public ResponseEntity upcoming(
            @RequestHeader(name = "loggedInMemberId") Integer id){
        Member member = memberService.findById(id).get();
        List<Event> eventList =  eventService.viewUpcomingEvents(member);
        if(eventList.size()>0) {
            return new ResponseEntity(eventList, HttpStatus.OK);
        }
        else {
            throw new NoSuchElementException("No event found by this id");
        }
    }

    @PostMapping("lowesforgeeks/event")
    public ResponseEntity create(
            @RequestHeader(name = "loggedInMemberId") Integer id , @RequestBody Event event){
        return eventService.create(event,memberService.findById(id).get());
    }

    @PutMapping("lowesforgeeks/event/watch/{eventId}")
    public ResponseEntity watch(
            @RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable Integer eventId){
        Member member = memberService.findById(id).get();
        Optional<Event> event = eventService.findById(eventId,member);
        if(event.isPresent()) {
            return new ResponseEntity(eventService.watch(event.get()), HttpStatus.OK);
        }
        else {
            throw new ValidationException("Event not found");
        }
    }

    @PutMapping("lowesforgeeks/event/unwatch/{eventId}")
    public ResponseEntity unwatch(
            @RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable Integer eventId){
        Member member = memberService.findById(id).get();
        Optional<Event> event = eventService.findById(eventId,member);
        if(event.isPresent()) {
            return new ResponseEntity(eventService.unwatch(event.get()), HttpStatus.OK);
        }
        else {
            throw new ValidationException("Event not found");
        }
    }

    @PutMapping("lowesforgeeks/event/like/{eventId}")
    public ResponseEntity like(
            @RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable Integer eventId){
        Member member = memberService.findById(id).get();
        Optional<Event> event = eventService.findById(eventId,member);
        if(event.isPresent()) {
            return new ResponseEntity(eventService.like(event.get()), HttpStatus.OK);
        }
        else {
            throw new ValidationException("Event not found");
        }
    }

    @PutMapping("lowesforgeeks/event/unlike/{eventId}")
    public ResponseEntity unlike(
            @RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable Integer eventId){
        Member member = memberService.findById(id).get();
        Optional<Event> event = eventService.findById(eventId,member);
        if(event.isPresent()) {
            return new ResponseEntity(eventService.unlike(event.get()), HttpStatus.OK);
        }
        else {
            throw new ValidationException("Event not found");
        }
    }

    @PutMapping("lowesforgeeks/event/participate/{eventId}")
    public ResponseEntity participate(
            @RequestHeader(name = "loggedInMemberId")Integer id, @PathVariable(name = "eventId") Integer eventId){
        Member member = memberService.findById(id).get();
        Optional<Event> event = eventService.findById(eventId,member);
        if(event.isPresent()){
            return eventService.participate(event.get(),member);
        }
        else {
            throw new NoSuchElementException("Event not found");
        }
    }

    @PutMapping("lowesforgeeks/event/unparticipate/{eventId}")
    public ResponseEntity unparticipate(
            @RequestHeader(name = "loggedInMemberId")Integer id, @PathVariable(name = "eventId") Integer eventId){
        System.out.println(id);
        Member member =memberService.findById(id).get();
        Optional<Event> event = eventService.findById(eventId,member);
        if(event.isPresent()) {
            return eventService.unParticipate(event.get(),member);
        }
        else {
            throw new NoSuchElementException("Event not found");
        }
    }

    @DeleteMapping("lowesforgeeks/event")
    public ResponseEntity delete(
            @RequestHeader(name = "loggedInMemberId") Integer id , @RequestBody Event event){
        return eventService.delete(event,memberService.findById(id).get());
    }

}
