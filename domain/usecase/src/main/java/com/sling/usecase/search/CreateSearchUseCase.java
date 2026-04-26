package com.sling.usecase.search;

import com.sling.model.search.Search;
import com.sling.model.search.service.SearchHashServicePort;
import com.sling.model.search.valueobject.SearchId;
import com.sling.model.search.port.EventPublisherPort;
import com.sling.usecase.IUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateSearchUseCase implements IUseCase<Search, SearchId> {
    private final EventPublisherPort publisherPort;
    private final SearchHashServicePort searchHashServicePort;

    @Override
    public SearchId execute(Search command) {
        String hash = searchHashServicePort.generateHash(command);

        Search toSave = command.toBuilder().hash(hash).build();
        publisherPort.publish(toSave);

        return new SearchId(hash);
    }
}
