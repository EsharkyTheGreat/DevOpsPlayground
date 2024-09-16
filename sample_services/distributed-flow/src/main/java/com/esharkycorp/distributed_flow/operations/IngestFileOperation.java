package com.esharkycorp.distributed_flow.operations;

import com.esharkycorp.distributed_flow.models.IngestionInfo;
import com.esharkycorp.distributed_flow.responses.IngestionResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractConsumerResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractDataHandler;
import com.hilabs.cdi.common.queuebuilder.abstracts.FatalException;
import com.hilabs.cdi.common.queuebuilder.abstracts.RetryException;
import com.hilabs.cdi.common.queuebuilder.abstracts.WarningException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IngestFileOperation extends AbstractDataHandler<IngestionInfo> {
    @Override
    public IngestionResponse process(IngestionInfo data) throws RetryException, FatalException, WarningException {
        log.info("Ingesting File : {}",data);
        return new IngestionResponse();
    }
}
