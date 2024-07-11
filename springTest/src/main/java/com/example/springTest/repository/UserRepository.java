package com.example.springTest.repository;

import com.example.springTest.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método criado para consultar o usuário no banco de dados
    UserDetails findByLogin(String username);
}
