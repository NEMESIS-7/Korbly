package com.arete.korbly.modules.shared.generics;

import java.sql.Timestamp;
import java.util.UUID;

public interface BaseEntityInterface<T>{
    UUID getUserId();

    T getUser(UUID userId);

    Timestamp getCreatedOn(T userId);

    Timestamp getUpdatedOn(T userId);


}
