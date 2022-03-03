package br.com.acrossatto.pocvalidation.repository.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.acrossatto.pocvalidation.domain.Request;

@Repository
public interface RequestQueryRepository extends JpaRepository<Request, Long> {

}