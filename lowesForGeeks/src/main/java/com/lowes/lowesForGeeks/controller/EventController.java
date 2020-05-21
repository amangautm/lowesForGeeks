package com.lowes.lowesForGeeks.controller;

import com.lowes.lowesForGeeks.Constants;
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

    private String mssg ="No such EVENT is associated with this member";
    private String message ="No such Event found in DataBase";

    @Autowired
    EventService eventService;
    @Autowired
    MemberService memberService;

    @Autowired
    public void setEventService(EventService eventService){ this.eventService = eventService; }

    @Autowired
    public void setMemberService(MemberService memberService){ this.memberService = memberService; }

    @GetMapping("lowesforgeeks/event")
    public ResponseEntity read(
            @RequestHeader(name = "loggedInMemberId") Integer id){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            List<Event> eventList = eventService.findAll(loggedInMember);
            if (eventList.size() > 0) {
                return new ResponseEntity(eventList, HttpStatus.OK);
            }
            else {
                throw new NoSuchElementException(mssg);
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @GetMapping("lowesforgeeks/event/{eventId}")
    public ResponseEntity read(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if(eventService.findById(eventId,loggedInMember).isPresent()){
                return new ResponseEntity(eventService.findById(eventId,loggedInMember).get(),HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException(mssg);
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @GetMapping("lowesforgeeks/event/trending")
    public ResponseEntity trending(
            @RequestHeader(name = "loggedInMemberId") Integer id) {
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            List<Event> eventList = eventService.viewTrendingEvents(loggedInMember);
            if (eventList.size() > 0) {
                return new ResponseEntity(eventList, HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException("No event found by this id");
            }
        }
        else{
             throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @GetMapping("lowesforgeeks/event/popular")
    public ResponseEntity popular(
            @RequestHeader(name = "loggedInMemberId") Integer id){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            List<Event> eventList =  eventService.viewPopularEvents(loggedInMember);
            if(eventList.size()>0) {
                  return new ResponseEntity(eventList, HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException("No event found by this id");
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @GetMapping("lowesforgeeks/event/upcoming")
    public ResponseEntity upcoming(
            @RequestHeader(name = "loggedInMemberId") Integer id){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            List<Event> eventList = eventService.viewUpcomingEvents(loggedInMember);
            if (eventList.size() > 0) {
                return new ResponseEntity(eventList, HttpStatus.OK);
            }
            else {
                throw new NoSuchElementException("No event found by this id");
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PostMapping("lowesforgeeks/event")
    public ResponseEntity create(
            @RequestHeader(name = "loggedInMemberId") Integer id ,
            @RequestBody Event event){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            return eventService.create(event,loggedInMember);
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/event/watch/{eventId}")
    public ResponseEntity watch(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            Optional<Event> event = eventService.findById(eventId,loggedInMember);
            if (event.isPresent()) {
                return new ResponseEntity(eventService.watch(event.get()), HttpStatus.OK);
            } else {
                throw new ValidationException("Event not found");
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/event/unwatch/{eventId}")
    public ResponseEntity unwatch(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            Optional<Event> event = eventService.findById(eventId, loggedInMember);
            if (event.isPresent()) {
                return new ResponseEntity(eventService.unwatch(event.get()), HttpStatus.OK);
            } else {
                throw new ValidationException("Event not found");
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/event/like/{eventId}")
    public ResponseEntity like(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (eventService.findById(eventId,loggedInMember).isPresent()) {
                Event event = eventService.findById(eventId, loggedInMember).get();
                return new ResponseEntity(eventService.like(event), HttpStatus.OK);
            } else {
                throw new ValidationException("Event not found");
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/event/unlike/{eventId}")
    public ResponseEntity unlike(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (eventService.findById(eventId,loggedInMember).isPresent()) {
                Event event = eventService.findById(eventId, loggedInMember).get();
                return new ResponseEntity(eventService.unlike(event), HttpStatus.OK);
            } else {
                throw new ValidationException("Event not found");
            }
        }
        else{
                throw new NoSuchElementException(Constants.loggingMember);
            }
    }

    @PutMapping("lowesforgeeks/event/participate/{eventId}")
    public ResponseEntity participate(
            @RequestHeader(name = "loggedInMemberId")Integer id,
            @PathVariable(name = "eventId") Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if(eventService.findById(eventId,loggedInMember).isPresent()) {
                Event event =eventService.findById(eventId,loggedInMember).get();
                return eventService.participate(event, loggedInMember);
            }
            else{
                throw new NoSuchElementException(mssg);
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/event/unparticipate/{eventId}")
    public ResponseEntity unparticipate(
            @RequestHeader(name = "loggedInMemberId")Integer id,
            @PathVariable(name = "eventId") Integer eventId){
         if(memberService.findById(id).isPresent()) {
             Member loggedInMember = memberService.findById(id).get();
             if(eventService.findById(eventId,loggedInMember).isPresent()) {
                 Event event =eventService.findById(eventId,loggedInMember).get();
                 return eventService.unParticipate(event, loggedInMember);
             }
             else{
                 throw new NoSuchElementException(mssg);
             }
         }
         else{
             throw new NoSuchElementException(Constants.loggingMember);
         }
    }

    @DeleteMapping("lowesforgeeks/event")
    public ResponseEntity delete(
            @RequestHeader(name = "loggedInMemberId") Integer id ,
            @RequestHeader(name = "eventId") Integer eventId){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(eventService.findById(eventId,loggedInMember).isPresent()) {
                Event event =eventService.findById(eventId,loggedInMember).get();
                return eventService.delete(event, loggedInMember);
            }
            else{
                throw new NoSuchElementException(mssg);
            }
        }
        else{
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

}
