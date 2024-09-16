package com.esharkycorp.distributed_flow.operations;

import com.esharkycorp.distributed_flow.models.FileInformation;
import com.esharkycorp.distributed_flow.responses.FileSizeResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractConsumerResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractDataHandler;
import com.hilabs.cdi.common.queuebuilder.abstracts.FatalException;
import com.hilabs.cdi.common.queuebuilder.abstracts.RetryException;
import com.hilabs.cdi.common.queuebuilder.abstracts.WarningException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckFileSizeOperation extends AbstractDataHandler<FileInformation> {
    @Override
    public FileSizeResponse process(FileInformation data) throws RetryException, FatalException, WarningException {
        log.info("Checking File Size : {}",data);
        return new FileSizeResponse();
    }
}
