package br.com.acrossatto.pocvalidation.repository.command;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.acrossatto.pocvalidation.domain.Request;

@Repository
public interface RequestCommandRepository extends JpaRepository<Request, Long> {

}