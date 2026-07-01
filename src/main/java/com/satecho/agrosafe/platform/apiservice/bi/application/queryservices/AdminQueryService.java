package com.satecho.agrosafe.platform.apiservice.bi.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetAllUsersQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AdminQueryService {
    Page<User> handle(GetAllUsersQuery query);
    Optional<User> handle(GetUserByIdQuery query);
}
