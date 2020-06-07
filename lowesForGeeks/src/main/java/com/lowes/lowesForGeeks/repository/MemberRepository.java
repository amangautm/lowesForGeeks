package com.lowes.lowesForGeeks.repository;

import com.lowes.lowesForGeeks.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member,Integer> {
    Iterable<Member> findByFirstNameAndLastName(String firstName, String lastName);
    Iterable<Member> findByFirstName(String firstName);
    Iterable<Member> findByLastName(String lastName);
}
