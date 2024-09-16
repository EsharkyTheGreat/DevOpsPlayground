package com.esharkycorp.distributed_flow.operations;

import com.esharkycorp.distributed_flow.models.FileInformation;
import com.esharkycorp.distributed_flow.responses.FileIntegrityResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractConsumerResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractDataHandler;
import com.hilabs.cdi.common.queuebuilder.abstracts.FatalException;
import com.hilabs.cdi.common.queuebuilder.abstracts.RetryException;
import com.hilabs.cdi.common.queuebuilder.abstracts.WarningException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckFileIntegrityOperation extends AbstractDataHandler<FileInformation> {
    @Override
    public FileIntegrityResponse process(FileInformation data) throws RetryException, FatalException, WarningException {
        log.info("Checking File Integrity : {}",data);
        return new FileIntegrityResponse();
    }
}
