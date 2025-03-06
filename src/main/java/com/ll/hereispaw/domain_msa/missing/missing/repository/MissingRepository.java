package com.ll.hereispaw.domain_msa.missing.missing.repository;

import com.ll.hereispaw.domain_msa.missing.missing.entity.Missing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissingRepository extends JpaRepository<Missing, Long> {
}
