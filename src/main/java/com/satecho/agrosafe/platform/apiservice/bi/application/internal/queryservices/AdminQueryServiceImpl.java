package com.satecho.agrosafe.platform.apiservice.bi.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.AdminQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetAllUsersQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdminQueryServiceImpl implements AdminQueryService {

    private final UserPersistenceRepository userRepository;

    public AdminQueryServiceImpl(UserPersistenceRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> handle(GetAllUsersQuery query) {
        PageRequest pageRequest = PageRequest.of(query.page(), query.size(),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return userRepository.findAll(pageRequest)
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId())
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }
}
