package com.lowes.lowesForGeeks.repository;

import com.lowes.lowesForGeeks.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization,Integer> {

}
